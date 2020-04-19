package com.example.bakingapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.adapter.RecipeAdapter;
import com.example.bakingapp.database.AppDatabase;
import com.example.bakingapp.database.FavEntry;
import com.example.bakingapp.database.MainViewModel;
import com.example.bakingapp.model.APIRecipe;
import com.example.bakingapp.utilities.AppExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentFavouriteRecipe extends Fragment implements RecipeAdapter.ItemClickListener {

    private RecipeAdapter mAdapter;
    private ArrayList<APIRecipe> favourite = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_favourite_recipe, container,
                false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.rv_favRecipe);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),
                2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RecipeAdapter(getActivity(), this);
        //setupViewMode();
        recyclerView.setAdapter(mAdapter);
        Log.d("ORAONO", favourite+"");
        mAdapter.setRecipeList(favourite);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewMode();

    }

    private void setupViewMode(){
        MainViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainViewModel.class);
        viewModel.getFav().observe(getViewLifecycleOwner(), new Observer<List<FavEntry>>() {
            @Override
            public void onChanged(List<FavEntry> favEntries) {
                Log.d("Perbarui","updatinglist of tasks from LiveData in ViewModel");
                final ArrayList<APIRecipe> favourite = new ArrayList<>();
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        //Load All Database about favorite recipe
                        List<FavEntry> favouriteEntries = AppDatabase.getInstance(Objects.requireNonNull(getActivity()).getApplicationContext()).favDao()
                                .loadAllTask();

                        for (FavEntry favEntry : favouriteEntries) {
                            APIRecipe a = new APIRecipe();
                            a.setId(favEntry.getId());
                            a.setName(favEntry.getName());
                            a.setIngredients(favEntry.getIngredients());
                            a.setSteps(favEntry.getSteps());
                            a.setServings(favEntry.getServings());
                            favourite.add(a);
                        }
                        Log.d("WIRATMA2",favourite+"");
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setRecipeList(favourite);

                            }
                        });

                    }
                });
                Log.d("WIRATMA",favourite+"");

            }
        });
    }

    @Override
    public void onListItemClicked(APIRecipe t) {
        Toast.makeText(getActivity(), t.getName(), Toast.LENGTH_SHORT).show();
    }
}
