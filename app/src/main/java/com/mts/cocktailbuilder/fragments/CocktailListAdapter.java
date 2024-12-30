package com.mts.cocktailbuilder.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.mts.cocktailbuilder.R;
import com.mts.cocktailbuilder.Utility.DiffUtil.CocktailDiffCallback;
import com.mts.cocktailbuilder.dao.RecipesDao;
import com.mts.cocktailbuilder.entity.Ingredient;
import com.mts.cocktailbuilder.entity.Recipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CocktailListAdapter extends RecyclerView.Adapter<CocktailListAdapter.CocktailListViewHolder> implements Filterable {
    private Context mContext;
    private List<Recipe> filteredRecipeList;
    private static List<Recipe> mRecipeList;
    private Map<Ingredient, String> mIngredientAmounts;

    AdapterLongClickHandler mLongClickHandler;
    AdapterOnClickHandler mOnClickHandler;
    AdapterOnRatingClick mOnRatingClickHandler;
    EditRecipeFragment.UpdateRecipeInterface mUpdateRecipeInterface;

    private boolean anyExpanded;
    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;
    private  HashMap<Integer, String> notesMap = new HashMap<>();
    private int previousRecipeID = -1;
    private RecyclerView recyclerView;
    private long mLastClickTime = 0;

    private boolean isExpanded = false;

    private RecipesDao recipesDao;

    @Override
    public Filter getFilter() {
        return new CocktailFilter();
    }

    public interface AdapterLongClickHandler {
        void onLongClick(int id);
    }

    public interface AdapterOnClickHandler {
        Map<Ingredient, String> recipeOpen(int id);
        void updateNotes(int id, String notes);
    }

    public interface AdapterOnRatingClick{
        void changeRating(int id);
    }

    private boolean animating=false;

    public void notifyAtPosition(int pos) {
        animating = true;
        notifyItemChanged(pos);
    }

    public void update(List<Recipe> recipes) {
//        mRecipeList = recipes;
        filteredRecipeList = recipes;
        notifyDataSetChanged();
    }

    public void bindUnfoldedState(View v, int pos) {
        if(animating) {
            v.animate()
//                    .alpha(1.0f)
//                    .setDuration(300)
                    .translationY(-v.getHeight())
                    .translationY(0)
                    .setDuration(500)   .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    v.setVisibility(View.VISIBLE);
                }
            });
        }else {
            v.animate()
//                    .alpha(0.0f)
//                    .setDuration(300)
                    .translationY(-v.getHeight())
                    .setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    v.setVisibility(View.GONE);
                }
            });
        }
    }


    public CocktailListAdapter(Context context, List<Recipe> recipes, AdapterLongClickHandler longClickHandler, AdapterOnClickHandler onClickHandler, AdapterOnRatingClick onRatingClickHandler, EditRecipeFragment.UpdateRecipeInterface updateRecipeInterface) {
        filteredRecipeList = recipes;
        mRecipeList = new ArrayList<>(recipes);
        mContext = context;
        mLongClickHandler = longClickHandler;
        mOnClickHandler = onClickHandler;
        mOnRatingClickHandler = onRatingClickHandler;
        mUpdateRecipeInterface = updateRecipeInterface;

        recipesDao = RecipesDao.getInstance(context.getApplicationContext());
    }

    public class CocktailListViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView ratingText;
        RelativeLayout container;
        RelativeLayout recipeDetails;
        TextView methodText;
        EditText notesText;
        LinearLayout ingredientsList;
        int recipeID;
        String currentNote;
        FloatingActionButton noteSaveButton;
        FloatingActionButton editRecipeButton;


        public CocktailListViewHolder(@NonNull View view) {
            super(view);
            this.nameText = view.findViewById(R.id.drink_row_text_view);
            this.ratingText = view.findViewById(R.id.drink_row_rating_text);
            this.container = view.findViewById(R.id.drink_row);
            this.recipeDetails = view.findViewById(R.id.recipe_details);
            this.methodText = view.findViewById(R.id.method_text);
            this.notesText = view.findViewById(R.id.view_recipe_notes);
            this.ingredientsList = view.findViewById(R.id.view_recipe_ingredients_list);
            this.noteSaveButton = view.findViewById(R.id.note_save_button);
            this.editRecipeButton = view.findViewById(R.id.edit_recipe_button);



            this.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    currentNote = notesText.getText().toString();

                    //save previous open
                    if(mExpandedPosition != -1){
                        previousExpandedPosition = mExpandedPosition;

                    }else{
                        previousExpandedPosition = -1;
                    }

                    //mark new open
                    mExpandedPosition = mExpandedPosition == position ? -1:position;
                    if (nameText.getTag() != null && nameText.getTag().equals("isOpen")){
                        nameText.setTag("isClosed");
                    }else {
                        nameText.setTag("isOpen");
                    }

                    //send to onBindViewHolder for update
//                    notifyDataSetChanged();
                    notifyAtPosition(position);
                    if (previousExpandedPosition != -1) {
                        notifyAtPosition(previousExpandedPosition);
                    }

                }

            });


            this.nameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    currentNote = notesText.getText().toString();

                    //save previous open
                    if(mExpandedPosition != -1){
                        previousExpandedPosition = mExpandedPosition;

                    }else{
                        previousExpandedPosition = -1;
                    }

                    //mark new open
                    mExpandedPosition = mExpandedPosition == position ? -1:position;
                    if (nameText.getTag() != null && nameText.getTag().equals("isOpen")){
                        nameText.setTag("isClosed");
                    }else {
                        nameText.setTag("isOpen");
                    }

                    //send to onBindViewHolder for update
