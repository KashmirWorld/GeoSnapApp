package org.kashmirworldfoundation.WildlifeGeoSnap.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.reflect.TypeToken;
import com.google.firebase.Timestamp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.kashmirworldfoundation.WildlifeGeoSnap.firebase.types.WildlifeSighting;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Utils {

    public interface LambdaInterface{ public void run();}

    private static Utils instance = null;

    public static Utils getInstance() {
        if(instance == null){
            instance = new Utils();
        }
        return instance;
    }

    public boolean getAgreement(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("TOS", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("tos", true);

    }
    public void setAgreement(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("TOS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean("tos",false);
        editor.apply();
    }

    public void saveSighting(WildlifeSighting wildlifeSighting, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<WildlifeSighting> list= getSighting(context);
        list.add(wildlifeSighting);
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new DataTypeAdapter()).create();
        String json =gson.toJson(list);

        editor.putString("wildlifeSighting",json);
        editor.apply();

    }
    public ArrayList<WildlifeSighting> getSighting(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new DataTypeAdapter()).create();
        String json =sharedPreferences.getString("wildlifeSighting",null);
        if (json==null){
            return new ArrayList<>();
        }
        else{


            Type type = new TypeToken<ArrayList<WildlifeSighting>>() {}.getType();
            return gson.fromJson(json,type);

        }

    }
}
