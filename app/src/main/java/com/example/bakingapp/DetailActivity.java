package com.example.bakingapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakingapp.adapter.StepAdapter;
import com.example.bakingapp.database.AppDatabase;
import com.example.bakingapp.database.FavEntry;
import com.example.bakingapp.model.APIRecipe;
import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Step;
import com.example.bakingapp.utilities.AppExecutors;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;

import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DetailActivity extends AppCompatActivity implements StepAdapter.ItemClickListener {



    TextView tvName;
    TextView tvServings;
    TextView tvIngredients;
    List<Ingredient> ingredients;
    List<Step> steps;
    Menu favMenu;

    boolean isFavourite = false;
    Integer servings;
    String recipeName;
    Integer id = 0;

    StepAdapter mAdapter;
    RecyclerView mRecyclerView;

    PlayerView playerView;
    SimpleExoPlayer player;

    private AppDatabase favDB;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvName = findViewById(R.id.tv_name);
        tvServings = findViewById(R.id.tv_servings);
        tvIngredients = findViewById(R.id.tv_ingredients);

        //Get data from bundle intent
        APIRecipe recipeData;
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        recipeData = bundle.getParcelable("RECIPE");
        assert recipeData != null;
        id = recipeData.getId();
        //Set name of the recipe
        recipeName = recipeData.getName();
        tvName.setText(recipeName);
        //Set servings
        servings = recipeData.getServings();
        tvServings.setText("Number of Servings : "+servings );
        Log.d("GBU", recipeData.getServings()+"");

        //Set Ingredients
        ingredients = recipeData.getIngredients();
        tvIngredients.setText(getIngredients(ingredients));

        //RV Steps
        mRecyclerView = findViewById(R.id.rv_steps);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new StepAdapter(this,this);
        //Get Steps
        steps = recipeData.getSteps();
        Log.d("TYAK", steps+"");
        mAdapter.setStepList(steps);
        mRecyclerView.setAdapter(mAdapter);

        favDB = AppDatabase.getInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                FavEntry favouriteId = favDB.favDao().loadFavById(id);
                if (favouriteId != null){
                    isFavourite = true;

                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu (Menu favBtn){
        favMenu = favBtn;
        getMenuInflater().inflate(R.menu.menu_fav,favBtn);
        setIconFav(isFavourite);
        return super.onCreateOptionsMenu(favBtn);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        favDB = AppDatabase.getInstance(getApplicationContext());
        final FavEntry favEntry = new FavEntry(id,recipeName,ingredients,steps,servings);
        Log.d("SIMPAN", favEntry.toString());

        if(!isFavourite){
            Toast.makeText(DetailActivity.this, recipeName + " add to favourite", Toast.LENGTH_SHORT).show();
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    favDB.favDao().insertFav(favEntry);
                    Log.d("SIMPAN", "Data tersimpan");
                }
            });
        } else {
            Toast.makeText(DetailActivity.this, recipeName +" removed from favourite", Toast.LENGTH_SHORT).show();
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    favDB.favDao().deleteFav(favEntry);
                    Log.d("HAPUS", "Data terhapus");
                }
            });

        } setIconFav(!isFavourite);
        return true;
    }

    private  void setIconFav(Boolean isFavourite){
        MenuItem item = favMenu.getItem(0);

        if (isFavourite){
            Log.d("Paporit","garis");
            item.setIcon(R.drawable.ic_favorite_black);
            this.isFavourite=true;
        }else {
            Log.d("Paporit","hitam");
            item.setIcon(R.drawable.ic_favorite);
            this.isFavourite=false;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getIngredients(List<Ingredient> ingredients){
        String plus;
        Set<String> gabungan = new LinkedHashSet<>();
        for(Ingredient ingredient : ingredients){
            String komposisi;
            String bahan = "- "+ingredient.getIngredient();
            String jumlah = ingredient.getQuantity().toString();
            String takaran = ingredient.getMeasure();
            Set<String> join = new LinkedHashSet<>();
            join.add(bahan);
            join.add(jumlah);
            join.add(takaran);
            komposisi = String.join(" ",join);
            gabungan.add(komposisi);
        }
        plus = String.join("\n\n",gabungan);

        return plus;
    }

    @Override
    public void onListItemClicked(Step t) {
        Log.d("GILSSS", t.getId()+"");
        //Toast.makeText(this, t.getShortDescription(), Toast.LENGTH_SHORT).show();
        final Dialog dialog = new Dialog(DetailActivity.this);
        dialog.setContentView(R.layout.steps_dialog);
        dialog.setTitle("Steps");
        TextView text = dialog.findViewById(R.id.tv_steps_desc);
        Button closeBtn = dialog.findViewById(R.id.btn_close);
        playerView = dialog.findViewById(R.id.video_view);
        final String url = t.getVideoURL();
        Log.d("GILA", url);
        //playerView = findViewById(R.id.video_view);
        if(!url.equals("")){
            initializePlayer(url);
            text.setText(t.getDescription());
            Log.d("GILA",t.getVideoURL());
            dialog.show();
        }
        else {
            text.setText(t.getDescription());
            Log.d("GILA", t.getVideoURL());
            dialog.show();
        }

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(!url.equals("")) {
                    player.release();
                }
            }
        });
    }

    private void initializePlayer(String videoUrl) {

        try {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(DetailActivity.this),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            Log.d("IBUK", player.toString());

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    DetailActivity.this, Util.getUserAgent(DetailActivity.this, "RecyclerView VideoPlayer"));
            if (videoUrl != null) {
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(videoUrl));
                playerView.setPlayer(player);
                player.prepare(videoSource);
                player.setPlayWhenReady(true);

                Log.d("IBUK", player.toString());
            }
        }catch (Exception e){
            Log.d("GILA", e.toString());
        }
    }

}
