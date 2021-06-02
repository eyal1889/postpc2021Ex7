package com.example.ex7;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.StructuredQuery;

public class OrderApp extends Application {
    private static OrderApp instance = null;
    private FirebaseFirestore firestore;
    SharedPreferences sp =null;
    public SharedPreferences getSp(){ return sp; }

    public FirebaseFirestore getFireBase(){
        return firestore;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FirebaseApp.initializeApp(this);
        firestore = FirebaseFirestore.getInstance();;
        sp= this.getSharedPreferences("local_db_todo", MODE_PRIVATE);
    }
    public static OrderApp getInstance(){
        return instance;
    }

    public String loadFromSp(){
        String storedJson = sp.getString("id","nope");
        if (storedJson.equals("nope")) return "nope";
        Gson gson = new Gson();
        return gson.fromJson(storedJson,String.class);
    }

    public void saveInSp(String id){
        Gson gson = new Gson();
        String objJson = gson.toJson(id);
        SharedPreferences.Editor editor= sp.edit();
        editor.putString("id",objJson);
        editor.apply();
    }
    public void clearSp(){
        Gson gson = new Gson();
        SharedPreferences.Editor editor= sp.edit();
        editor.clear();
        editor.apply();
    }
}
