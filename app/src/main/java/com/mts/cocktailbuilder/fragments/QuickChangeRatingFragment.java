package com.mts.cocktailbuilder.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mts.cocktailbuilder.R;
import com.mts.cocktailbuilder.configuration.CocktailBuilderDBHelper;
import com.mts.cocktailbuilder.configuration.CocktailBuilderDatabase;
import com.mts.cocktailbuilder.interfaces.UpdateList;

public class QuickChangeRatingFragment extends DialogFragment {

    private RatingBar ratingBar;
    private Button saveButton;

    private SQLiteDatabase database;
    private final int recipeID;

    UpdateList mUpdateList;

    public QuickChangeRatingFragment(UpdateList updateList, int recipeID){
        mUpdateList = updateList;
        this.recipeID = recipeID;

    }


    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.change_rating, container);
        v.setBackgroundColor(getResources().getColor(android.R.color.transparent, Resources.getSystem().newTheme()));
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        ratingBar = v.findViewById(R.id.quick_change_rating_bar);
        saveButton = v.findViewById(R.id.changeRatingSaveButton);

        CocktailBuilderDBHelper dbHelper = new CocktailBuilderDBHelper(getActivity());
        database = dbHelper.getWritableDatabase();


        saveButton.setOnClickListener(view ->{
            updateRating(recipeID, ratingBar.getRating());
            this.mUpdateList.onItemUpdate();
            dismiss();
        });


        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ratingBar = view.findViewById(R.id.quick_change_rating_bar);
        saveButton = view.findViewById(R.id.changeRatingSaveButton);

        Cursor rating = database.query(
                CocktailBuilderDatabase.RecipeEntry.TABLE_NAME,
                null,
                CocktailBuilderDatabase.RecipeEntry._ID + " = " + recipeID,
                null,
                null,
                null,
                null
        );

        int oldRating = rating.getColumnIndex(CocktailBuilderDatabase.RecipeEntry.COLUMN_RATING);
        ratingBar.setRating(oldRating);
        rating.close();
    }




    public void updateRating(int id, Float rating){
        ContentValues cv = new ContentValues();
        cv.put(CocktailBuilderDatabase.RecipeEntry.COLUMN_RATING, rating);
        database.update(
                CocktailBuilderDatabase.RecipeEntry.TABLE_NAME,
                cv,
                CocktailBuilderDatabase.RecipeEntry._ID + "=" + id,
                null
        );
    }

}
