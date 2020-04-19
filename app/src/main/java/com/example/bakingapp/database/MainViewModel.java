package com.example.bakingapp.database;

import android.app.Application;


import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<FavEntry>> fav;

    public MainViewModel( Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        fav = database.favDao().loadAllTaskLive();
    }

    public LiveData<List<FavEntry>> getFav(){
        return fav;
    }
}
