package com.mts.cocktailbuilder.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

public class IngredientAddAutoComplete extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {


    public IngredientAddAutoComplete(Context context) {
        super(context);
    }

    public IngredientAddAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IngredientAddAutoComplete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean enoughToFilter(){
        return true;
    }


    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocused){
            super.onFocusChanged(focused,direction,previouslyFocused);
    }

    @Override
    public void setThreshold(int threshold){
        int newThreshold = 1;
        super.setThreshold(newThreshold);
    }
}