//                    notifyDataSetChanged();
                    notifyAtPosition(position);
                    if (previousExpandedPosition != -1) {
                        notifyAtPosition(previousExpandedPosition);
                    }

                }

            });

            ratingText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnRatingClickHandler.changeRating(recipeID);
                }
            });

            nameText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickHandler.onLongClick(recipeID);
                    return false;
                }
            });

          noteSaveButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  mOnClickHandler.updateNotes(recipeID, notesText.getText().toString().trim());
              }
          });


          editRecipeButton.setOnClickListener(v->{
                FragmentManager fm = ((AppCompatActivity)mContext).getSupportFragmentManager();
                EditRecipeFragment editRecipeFragment = new EditRecipeFragment(recipeID, mUpdateRecipeInterface);
                editRecipeFragment.show(fm, "edit_recipe");
            });

        }
    }

    @NonNull
    @Override
    public CocktailListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.drink_row, parent, false);
        return new CocktailListViewHolder(view);
        }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CocktailListViewHolder holder, int position) {

        holder.nameText = holder.itemView.findViewById(R.id.drink_row_text_view);
        holder.ratingText = holder.itemView.findViewById(R.id.drink_row_rating_text);
        holder.container = holder.itemView.findViewById(R.id.drink_row);
        holder.recipeDetails = holder.itemView.findViewById(R.id.recipe_details);
        holder.methodText = holder.itemView.findViewById(R.id.method_text);
        holder.notesText = holder.itemView.findViewById(R.id.view_recipe_notes);
        holder.ingredientsList = holder.itemView.findViewById(R.id.view_recipe_ingredients_list);


        if (position < filteredRecipeList.size()) {
            Recipe recipe = filteredRecipeList.get(position);
            holder.recipeID = recipe.getId();
            holder.nameText.setText(recipe.getName());
            holder.ratingText.setText(String.valueOf(recipe.getRating()));
            holder.methodText.setText(recipe.getMethod());
            holder.notesText.setText(recipe.getNotes());

            if(mIngredientAmounts != null){
                mIngredientAmounts.clear();
            }
            mIngredientAmounts = mOnClickHandler.recipeOpen(holder.recipeID); // Loads all ingredients


            // todo: this is masking a bug where certain recipes are being filled with wrong ingredients on first open.
            holder.ingredientsList.removeAllViews();

            // Display ingredient list
            int numIngredients = 0;
            for (Map.Entry<Ingredient, String> ingredient : mIngredientAmounts.entrySet()) {

                // expand populate and format ingredient views
                if (holder.ingredientsList.getChildCount() < mIngredientAmounts.size()) {
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View ingredientView = inflater.inflate(R.layout.recipe_ingredient_field, null, false);
                    TextView nameText = ingredientView.findViewById(R.id.ingredient_name);
                    TextView quantityText = ingredientView.findViewById(R.id.ingredient_quantity);
                    nameText.setTextColor(Color.BLACK);
                    quantityText.setTextColor(Color.BLACK);
                    nameText.setText(String.format("%s. %s", numIngredients + 1, ingredient.getKey().getName()));

                    // format fractional amounts
                    if(ingredient.getValue().contains("/")){
                        quantityText.setText(HtmlCompat.fromHtml(String.format("<sup>%s</sup>&frasl;<sub>%s</sub>", ingredient.getValue().charAt(0), ingredient.getValue().charAt(2)), HtmlCompat.FROM_HTML_MODE_LEGACY));
                    } else {
                        quantityText.setText(ingredient.getValue());
                    }
                    holder.ingredientsList.addView(ingredientView, holder.ingredientsList.getChildCount());
                } else {
                    LinearLayout childLayout = (LinearLayout) holder.ingredientsList.getChildAt(numIngredients);
                    TextView nameText = childLayout.findViewById(R.id.ingredient_name);
                    TextView quantityText = childLayout.findViewById(R.id.ingredient_quantity);
                    nameText.setTextColor(Color.BLACK);
                    quantityText.setTextColor(Color.BLACK);
                    nameText.setText(String.format("%s. %s", numIngredients + 1, ingredient.getKey()));

                    // format fractional amounts
                    if(ingredient.getValue().contains("/")){
                        quantityText.setText(HtmlCompat.fromHtml(String.format("<sup>%s</sup>&frasl;<sub>%s</sub>", ingredient.getValue().charAt(0), ingredient.getValue().charAt(2)), HtmlCompat.FROM_HTML_MODE_LEGACY));
                    } else {
                        quantityText.setText(ingredient.getValue());
                    }
                }
                numIngredients = numIngredients + 1;
            }

            holder.nameText.setActivated(false);
            if (position == mExpandedPosition){
                holder.nameText.setActivated(true);
            }

            // gets called every scroll todo: change this to one time function call
            if (holder.recipeDetails != null) {
                View details = holder.recipeDetails;
                if(holder.nameText.isActivated()){
                    bindUnfoldedState(details, holder.getLayoutPosition());
                }else {
                    details.setVisibility(View.GONE);
                }
            }
        }


    }



    @Override
    public int getItemCount() {
        if (filteredRecipeList != null) {
            return filteredRecipeList.size();
        }
        return 0;
    }

    public void refreshList(List<Recipe> recipes){
        update(recipes);
    }



    private class CocktailFilter extends Filter{

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() != 0) {
                List<Integer> recipesWithMatchingIngredients = recipesDao.getRecipeIdsByIngredient(constraint.toString());

                results.values = mRecipeList.stream()
                        .filter(r -> recipesWithMatchingIngredients.contains(r.getId()))
                        .sorted(Comparator.comparing(Recipe::getRating).reversed())
                        .collect(Collectors.toList());
            }else {
                results.values = mRecipeList;
            }
            results.count = ((List) results.values).size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredRecipeList.clear();
            if (results.values != null) {
                filteredRecipeList.addAll((List) results.values);
            }
            notifyDataSetChanged();
        }
    }

}
