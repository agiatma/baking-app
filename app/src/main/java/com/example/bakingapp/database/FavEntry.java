package com.example.bakingapp.database;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

@Entity(tableName = "recipe")
public class FavEntry {

    @PrimaryKey()
    private Integer id;
    private String name;
    @TypeConverters(IngredientListToGsonConverter.class)
    private List<Ingredient> ingredients;

    @TypeConverters(StepListToGsonConverter.class)
    private List<Step> steps;
    private Integer servings;

    public FavEntry(Integer id, String name, List<Ingredient> ingredients, List<Step> steps, int servings){
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
    }

    public static class IngredientListToGsonConverter{
        @TypeConverter
        public static List<Ingredient> restoreIngredient(String listOfString){
            return new Gson().fromJson(listOfString, new TypeToken<List<Ingredient>>() {}.getType());
        }

        @TypeConverter
        public static String saveListOfIngredient(List<Ingredient> listOfString){
            return new Gson().toJson(listOfString);
        }
    }

    public static class StepListToGsonConverter{
        @TypeConverter
        public static List<Step> restoreStep(String listOfString){
            return new Gson().fromJson(listOfString, new TypeToken<List<Step>>() {}.getType());
        }

        @TypeConverter
        public static String saveListOfStep(List<Step> listOfString){
            return new Gson().toJson(listOfString);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }
}
