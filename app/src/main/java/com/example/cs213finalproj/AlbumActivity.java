package com.example.cs213finalproj;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import model.Album;
import model.Photo;

public class AlbumActivity extends AppCompatActivity {

    RecyclerAdapter adapter;
    Button addPhoto;
    ArrayList<Uri> uri;
    GridView gridview;
    ArrayList<Photo> photos;
    ArrayList<String> photosList = new ArrayList<>();
    ArrayAdapter<String>padapter;
    ListView lv_pList;
    int position;
    boolean dup = false;
    ArrayList<Album> albums;
    Album selectAlbum;
    ImageView display;
    Photo shownPhoto;
    Button movePhoto;
    Button deletePhoto;
    Photo currentPhoto;
    EditText move_text;
    Button openPhoto;
    ArrayAdapter<String> adp;
    int selected_idx;
    int delPosition;
    private static final int Read_Permission = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_activity);
        loadData();

        uri = new ArrayList<>();

        gridview = (GridView) findViewById(R.id.gridView);
        movePhoto = findViewById(R.id.movePhoto);
        addPhoto = findViewById(R.id.addPhoto);
        deletePhoto = findViewById(R.id.deletePhoto);
        display = findViewById(R.id.display);
        move_text = findViewById(R.id.move_text);
        photos = selectAlbum.getPhoto();
        adapter = new RecyclerAdapter(this, uri);
        gridview.setAdapter(adapter);
        openPhoto = findViewById(R.id.openPhoto);
        adp= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,photosList);

        Toast.makeText(getApplicationContext(),
                selectAlbum.getName(), Toast.LENGTH_LONG)
                .show();
        if(ContextCompat.checkSelfPermission(AlbumActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AlbumActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);
        }

        for(int i = 0; i < selectAlbum.getPhoto().size();i++){
            uri.add(Uri.parse(selectAlbum.getPhoto().get(i).getFilePath()));
            adapter.notifyDataSetChanged();
        }

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                saveData();
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                String p = uri.get(position).toString();
                for(int m = 0; m < photos.size();m++){
                    if(photos.get(m).getFilePath().equals(p)){
                        shownPhoto = photos.get(m);
                        selected_idx=i;
                        saveData();
                    }
                }

                display.setImageURI(uri.get(position));
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            if(data.getClipData() != null) {
                int x = data.getClipData().getItemCount();
                Calendar date = Calendar.getInstance();
                for (int i = 0; i < x; i++) {
                    if (!uri.contains((data.getClipData()).getItemAt(i).getUri())) {
                        uri.add((data.getClipData()).getItemAt(i).getUri());
                        String name = data.getClipData().toString();
                    }
                }
            }
            else if(data.getData() != null) {
                Uri imgUri = data.getData();
                if (!uri.contains(imgUri)) {
                    uri.add(imgUri);
                    selectAlbum.addPhoto(new Photo("", imgUri.toString()));
                }
                Toast.makeText(getApplicationContext(), "Duplicate Photo", Toast.LENGTH_SHORT).show();
            }
        }

        adapter.notifyDataSetChanged();
        saveData();
    }

    private void saveData() {
        for(int i = 0; i < albums.size(); i++){
            if(albums.get(i).albumName.equals(selectAlbum.albumName)){
                Log.d("Updated Album", albums.get(i).albumName);
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

        selected_idx  = sharedPreferences.getInt("selectedIndex", 0);
    }

    public Photo getDisplayedPhoto(){
        String p = uri.get(position).toString();
        for(int m = 0; m < photos.size();m++){
            if(photos.get(m).getFilePath().equals(p)){
                shownPhoto = photos.get(m);
                Log.d("check here", photos.get(m).getFilePath());
            }
        }
        return null;
    }

    public void DeletePhoto(View view) {
        uri.remove((Uri.parse(selectAlbum.getPhoto().get(position).getFilePath())));

        if(photos.get(position).getFilePath().equals(shownPhoto.getFilePath())){
            photos.remove(shownPhoto);

        }

        display.setImageURI(null);

        adapter.notifyDataSetChanged();
        saveData();
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

    public void MovePhoto(View view) {
        String m = move_text.getText().toString();
        currentPhoto = photos.get(position);

        for(Album album: albums){
            if(album.getAlbum().equals(m)){
                album.addPhoto(currentPhoto);

                if(photos.get(position).getFilePath().equals(shownPhoto.getFilePath())){
                    photos.remove(shownPhoto);
                    photosList.remove(shownPhoto);
                    uri.remove(Uri.parse(shownPhoto.getFilePath()));

                    adp.notifyDataSetChanged();
                }
            }
        }

        adapter.notifyDataSetChanged();
        saveData();
    }

    public void OpenPhoto(View view) {
        shownPhoto=selectAlbum.getPhoto().get(position);
        saveData();
        Intent myIntent = new Intent(AlbumActivity.this, PhotoActivity.class);
        startActivity(myIntent);
    }
}
