package com.mts.cocktailbuilder.entity;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.LinkedHashMap;
import java.util.Map;

public class Recipe {

    private int id;

    private String name;

    private String method;

    private float rating;

    private String notes;

    private Map<Ingredient, Float> ingredients;

    public Recipe(int id, String name, String method, float rating, String notes, Map<Ingredient, Float> ingredients) {
        this.id = id;
        this.name = name;
        this.method = method;
        this.rating = rating;
        this.notes = notes;
        this.ingredients = ingredients;
    }

    public Recipe(int id, String name, String method, float rating, String notes) {
        this(id, name, method, rating, notes, new LinkedHashMap<>());
    }

    public Recipe(int id, String name) {
        this(id, name, "", 0, "");
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public Map<Ingredient, Float> getIngredients() {
        return ingredients;
    }

    public void addIngredient(Ingredient ingredient, Float amount){
        ingredients.put(ingredient, amount);
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeIngredient(String ingredientName) {
        ingredients.entrySet().removeIf(e -> e.getKey().getName().equals(ingredientName));
        ingredients.entrySet().stream();
    }


}
