package com.mts.cocktailbuilder.configuration;

import android.provider.BaseColumns;

public class CocktailBuilderDatabase {


    private CocktailBuilderDatabase(){}

    public static final class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME = "recipeList";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_METHOD ="method";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_NOTES = "notes";

    }
    public static final class DrinkEntry implements BaseColumns {
        public static final String TABLE_NAME = "drinksList";
        public static final String COLUMN_BRAND = "brand";
        public static final String COLUMN_VARIANT = "variant";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_INBAR = "inBar";
        public static final String COLUMN_COMMON = "COMMON";

    }
    public static final class IngredientRelation implements BaseColumns {
        public static final String TABLE_NAME = "ingredientRelation";
        public static final String COLUMN_INGREDIENTID = "ingredientID";
        public static final String COLUMN_RECIPEID = "recipeID";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_SUBSTITUTE = "substitute";

    }

//    public static void exportDB() {
//        try {
//            File sd = Environment.getExternalStorageDirectory();
//            File data = Environment.getDataDirectory();
//
//            if(sd.canWrite()){
//                String currentDBPath = "\\cocktailBuilderBackup\\" + CocktailBuilderDBHelper.DATABASE_NAME;
//                String copyDBPath = C
//            }
//        }
//    }
}
