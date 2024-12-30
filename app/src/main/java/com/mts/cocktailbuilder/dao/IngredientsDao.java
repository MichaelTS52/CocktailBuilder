package com.mts.cocktailbuilder.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.fragment.app.FragmentActivity;

import com.mts.cocktailbuilder.configuration.CocktailBuilderDBHelper;
import com.mts.cocktailbuilder.configuration.CocktailBuilderDatabase;
import com.mts.cocktailbuilder.entity.Ingredient;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngredientsDao {

    private static CocktailBuilderDBHelper dbHelper;
    private final SQLiteDatabase database;

    private static IngredientsDao ingredientsDao = null;

    private IngredientsDao(FragmentActivity activity) {
        dbHelper = new CocktailBuilderDBHelper(activity);
        database = dbHelper.getWritableDatabase();
    }

    private void setDbHelper(FragmentActivity activity) {
        dbHelper = new CocktailBuilderDBHelper(activity);
    }

    public void close() {
        database.close();
    }

    public static IngredientsDao getInstance(FragmentActivity activity) {
        if (ingredientsDao == null) {
            ingredientsDao = new IngredientsDao(activity);
        }
        ingredientsDao.setDbHelper(activity);
        return ingredientsDao;
    }

    public List<Ingredient> getAllIngredients() {
        String[] cols = {
                CocktailBuilderDatabase.DrinkEntry._ID,
                CocktailBuilderDatabase.DrinkEntry.COLUMN_BRAND,
                CocktailBuilderDatabase.DrinkEntry.COLUMN_VARIANT,
                CocktailBuilderDatabase.DrinkEntry.COLUMN_TYPE,
        };
        return getIngredientList(
                database.query(
                        CocktailBuilderDatabase.DrinkEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );
    }

    public List<Ingredient> getAllIngredientsInBar() {
        return getIngredientList(
                database.query(
                        CocktailBuilderDatabase.DrinkEntry.TABLE_NAME,
                        null,
                        CocktailBuilderDatabase.DrinkEntry.COLUMN_INBAR + "= 1",
                        null,
                        null,
                        null,
                        CocktailBuilderDatabase.DrinkEntry.COLUMN_TYPE
                )
        );
    }

    private List<Ingredient> getIngredientList(Cursor cursor) {
        List<Ingredient> ingredients = new ArrayList<>();
        while (cursor.moveToNext()) {
            ingredients.add(
                    new Ingredient(
                            cursor.getInt(cursor.getColumnIndex(CocktailBuilderDatabase.DrinkEntry._ID)),
                            cursor.getString(cursor.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_BRAND)),
                            cursor.getString(cursor.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_VARIANT)),
                            cursor.getString(cursor.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_TYPE)),
                            Boolean.getBoolean(String.valueOf(cursor.getInt(cursor.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_INBAR)))),
                            Boolean.getBoolean(String.valueOf(cursor.getInt(cursor.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_COMMON))))
                    )
            );
        }
        cursor.close();
        return ingredients;
    }

    public Map<Ingredient, Float> getIngredientsByRecipe(int id) {
        Cursor ingredientRecipeRelation = database.query(
                CocktailBuilderDatabase.IngredientRelation.TABLE_NAME,
                null,
                CocktailBuilderDatabase.IngredientRelation.COLUMN_RECIPEID + " = " + id,
                null,
                null,
                null,
                null
        );
        Map<Ingredient, Float> ingredientAmounts = new HashMap<>();
        while (ingredientRecipeRelation.moveToNext()) {
            int ingredientId = ingredientRecipeRelation.getInt(ingredientRecipeRelation.getColumnIndex(CocktailBuilderDatabase.IngredientRelation.COLUMN_INGREDIENTID));
            float amount = ingredientRecipeRelation.getFloat(ingredientRecipeRelation.getColumnIndex(CocktailBuilderDatabase.IngredientRelation.COLUMN_AMOUNT));

            Cursor ingredientDetail = database.query(
                    CocktailBuilderDatabase.DrinkEntry.TABLE_NAME,
                    null,
                    CocktailBuilderDatabase.DrinkEntry._ID + " = " + ingredientId,
                    null,
                    null,
                    null,
                    CocktailBuilderDatabase.DrinkEntry.COLUMN_TYPE
            );
            ingredientDetail.moveToFirst();
            Ingredient ingredient = new Ingredient(
                    ingredientId,
                    ingredientDetail.getString(ingredientDetail.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_BRAND)),
                    ingredientDetail.getString(ingredientDetail.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_VARIANT)),
                    ingredientDetail.getString(ingredientDetail.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_TYPE)),
                    Boolean.parseBoolean(ingredientDetail.getString(ingredientDetail.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_INBAR))),
                    Boolean.parseBoolean(ingredientDetail.getString(ingredientDetail.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_INBAR)))
            );
            ingredientAmounts.put(ingredient, amount);
            ingredientDetail.close();
        }
        ingredientRecipeRelation.close();
        return ingredientAmounts;

    }

    public void removeFromDatabase(Ingredient ingredient) {
        removeFromDatabase(ingredient.getId());
    }

    public void removeFromDatabase(int id) {
        database.delete(
                CocktailBuilderDatabase.DrinkEntry.TABLE_NAME,
                CocktailBuilderDatabase.DrinkEntry._ID + "=" + id,
                null
        );
    }

    public void removeFromBar(Ingredient ingredient) {
        removeFromBar(ingredient.getId());
    }

    public void removeFromBar(int id) {
        ContentValues cv = new ContentValues();
        cv.put(CocktailBuilderDatabase.DrinkEntry.COLUMN_INBAR, 0);
        database.update(
                CocktailBuilderDatabase.DrinkEntry.TABLE_NAME,
                cv,
                CocktailBuilderDatabase.DrinkEntry._ID + "=" + id,
                null
        );
    }


    public Map<Ingredient, String> getFormattedAmountIngredients(int id) {
        Map<Ingredient, Float> ingredients = getIngredientsByRecipe(id);
        Map<Ingredient, String> ingredientsFormattedAmounts = new HashMap<>();
        for (Map.Entry<Ingredient, Float> ingredient : ingredients.entrySet()) {
            float amount = ingredient.getValue();
            String amountRep;
            if (amount < 0.22) {
                amountRep = "Splash";
            } else if (0.22 <= amount && 0.33 > amount) {
                amountRep = "1/4";
            } else if (0.33 <= amount && 0.55 > amount) {
                amountRep = "1/2";
            } else if (0.55 <= amount && 1 > amount) {
                amountRep = "3/4";
            } else {
                amountRep = "  " + Math.round(amount) + "  ";
            }
            ingredientsFormattedAmounts.put(ingredient.getKey(), amountRep);
        }
        return ingredientsFormattedAmounts;
    }
}
