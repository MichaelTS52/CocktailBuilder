package com.mts.cocktailbuilder.mybar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.mts.cocktailbuilder.R;
import com.mts.cocktailbuilder.configuration.CocktailBuilderDBHelper;
import com.mts.cocktailbuilder.configuration.CocktailBuilderDatabase;
import com.mts.cocktailbuilder.interfaces.UpdateList;

import java.util.HashMap;
import java.util.Map;

public class QuickAddBarItemFragment extends DialogFragment {
    private Context mContext;

    private Map<String, Integer> ingredientIDs = new HashMap<>();
    private String[] ingredientList;

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> autoCompleteAdapter;
    private LinearLayout placeholder;
    private View buttonView;
    private boolean addtiionalIngredientButton = false;

    UpdateList mUpdateList;

    private SQLiteDatabase database;
    private CocktailBuilderDBHelper dbHelper;


    public QuickAddBarItemFragment(UpdateList updateList){
        mUpdateList = updateList;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.quick_add_bar_item, container);
        v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);


        placeholder = view.findViewById(R.id.additional_ingredient_button_placeholder);
        autoCompleteTextView = view.findViewById(R.id.quick_bar_add_autocomplete);
        autoCompleteTextView.requestFocus();

        dbHelper = new CocktailBuilderDBHelper(getActivity());
        database = dbHelper.getWritableDatabase();

        setUpAutoComplete(); //Function used to set and update autocomplete
        autoCompleteAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.select_dialog_item, ingredientList);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 || (autoCompleteAdapter.getCount() != 0 && placeholder.getChildCount() != 0)) {
                    removeButton(buttonView);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (autoCompleteAdapter.getCount() == 0 && !addtiionalIngredientButton){
                    Toast.makeText(mContext, "No results found", Toast.LENGTH_SHORT).show();
                    addAdditionalIngredientButton(view, s);
                }

            }
        });

        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_GO) {
                   if(ingredientIDs.get(autoCompleteTextView.getText().toString()) != null){
                       updateBar(ingredientIDs.get(autoCompleteTextView.getText().toString()));
                       autoCompleteTextView.setText("");
                   }
                }
                return true;
            }
        });
    }


    public void setUpAutoComplete(){
        ingredientIDs.clear();
        Cursor mCursor = loadIngredientList();
        while (mCursor.moveToNext()){
            String name = mCursor.getString(mCursor.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_BRAND))
                    + " " + mCursor.getString(mCursor.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_VARIANT))
                    + " " + mCursor.getString(mCursor.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_TYPE));
            int id = mCursor.getInt(mCursor.getColumnIndex(CocktailBuilderDatabase.DrinkEntry._ID));
            ingredientIDs.put(name,id);
        }

        ingredientList = ingredientIDs.keySet().toArray(new String[ingredientIDs.size()]);
    }

    protected Cursor loadIngredientList(){
        String[] cols = {
                CocktailBuilderDatabase.DrinkEntry.COLUMN_BRAND,
                CocktailBuilderDatabase.DrinkEntry.COLUMN_VARIANT,
                CocktailBuilderDatabase.DrinkEntry.COLUMN_TYPE,
                CocktailBuilderDatabase.DrinkEntry._ID
        };
        return database.query(
                CocktailBuilderDatabase.DrinkEntry.TABLE_NAME,
                cols,
                CocktailBuilderDatabase.DrinkEntry.COLUMN_INBAR + "=" + 0,
                null,
                null,
                null,
                null
        );
    }

    public void addAdditionalIngredientButton(View view, CharSequence s){
        if(addtiionalIngredientButton || s.length() == 0){
            return;
        }
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        buttonView = inflater.inflate(R.layout.additional_ingredient_button_layout, null);
        Button button = buttonView.findViewById(R.id.additional_ingredient_button);
        placeholder.addView(buttonView, placeholder.getChildCount());

        button.setOnClickListener(v -> {
            getDialog().hide();
            FragmentManager fm = getChildFragmentManager();
            MyBarFragment barFragment = (MyBarFragment) getParentFragment();
            AddBarItemFragment addBarItemFragment = new AddBarItemFragment(barFragment);
            addBarItemFragment.show(fm,"add_bar_item");
            autoCompleteTextView.setText("");
        });
        addtiionalIngredientButton = true;

    }

    public void removeButton(View view){
        placeholder.removeView(view);
        addtiionalIngredientButton = false;
        setUpAutoComplete();
        autoCompleteAdapter.notifyDataSetChanged();
    }

    public void updateBar(int id){
        ContentValues cv = new ContentValues();
        cv.put(CocktailBuilderDatabase.DrinkEntry.COLUMN_INBAR, 1);
        database.update(
                CocktailBuilderDatabase.DrinkEntry.TABLE_NAME,
                cv,
                CocktailBuilderDatabase.DrinkEntry._ID + "=" + id,
                null
        );
        mUpdateList.onItemUpdate();
    }
}
