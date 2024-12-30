package com.mts.cocktailbuilder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mts.cocktailbuilder.R;
import com.mts.cocktailbuilder.dao.IngredientsDao;
import com.mts.cocktailbuilder.dao.RecipesDao;
import com.mts.cocktailbuilder.entity.Recipe;

import java.util.List;

public class FullListFragment extends CocktailListFragment implements AddRecipeFragment.AddRecipeInterface {

    public FullListFragment() {
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (getView() != null) {
//            if (isVisibleToUser) {
//                if (cocktailList != null && !cocktailList.isEmpty()) {
//                    updateList(cocktailList);
//                } else {
//                    updateList();
//                }
//            }
//        }
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ingredientsDao = IngredientsDao.getInstance(getActivity());
        recipesDao = RecipesDao.getInstance(getActivity());
        cocktailList = recipesDao.getRecipes();

        View view = inflater.inflate(R.layout.fragment_cocktail_list, container, false);


        recyclerView = view.findViewById(R.id.full_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new CocktailListAdapter(this.getContext(), cocktailList, this, this, this, this);
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton add_recipe = view.findViewById(R.id.add_recipe_button);

        add_recipe.setOnClickListener(v -> {
            FragmentManager fm = getChildFragmentManager();
            AddRecipeFragment addRecipeFragment = new AddRecipeFragment(this);
            addRecipeFragment.show(fm, "add_recipe");
            addRecipeFragment.onDestroy();
        });


        nestedScrollView = view.findViewById(R.id.nested_scroll);
        searchView = view.findViewById(R.id.action_search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return view;
    }

    @Override
    public void onAddRecipeClick() {
        this.updateList(recipesDao.getRecipes());
    }


    @Override
    public void updateList() {
       mAdapter.refreshList(recipesDao.getRecipes());

//        for (Integer pos : changed) {
//            mAdapter.notifyAtPosition(pos);
//        }

//        mAdapter.notifyItemInserted();
    }

    @Override
    public void updateList(List<Recipe> recipes) {
        mAdapter.refreshList(recipes);

//        for (Integer pos : changed) {
//            mAdapter.notifyAtPosition(pos);
//        }
//        mAdapter.notifyDataSetChanged();
    }
}
