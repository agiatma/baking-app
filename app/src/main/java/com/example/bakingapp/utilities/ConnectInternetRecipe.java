package com.example.bakingapp.utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bakingapp.interfaces.IConnectInternet;
import com.example.bakingapp.model.APIRecipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConnectInternetRecipe extends AsyncTask<URL, Integer, List<APIRecipe>> {

    public static final String TAG = "ConnectInternetRecipe";
    private IConnectInternet callbackHandler = null;

    public ConnectInternetRecipe(IConnectInternet callback){
        this.callbackHandler = callback;
    }

    @Override
    protected List<APIRecipe> doInBackground(URL... urls) {
        String tmpResult ="";
        try {
            tmpResult = NetworkUtils.getResponseFromHttpUrl(urls[0]);
        }catch (IOException e){
            Log.e(TAG,e.getMessage());
        }
        Log.d("TEST", tmpResult);

  //      Gson tmpGSON = new GsonBuilder().create();
//        APIRecipe tmpModel = tmpGSON.fromJson(tmpResult,APIRecipe.class);

        Type listType = new TypeToken<ArrayList<APIRecipe>>(){}.getType();
        List<APIRecipe> list = new Gson().fromJson(tmpResult,listType);

        return list;
    }

    @Override
    protected void onPostExecute(List<APIRecipe> s){
        super.onPostExecute(s);

        if(callbackHandler !=null){
            callbackHandler.callback(s);
        }
    }
}
