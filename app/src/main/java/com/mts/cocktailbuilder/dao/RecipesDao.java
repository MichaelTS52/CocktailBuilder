package com.mts.cocktailbuilder.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.mts.cocktailbuilder.Utility.Utils;
import com.mts.cocktailbuilder.configuration.CocktailBuilderDBHelper;
import com.mts.cocktailbuilder.configuration.CocktailBuilderDatabase;
import com.mts.cocktailbuilder.entity.Ingredient;
import com.mts.cocktailbuilder.entity.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipesDao {

    private static CocktailBuilderDBHelper dbHelper;
    private SQLiteDatabase database;

    private static RecipesDao recipesDao = null;

    private RecipesDao(FragmentActivity activity) {
        dbHelper = new CocktailBuilderDBHelper(activity);
        database = dbHelper.getWritableDatabase();
    }

    private RecipesDao(Context context) {
        dbHelper = new CocktailBuilderDBHelper(context);
        database = dbHelper.getWritableDatabase();
    }


    private void setDbHelper(FragmentActivity activity) {
        dbHelper = new CocktailBuilderDBHelper(activity);
    }

    private void setDbHelper(Context context) {
        dbHelper = new CocktailBuilderDBHelper(context);
    }

    public static RecipesDao getInstance(FragmentActivity activity) {
        if (recipesDao == null) {
            recipesDao = new RecipesDao(activity);
        }
        recipesDao.setDbHelper(activity);
        return recipesDao;
    }

    public static RecipesDao getInstance(Context context) {
        if (recipesDao == null) {
            recipesDao = new RecipesDao(context);
        }
        recipesDao.setDbHelper(context);
        return recipesDao;
    }


    public List<Recipe> getRecipes() {
        Cursor cursor;
        try {
            cursor = database.query(
                    CocktailBuilderDatabase.RecipeEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    CocktailBuilderDatabase.RecipeEntry.COLUMN_RATING + " DESC");
        } catch (NullPointerException e) {
            return null;
        }

        return getRecipes(cursor);
    }

    public List<Recipe> getMakeableRecipes() {

        String query =
                "SELECT * FROM recipeList " +
                "WHERE _ID NOT IN ( " +
                    "SELECT recipeID FROM ingredientRelation " +
                    "WHERE ingredientID in (" +
                        "SELECT _ID FROM drinksList " +
                        "WHERE inBar = 0" +
                    ")" +
                ") ORDER BY rating DESC";
        Cursor cursor = database.rawQuery(query, new String[]{});
        return getRecipes(cursor);
    }

    public List<Recipe> getRecipes(Cursor cursor) {
        List<Recipe> recipes = new ArrayList<>();
        while (cursor.moveToNext()) {
            recipes.add(
                    new Recipe(
                            cursor.getInt((cursor.getColumnIndex(CocktailBuilderDatabase.RecipeEntry._ID))),
                            cursor.getString(cursor.getColumnIndex(CocktailBuilderDatabase.RecipeEntry.COLUMN_NAME)),
                            Utils.nullSafetoString(cursor.getString(cursor.getColumnIndex(CocktailBuilderDatabase.RecipeEntry.COLUMN_METHOD))),
                            cursor.getFloat(cursor.getColumnIndex(CocktailBuilderDatabase.RecipeEntry.COLUMN_RATING)),
                            Utils.nullSafetoString(cursor.getString(cursor.getColumnIndex(CocktailBuilderDatabase.RecipeEntry.COLUMN_NOTES)))
                    )
            );
        }
        cursor.close();
        return recipes;
    }

    public void addRecipe(Recipe recipe) throws Error {
        ContentValues cvRecipe = new ContentValues();
        cvRecipe.put(CocktailBuilderDatabase.RecipeEntry.COLUMN_NAME, recipe.getName());
        cvRecipe.put(CocktailBuilderDatabase.RecipeEntry.COLUMN_METHOD, recipe.getMethod());
        if (recipe.getRating() > 0.5) {
            float ratingBarValue = recipe.getRating();
            cvRecipe.put(CocktailBuilderDatabase.RecipeEntry.COLUMN_RATING, ratingBarValue);
        }


        ContentValues cvIngredients = new ContentValues();
        for (Map.Entry<Ingredient, Float> ingredient : recipe.getIngredients().entrySet()) {
            int ingredientID = ingredient.getKey().getId();
            float amount = ingredient.getValue();
            cvIngredients.put(CocktailBuilderDatabase.IngredientRelation.COLUMN_INGREDIENTID, ingredientID);
            cvIngredients.put(CocktailBuilderDatabase.IngredientRelation.COLUMN_AMOUNT, amount);
            cvIngredients.put(CocktailBuilderDatabase.IngredientRelation.COLUMN_RECIPEID, recipe.getId());

        }
        long res1 = -1;
        long res2 = -2;
        try {
            res1 = database.insert(CocktailBuilderDatabase.RecipeEntry.TABLE_NAME, null, cvRecipe);
            res2 = database.insert(CocktailBuilderDatabase.IngredientRelation.TABLE_NAME, null, cvIngredients);
        } catch (Error e) {
            throw e;
        } finally {
            if (res1 == -1 || res2 == -1) {
                Log.e("insert Error", String.format("SQL insert error database in inconsistent state -- Recipes table retCode:[%s], IngredientRecipeRelation retCode:[%s]", res1, res2));
            }
        }
    }

    public void deleteRecipe(Recipe recipe) {
        deleteRecipe(recipe.getId());
    }

    public void deleteRecipe(int id) {
        database.delete(
                CocktailBuilderDatabase.RecipeEntry.TABLE_NAME,
                CocktailBuilderDatabase.RecipeEntry._ID + "=" + id,
                null
        );
        database.delete(
                CocktailBuilderDatabase.IngredientRelation.TABLE_NAME,
                CocktailBuilderDatabase.IngredientRelation.COLUMN_RECIPEID + "=" + id,
                null
        );
    }

    public void updateRating(Recipe recipe) throws Error {
        updateRating(recipe.getId(), recipe.getRating());
    }

    public void updateRating(int recipeID, float newRating) throws Error {
    }

    public void updateNotes(Recipe recipe) throws Error {
        updateNotes(recipe.getId(), recipe.getNotes());
    }

    public void updateNotes(int recipeID, String newNote) throws Error {
        ContentValues cv = new ContentValues();
        cv.put(CocktailBuilderDatabase.RecipeEntry.COLUMN_NOTES, newNote);
        try {
            database.update(CocktailBuilderDatabase.RecipeEntry.TABLE_NAME, cv,
                    CocktailBuilderDatabase.RecipeEntry._ID + " = " + recipeID,
                    null);
        } catch (Error e) {
            Log.e("noteUpdate", "SQL update error");
            throw e;
        }
    }

    public List<Integer> getRecipeIdsByIngredient(@NonNull String nameMatch) {

        String query = String.format("SELECT * FROM recipeList " +
                "WHERE name LIKE '%%%1$s%%' OR " +
                "_ID IN (SELECT recipeID FROM ingredientRelation " +
                "WHERE ingredientID IN ( SELECT _ID FROM drinksList " +
                "WHERE type LIKE '%%%1$s%%' OR variant LIKE '%%%1$s%%' OR brand LIKE '%%%1$s%%'))" +
                "ORDER BY rating DESC", nameMatch);

        Cursor cursor = database.rawQuery(query, new String[]{});
        List<Integer> recipeInts = new ArrayList<>();

        while (cursor.moveToNext()) {
            recipeInts.add(cursor.getInt(cursor.getColumnIndex(CocktailBuilderDatabase.RecipeEntry._ID)));
        }
        cursor.close();
        return recipeInts;

    }


}
