package com.mts.cocktailbuilder.stateMangement;

import com.mts.cocktailbuilder.entity.Ingredient;

import java.util.List;

public class BarState {

    private List<Ingredient> currentBarState;

    public BarState() {

    }


    public List<Ingredient> getCurrentBarState() {
        return currentBarState;
    }

    public void setCurrentBarState(List<Ingredient> barState) {
        this.currentBarState = barState;
    }
}
