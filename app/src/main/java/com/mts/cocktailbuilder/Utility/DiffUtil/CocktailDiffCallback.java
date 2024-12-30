package com.mts.cocktailbuilder.Utility.DiffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.mts.cocktailbuilder.entity.Recipe;

import java.util.List;

public class CocktailDiffCallback extends DiffUtil.Callback {

    private final List<Recipe> mOldCocktailList;
    private final List<Recipe> mNewCocktailList;

    public CocktailDiffCallback(List<Recipe> oldCocktailList, List<Recipe> newCocktailList) {
        this.mOldCocktailList = oldCocktailList;
        this.mNewCocktailList = newCocktailList;
    }

    @Override
    public int getOldListSize() {

        return mOldCocktailList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewCocktailList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldCocktailList.get(oldItemPosition).getId() == mNewCocktailList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Recipe oldRecipe = mOldCocktailList.get(oldItemPosition);
        final Recipe newRecipe = mNewCocktailList.get(newItemPosition);
        return
                oldRecipe.getName().equals(newRecipe.getName()) &&
                oldRecipe.getRating() == newRecipe.getRating() &&
                oldRecipe.getMethod().equals(newRecipe.getMethod()) &&
                oldRecipe.getNotes().equals(newRecipe.getNotes());
    }
}
