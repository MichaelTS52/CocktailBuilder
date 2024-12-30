package com.mts.cocktailbuilder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mts.cocktailbuilder.R;
import com.mts.cocktailbuilder.dao.IngredientsDao;
import com.mts.cocktailbuilder.dao.RecipesDao;
import com.mts.cocktailbuilder.entity.Recipe;

import java.util.List;

public class MakeListFragment extends CocktailListFragment {

    public MakeListFragment(){
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (cocktailList != null && !cocktailList.isEmpty()) {
                updateList(cocktailList);
            }else {
                updateList();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ingredientsDao = IngredientsDao.getInstance(getActivity());
        recipesDao = RecipesDao.getInstance(getActivity());
        cocktailList = recipesDao.getMakeableRecipes();

        View view = inflater.inflate(R.layout.makeable_list, container, false);

        recyclerView = view.findViewById(R.id.makeable_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new CocktailListAdapter(this.getContext(), cocktailList,this, this, this, this);
        recyclerView.setAdapter(mAdapter);


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
        searchView.setOnCloseListener(() -> {
            searchView.clearFocus();
            return false;
        });

        return view;
    }


    @Override
    public void updateList(){
        cocktailList = recipesDao.getMakeableRecipes();
        mAdapter.refreshList(cocktailList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateList(List<Recipe> recipes) {
        mAdapter.refreshList(recipes);
        mAdapter.notifyDataSetChanged();
    }
}
