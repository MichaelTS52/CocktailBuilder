package com.mts.cocktailbuilder.entity;


import java.util.Objects;

public class Ingredient {

    private int id;

    private String brand;

    private String variant;

    private String type;

    private String name;

    private boolean inBar;

    private boolean isCommon;

    public Ingredient(int id, String brand, String variant, String type, boolean inBar, boolean isCommon) {
        this.id = id;
        this.brand = brand;
        this.variant = variant;
        this.type = type;
        this.name = (brand == null ? "" : brand) + (variant == null ? "" : (" " + variant)) + " " + type;
        this.inBar = inBar;
        this.isCommon = isCommon;
    }

    public Ingredient(int id, String variant, String type, boolean inBar, boolean isCommon){
        this(id, "", variant, type, inBar, isCommon);
    }

    public Ingredient(int id, String variant, String type, boolean inBar){
        this(id,"", variant, type, inBar, false);
    }

    public Ingredient(int id, String variant, String type){
        this(id, "", variant, type, false, false);
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInBar() {
        return inBar;
    }

    public void setInBar(boolean inBar) {
        this.inBar = inBar;
    }

    public boolean isCommon() {
        return isCommon;
    }

    public void setCommon(boolean common) {
        isCommon = common;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return this.name.equals(that.name);
    }

    @Override
    public String toString() {
        return "ingredient{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
