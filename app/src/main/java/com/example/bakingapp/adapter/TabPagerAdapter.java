package com.example.bakingapp.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.bakingapp.fragment.FragmentFavouriteRecipe;
import com.example.bakingapp.fragment.FragmentRecipeList;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    public TabPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("TEST", "Position ke - " + position);

        switch(position){
            case 0:
                return new FragmentRecipeList();
            case 1 :
                return new FragmentFavouriteRecipe();
        }

        return new FragmentRecipeList();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return ("Recipe List");
        }
        else {
            return ("Favourite Recipe");
        }
    }
}
