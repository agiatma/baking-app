package com.example.bakingapp.interfaces;

import com.example.bakingapp.model.APIRecipe;

import java.util.List;

public interface IConnectInternet {
    void callback(List<APIRecipe> obj);
}
