package com.mts.cocktailbuilder.mybar;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.mts.cocktailbuilder.R;
import com.mts.cocktailbuilder.configuration.CocktailBuilderDatabase;
import com.mts.cocktailbuilder.fragments.MakeListFragment;

public class MyBarAdapter extends RecyclerView.Adapter<MyBarAdapter.MyBarViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    AdapterLongClickHandler mLongClickHandler;

    public interface AdapterLongClickHandler{
        void onLongClick(int id);
    }


    public MyBarAdapter(Context context, Cursor cursor, AdapterLongClickHandler longClickHandler) {
        mCursor = cursor;
        mContext = context;
        mLongClickHandler = longClickHandler;
    }


    public static class MyBarViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public LinearLayout container;


        public MyBarViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.bar_item_row_text_view);
            container = itemView.findViewById(R.id.bar_item_row);
        }


}
    @NonNull
    @Override
    public MyBarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.bar_item_row, parent, false);
        return new MyBarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBarViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }
        String name = mCursor.getString(mCursor.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_BRAND))
                + " " + mCursor.getString(mCursor.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_VARIANT))
                + " " + mCursor.getString(mCursor.getColumnIndex(CocktailBuilderDatabase.DrinkEntry.COLUMN_TYPE));
        int id = mCursor.getInt(mCursor.getColumnIndex(CocktailBuilderDatabase.DrinkEntry._ID));
        holder.nameText.setText(name);
        holder.nameText.setTag(id);
        holder.nameText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mLongClickHandler.onLongClick(id);
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    public void swapCursor(Cursor newCursor){
        if (mCursor != null){
            mCursor.close();
        }
        mCursor = newCursor;

        if(newCursor != null){
            notifyDataSetChanged();
        }
    }
    public void swapCursor(){
        mCursor.close();
        mCursor = null;
    }


}
