package com.example.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.DetailActivity;
import com.example.bakingapp.R;
import com.example.bakingapp.model.APIRecipe;
import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    Context context;
    private List<APIRecipe> dataList = new ArrayList<>();

    private final ItemClickListener<APIRecipe> mOnclickListener;

    public interface ItemClickListener<T>{
        void onListItemClicked(APIRecipe t);
    }

    public RecipeAdapter(Context context, ItemClickListener onClickListener){
        this.context = context;
        this.mOnclickListener = onClickListener;
    }

    public void setRecipeList(List<APIRecipe> recipeList){
        dataList = new ArrayList<>();
        dataList = recipeList;
        notifyDataSetChanged();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder{
        private View itemView;
        TextView tvRecipeName;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvRecipeName = itemView.findViewById(R.id.tv_recipe_name);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_grid_layout,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, final int position){
        final String name = dataList.get(position).getName();

        holder.tvRecipeName.setText(name);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnclickListener.onListItemClicked(dataList.get(position));
                Log.d("ADITY",dataList.get(position).getSteps()+"");

                Bundle bundle = new Bundle();

                // Using parceable
                bundle.putParcelable("RECIPE",dataList.get(position));

                //create intent
                Intent newIntent = new Intent(context, DetailActivity.class);
                newIntent.putExtras(bundle);
                context.startActivity(newIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (dataList != null)? dataList.size():0;
    }
}
