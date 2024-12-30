package com.mts.cocktailbuilder.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mts.cocktailbuilder.R;
import com.mts.cocktailbuilder.dao.IngredientsDao;
import com.mts.cocktailbuilder.dao.RecipesDao;
import com.mts.cocktailbuilder.entity.Ingredient;
import com.mts.cocktailbuilder.entity.Recipe;
import com.mts.cocktailbuilder.interfaces.UpdateList;

import java.util.List;
import java.util.Map;

public abstract class CocktailListFragment extends Fragment implements CocktailListAdapter.AdapterLongClickHandler, EditRecipeFragment.UpdateRecipeInterface, CocktailListAdapter.AdapterOnClickHandler, CocktailListAdapter.AdapterOnRatingClick, UpdateList, SearchView.OnQueryTextListener {

    // view components
    CocktailListAdapter mAdapter;
    RecyclerView recyclerView;
    NestedScrollView nestedScrollView;
    SearchView searchView;

    // class components
    List<Recipe> cocktailList;
    IngredientsDao ingredientsDao;
    RecipesDao recipesDao;


    // Search =================================================
    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return false;
    }

    // =============================================


    // Read ====================================================
    @Override
    public Map<Ingredient, String> recipeOpen(int id) {
        return ingredientsDao.getFormattedAmountIngredients(id);
    }

    // ========================================================

    // Update ========================================================
    @Override
    public void updateNotes(int id, String notes) {
        recipesDao.updateNotes(id, notes);
        this.onItemUpdate();
    }

    @Override
    public void changeRating(int id) {
        FragmentManager fm = getChildFragmentManager();
        QuickChangeRatingFragment quickChangeRatingFragment = new QuickChangeRatingFragment(this, id);
        quickChangeRatingFragment.show(fm, "quick_change_rating");
    }

    @Override
    public void onUpdateRecipeClick() {
        this.updateList();
    }

    @Override
    public void onItemUpdate() {
        this.updateList();
    }
    // ========================================================

    // Delete  ========================================================
    @Override
    public void onLongClick(int id) {
        openDeleteDialog(id);
    }

    public void openDeleteDialog(int id) {
        // Dialog code to delete recipes
        Context context = getActivity();
        View deleteRecipe = View.inflate(context, R.layout.delete_recipe_dialog, null);

        AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(this.getContext());



        deleteDialogBuilder
                .setView(deleteRecipe)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        recipesDao.deleteRecipe(id);
                        mAdapter.refreshList(recipesDao.getRecipes());
//                        ((CocktailListFragment) getParentFragment()).updateList(recipesDao.getRecipes());
                        dialog.dismiss();
                    }
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
    // ========================================================


    public abstract void updateList(List<Recipe> recipes);
    public abstract void updateList();

}
