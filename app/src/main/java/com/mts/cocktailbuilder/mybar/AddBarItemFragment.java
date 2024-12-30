package com.mts.cocktailbuilder.mybar;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mts.cocktailbuilder.R;
import com.mts.cocktailbuilder.configuration.CocktailBuilderDBHelper;
import com.mts.cocktailbuilder.configuration.CocktailBuilderDatabase;


public class AddBarItemFragment extends DialogFragment {
    private EditText brand;
    private EditText variant;
    private EditText type;
    private Switch commonSwitch;
    private CheckBox inBarCheckBox;
    private Button addItemButton;
    AddItemInterface mAddItemHandler;
    private String whereFrom;

    private SQLiteDatabase database;
    private CocktailBuilderDBHelper dbHelper;


    public AddBarItemFragment(AddItemInterface AddItemHandler){
        mAddItemHandler = AddItemHandler;
    }
    public AddBarItemFragment(){
        whereFrom = this.getTag();
    }

    public interface AddItemInterface{
        void onAddItemClick();
    }

    @Override
    public void onDismiss(final DialogInterface dialog){
        super.onDismiss(dialog);
    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dialogue_add_bar_item, container);
        v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        dbHelper = new CocktailBuilderDBHelper(getActivity());


        brand = v.findViewById(R.id.add_brand_text);
        variant= v.findViewById(R.id.add_variant_text);
        type = v.findViewById(R.id.add_type_text);
        commonSwitch = v.findViewById(R.id.common_switch);
        inBarCheckBox = v.findViewById(R.id.in_bar_checkbox);
        addItemButton = v.findViewById(R.id.add_new_item);

        whereFrom = this.getTag();
        if(whereFrom.equals("add_new_ingredient")) {
        inBarCheckBox.setChecked(false);
        }

        addItemButton.setOnClickListener(v1 -> {
            addIngredient();
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        EditText brandText = view.findViewById(R.id.add_brand_text);
        brandText.requestFocus();
    }


    public void addIngredient(){
        String brandText = brand.getText().toString().toLowerCase().trim();
        String variantText = variant.getText().toString().toLowerCase().trim();
        String typeText = type.getText().toString().toLowerCase().trim();
        int inBar = inBarCheckBox.isChecked() ? 1 : 0;
        int commonItem = commonSwitch.isChecked() ? 1 : 0;
        CharSequence error = "Type field cannot be empty";
        if (typeText.length() == 0){
            Toast.makeText(getDialog().getContext(), error,Toast.LENGTH_LONG).show();
        }
        else{

            ContentValues cv = new ContentValues();
            cv.put(CocktailBuilderDatabase.DrinkEntry.COLUMN_BRAND, brandText);
            cv.put(CocktailBuilderDatabase.DrinkEntry.COLUMN_VARIANT, variantText);
            cv.put(CocktailBuilderDatabase.DrinkEntry.COLUMN_TYPE, typeText);
            cv.put(CocktailBuilderDatabase.DrinkEntry.COLUMN_COMMON, commonItem);
            //default is true in database
            if (inBar == 0){
                cv.put(CocktailBuilderDatabase.DrinkEntry.COLUMN_INBAR, inBar);
            }
            try {
                database = dbHelper.getWritableDatabase();
                database.insert(CocktailBuilderDatabase.DrinkEntry.TABLE_NAME, null, cv);
                database.close();
                if(!whereFrom.equals("add_new_ingredient")) {
                    mAddItemHandler.onAddItemClick();
                }


            }catch (Error e){
                CharSequence SQLError = "Error adding item to database";
                Toast.makeText(getDialog().getContext(), SQLError, Toast.LENGTH_SHORT).show();
                Log.e("myBarAdd", "SQL error");
            }
            getDialog().dismiss();
        }

    }




}
