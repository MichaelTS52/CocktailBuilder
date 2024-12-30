package com.mts.cocktailbuilder.mybar;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mts.cocktailbuilder.R;
import com.mts.cocktailbuilder.configuration.CocktailBuilderDBHelper;
import com.mts.cocktailbuilder.configuration.CocktailBuilderDatabase;
import com.mts.cocktailbuilder.fragments.MakeListFragment;
import com.mts.cocktailbuilder.interfaces.UpdateList;

public class MyBarFragment extends Fragment implements MyBarAdapter.AdapterLongClickHandler , AddBarItemFragment.AddItemInterface, UpdateList {
    private SQLiteDatabase database;
    private MyBarAdapter mAdapter;
    private RecyclerView recyclerView;
    private static CocktailBuilderDBHelper dbHelper;
    private BarChangedNotifier mBarChangedNotifier;


    public MyBarFragment(){
    }

    public interface BarChangedNotifier {
        void onBarChange();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mBarChangedNotifier = (BarChangedNotifier) getActivity();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_bar, container, false);

        dbHelper = new CocktailBuilderDBHelper(getActivity());
        database = dbHelper.getWritableDatabase();

        recyclerView = view.findViewById(R.id.bar_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new MyBarAdapter(this.getContext(), loadAll(), this);
        recyclerView.setAdapter(mAdapter);



        FloatingActionButton add_ingredient = view.findViewById(R.id.add_bar_item_button);


        add_ingredient.setOnClickListener(v -> {
            FragmentManager fm = getChildFragmentManager();
            QuickAddBarItemFragment quickAddBarItemFragment = new QuickAddBarItemFragment(this);
            quickAddBarItemFragment.show(fm,"quick_add");
        });

        return view;
    }


    private Cursor loadAll(){
        return database.query(
                CocktailBuilderDatabase.DrinkEntry.TABLE_NAME,
                null,
                CocktailBuilderDatabase.DrinkEntry.COLUMN_INBAR + "= 1",
                null,
                null,
                null,
                CocktailBuilderDatabase.DrinkEntry.COLUMN_TYPE
        );
    }




    // Handles Deleting Items from list via interface with RecyclerView Adapter MyBarAdapter

    @Override
    public void onLongClick(int id) {
        openDeleteDialog(this.getView(), id);
    }


    public void openDeleteDialog(View view, int id) {
        // Dialog code to delete items from bar
        Context context = getActivity();
        View checkBoxView = View.inflate(context, R.layout.delete_item_checkbox, null);
        CheckBox permDeleteCheck =  checkBoxView.findViewById(R.id.delete_checkbox);

        AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(this.getContext());

        deleteDialogBuilder
                .setView(checkBoxView)
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean permDelete = permDeleteCheck.isChecked();
                    if(permDelete){
                        deleteFromDatabase(id);
                    }else{
                        deleteItemFromBar(id);
                    }
                    Cursor cursor = loadAll();
                    mAdapter.swapCursor(cursor);
                    dialog.cancel();
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog deleteDialog = deleteDialogBuilder.create();
        deleteDialog.getWindow().setBackgroundDrawableResource(R.color.white_blue);

        deleteDialog.show();
        Button deleteButton = deleteDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        deleteButton.setTextColor(Color.RED);

        Button cancelButton = deleteDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        cancelButton.setTextColor(Color.GRAY);

    }

    private void deleteFromDatabase(int id){
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(
                CocktailBuilderDatabase.IngredientRelation.TABLE_NAME,
                null,
                CocktailBuilderDatabase.IngredientRelation.COLUMN_INGREDIENTID + "= " + id,
                null,
                null,
                null,
                null,
                null
        );
        if(cursor.getCount() > 0){
            Toast.makeText(getContext(),"First delete all recipes using this ingredient before" +
                    "deleting from database", Toast.LENGTH_LONG).show();
        }else {
            database.delete(
                    CocktailBuilderDatabase.DrinkEntry.TABLE_NAME,
                    CocktailBuilderDatabase.DrinkEntry._ID + "=" + id,
                    null
            );
            mBarChangedNotifier.onBarChange();
        }
    }

    private void deleteItemFromBar(int id){
        ContentValues cv = new ContentValues();
        cv.put(CocktailBuilderDatabase.DrinkEntry.COLUMN_INBAR, 0);
        database = dbHelper.getWritableDatabase();
        database.update(
                CocktailBuilderDatabase.DrinkEntry.TABLE_NAME,
                cv,
                CocktailBuilderDatabase.DrinkEntry._ID + "=" + id,
                null
        );
        mBarChangedNotifier.onBarChange();
    }


    //handles updating RecyclerView once item has been added to database
    @Override
    public void onAddItemClick() {
        mBarChangedNotifier.onBarChange();
        mAdapter.swapCursor(loadAll());
    }

    // handles update from quick add
    @Override
    public void onItemUpdate() {
        mBarChangedNotifier.onBarChange();
        mAdapter.swapCursor(loadAll());
    }
}

