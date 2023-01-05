package com.example.cs213finalproj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import model.Album;
import model.Photo;
import model.Tag;


public class SearchActivity extends Activity {
    ArrayList<Album> albums;
    Album selectAlbum;
    Photo shownPhoto;
    ArrayList<Photo> addThisPhoto ;
    ArrayList<Tag> tags;
    ArrayList<String> tagList;
   RecyclerAdapter adapter;
    int listViewPosition;
    AutoCompleteTextView location_autoc;
    AutoCompleteTextView person_autoc;
    Button s_button;
    ImageView image_select;
    ArrayList<String> albNames;
    ArrayList<String> photos;
    GridView search_g;
    int selected_idx;
    Photo current_p;
    ImageView i_search;
    ArrayList<Uri> searchResult;
    ArrayList<String> p_auto;
    ArrayList<String> l_auto;
    ArrayAdapter<String>p_adapt;
    ArrayAdapter<String> l_adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        loadData();
        populate();
        location_autoc = findViewById(R.id.location_autoc);
        search_g=findViewById(R.id.grid_search);
        person_autoc = findViewById(R.id.person_autoc);
        i_search = findViewById(R.id.i_search);
        searchResult = new ArrayList<>();
        adapter = new RecyclerAdapter(this, searchResult);
        search_g.setAdapter(adapter);
        s_button = findViewById(R.id.s_button);
        albNames = new ArrayList<>();
        addThisPhoto = new ArrayList<>();
        photos = new ArrayList<>();
        p_adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,p_auto);
        l_adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,l_auto);
        location_autoc.setAdapter(l_adapt);
        person_autoc.setAdapter(p_adapt);

        search_g.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             i_search.setImageURI(searchResult.get(i));
            }

        });
    }

    public void populate(){
        p_auto = new ArrayList<>();
        l_auto = new ArrayList<>();

        for(Album searchAlb: albums) {
            for (Photo searchP : searchAlb.getPhoto()) {
                for (Tag searchT : searchP.getTag()) {
                    String tagsVal = searchT.getTagValue();
                    String tagName = searchT.getTagName();

                    if(tagName.equals("person")) {
                        if (!p_auto.contains(tagsVal)) {
                            p_auto.add(tagsVal);
                            Log.d("999",tagsVal);
                        }
                    }

                    if(tagName.equals("location")) {
                        if (!l_auto.contains(tagsVal)) {
                            l_auto.add(tagsVal);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        saveData();
        loadData();

        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void searchTags(View view){
        loadData();
        String search_person = person_autoc.getText().toString();
        String search_location = location_autoc.getText().toString();
        Log.d("444",search_person);

        if(search_person!=null || search_location!=null){
            addThisPhoto.clear();

            for(Album searchAlb: albums){
                for(Photo searchP: searchAlb.getPhoto()){
                    ArrayList<String> l= new ArrayList<>();

                    ArrayList<Tag> l_r = searchP.getTag();
                    ArrayList<String> p= new ArrayList<>();
                    for(Tag m : searchP.getTag()){

                    }
                    for(Tag searchT: searchP.getTag()){
                        String tagsVal = searchT.getTagValue();

                        if(searchT.getTagName().equals("person")){
                            p.add(searchT.getTagValue());
                        }
                        else if(searchT.getTagName().equals("location")){
                            l.add(searchT.getTagValue());
                        }

                        for(int i = 0; i < p.size();i++){
                            for(int j = 0; j < l.size();j++){
                                if(search_person.equalsIgnoreCase(p.get(i)) && search_location.equalsIgnoreCase(l.get(j))){
                                    addThisPhoto.add(searchP);
                                }
                            }
                        }

                        if(tagsVal.equalsIgnoreCase(search_person) && search_location.isEmpty()){
                            addThisPhoto.add(searchP);
                        }else if(tagsVal.equalsIgnoreCase(search_location) && search_person.isEmpty()){
                            addThisPhoto.add(searchP);
                        }

                    }

                }
            }

            searchResult.clear();
            for(int i = 0; i < addThisPhoto.size();i++){
               searchResult.add(Uri.parse(addThisPhoto.get(i).getFilePath()));
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void saveData() {
        for(int i = 0; i < albums.size(); i++){
            if(albums.get(i).albumName.equals(selectAlbum.albumName)){
                albums.set(i, selectAlbum);
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(albums);
        editor.putString("album list", json);

        String json2 = gson.toJson(selectAlbum);
        editor.putString("selectedAlbum", json2);

        String json3 = gson.toJson(shownPhoto);
        editor.putString("selectedPhoto", json3);

        editor.putInt("selectedIndex", selected_idx);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("album list", null);
        Type type = new TypeToken<ArrayList<Album>>() {}.getType();
        albums = gson.fromJson(json, type);

        if (albums == null) {
            albums = new ArrayList<>();
        }

        String json2 = sharedPreferences.getString("selectedAlbum", "");
        selectAlbum = gson.fromJson(json2, Album.class);

        String json3 = sharedPreferences.getString("selectedPhoto", "");
        shownPhoto = gson.fromJson(json3, Photo.class);

        if (shownPhoto == null) {
            shownPhoto = new Photo("","");
        }

        selected_idx  = sharedPreferences.getInt("selectedIndex", 0);
    }
}
