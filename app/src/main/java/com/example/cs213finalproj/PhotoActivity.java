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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import model.Tag;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import model.Album;
import model.Photo;

public class PhotoActivity extends Activity {
    ArrayList<Album> albums;
    Album selectAlbum;
    Photo shownPhoto;
    ImageView shownImageView;
    ListView lv_pList;
    ArrayList<Tag> tags;
    ArrayList<String> tagList;
    ArrayAdapter<String> adapter;
    int listViewPosition;
    EditText person_text;
    EditText location_text;
    int selected_idx;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        loadData();

        shownImageView = findViewById(R.id.shownImageView);
        shownImageView.setImageURI(Uri.parse(shownPhoto.getFilePath()));
        lv_pList = findViewById(R.id.lv_pList);
        person_text = findViewById(R.id.person_text);

        initiateScreen();
        location_text = findViewById(R.id.location_text);

        lv_pList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {

                listViewPosition = position;
                saveData();
            }
        });
    }

    public void initiateScreen(){
        tags = shownPhoto.getTag();
        tagList = new ArrayList<>();
        for(int i = 0; i < tags.size();i++){
            tagList.add(shownPhoto.getTag().get(i).getTagName() + "-" + shownPhoto.getTag().get(i).getTagValue());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,tagList);
        lv_pList.setAdapter(adapter);
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


    public void addTag(View view) {
        Tag pt = new Tag(person_text.getText().toString(),"person");
        Tag lt = new Tag(location_text.getText().toString(),"location");

        Log.d("cp", person_text.getText().toString());
        Log.d("cl", location_text.getText().toString());

        if(!person_text.getText().toString().isEmpty() && location_text.getText().toString().isEmpty()){
          tags.add(pt);
          tagList.add("person - " + pt.getTagValue());
        }
        else if( person_text.getText().toString().isEmpty() && !location_text.getText().toString().isEmpty()){
          tags.add(lt);
          tagList.add("location - " + lt.getTagValue());
        }
        else if(!person_text.getText().toString().isEmpty() && !location_text.getText().toString().isEmpty()){
          tagList.add("person - " + pt.getTagValue());

          tagList.add("location - " + lt.getTagValue());
          tags.add(pt);

          tags.add(lt);
        }

        selectAlbum.getPhoto().set(selected_idx,shownPhoto);
        adapter.notifyDataSetChanged();
        saveData();
    }

    public void deleteTag(View view) {
        ArrayList<Tag> rem = shownPhoto.getTag();
        Log.d("tC1", shownPhoto.getTag().toString());
        String j = tagList.get(listViewPosition);
        Log.d("tC2", j);

        rem.remove(listViewPosition);
        tagList.remove(listViewPosition);
        selectAlbum.getPhoto().set(selected_idx,shownPhoto);
        adapter.notifyDataSetChanged();
        saveData();
    }

    @Override
    public void onBackPressed() {
        saveData();
        loadData();

        Intent myIntent = new Intent(this,AlbumActivity.class);
        startActivity(myIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void previousPhoto(View view) {
        if(selected_idx >0){
            selected_idx -=1;
            shownPhoto = selectAlbum.getPhoto().get(selected_idx);
            shownImageView.setImageURI(Uri.parse(shownPhoto.getFilePath()));
        }

        initiateScreen();
    }

    public void nextPhoto(View view) {
        if(selected_idx < selectAlbum.getPhoto().size()-1){
            selected_idx +=1;
            shownPhoto = selectAlbum.getPhoto().get(selected_idx);
            shownImageView.setImageURI(Uri.parse(shownPhoto.getFilePath()));
        }

        initiateScreen();
    }
}
