package com.example.bakingapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.adapter.RecipeAdapter;
import com.example.bakingapp.interfaces.IConnectInternet;
import com.example.bakingapp.model.APIRecipe;
import com.example.bakingapp.utilities.ConnectInternetRecipe;
import com.example.bakingapp.utilities.NetworkUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FragmentRecipeList extends Fragment implements RecipeAdapter.ItemClickListener, IConnectInternet {

    private URL urlRecipe;
    private RecipeAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View tmpView = inflater.inflate(R.layout.fragment_recipe_list, container,false);

        try{
            urlRecipe = NetworkUtils.buildUrl();
        }catch (MalformedURLException e){
            String textToShow = "URL GAGAL BUILD";
            Toast.makeText(getActivity(),textToShow,Toast.LENGTH_SHORT).show();
        }
        if(urlRecipe != null){
            new ConnectInternetRecipe(this).execute(urlRecipe);
        }
        try{
            NetworkUtils.buildUrl();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        RecyclerView recyclerView = tmpView.findViewById(R.id.rv_recipe);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RecipeAdapter(getActivity(),this);
        recyclerView.setAdapter(mAdapter);
        return tmpView;
    }

    @Override
    public void callback(List<APIRecipe> obj) {
        Log.d("ADITS", obj.get(0).getIngredients()+"");
        mAdapter.setRecipeList(obj);
        Log.d("ADIT", obj +"");
    }

    @Override
    public void onListItemClicked(APIRecipe t) {
        Toast.makeText(getActivity(), t.getName(), Toast.LENGTH_SHORT).show();
    }
}
