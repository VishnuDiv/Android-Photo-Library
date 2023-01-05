package com.example.cs213finalproj;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import model.Album;
public class MainActivity extends AppCompatActivity {
    EditText create_text;
    ListView lv_albumsList;
    Button create_button;
    ArrayList<String> albList = new ArrayList<String>();
    ArrayList<Album> albums = new ArrayList<Album>();
    ArrayAdapter<String> adapt;
    Button remove_button;
    int listViewPosition;
    Album selectAlbum;
    boolean dup =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();

        selectAlbum = new Album("");

        for(int i = 0; i < albums.size();i++){
            albList.add(albums.get(i).getAlbum());
        }

        create_text = findViewById(R.id.create_text);
        lv_albumsList = findViewById(R.id.lv_albumsList);
        create_button = findViewById(R.id.create_button);
        adapt = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,albList);
        lv_albumsList.setAdapter(adapt);


        lv_albumsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {

                listViewPosition = position;
                selectAlbum = albums.get(position);
                saveData();
            }
        });
    }

    public boolean checkDup(Album second){
        Album first = new Album(create_text.getText().toString());
        second = new Album((create_text.getText().toString()));

        for(Album checkAlb: albums){
            if(first.albumName.trim().equals(second.albumName.trim())){
                return true;
            }
        }

        return false;
    }

    public void CreateAlb(View view) {
        String albName = create_text.getText().toString();
        Album temp = new Album(albName);

        Album a = new Album(albName);
        Album b = new Album((albName));

        if(adapt==null){

        }

        if(!albList.contains(albName)){
            adapt.add(albName);
            albums.add(a);
        }else{
            Toast.makeText(getApplicationContext(), "Duplicate Album", Toast.LENGTH_SHORT).show();
        }

        adapt.notifyDataSetChanged();
        saveData();
    }


    public void showInputBox(String old, int index){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Rename album");
        dialog.setContentView(R.layout.input_box);
        TextView txtMessage =(TextView) dialog.findViewById(R.id.txtmessage);
        txtMessage.setText("Update item");
        txtMessage.setTextColor(Color.parseColor("#ff2222"));
        EditText editText = (EditText) dialog.findViewById(R.id.input_text);
        editText.setText(old);
        Button bt = (Button) dialog.findViewById(R.id.btdone);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albList.set(index,editText.getText().toString());
                Album newName = new Album(editText.getText().toString());
                albums.set(index,newName);
                adapt.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
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
        if (selectAlbum == null) {
            selectAlbum = new Album("");
        }
        String json2 = sharedPreferences.getString("selectedAlbum", "");
        selectAlbum = gson.fromJson(json2, Album.class);
    }

    public void OpenAlb(View view) {
        Intent myIntent = new Intent(MainActivity.this, AlbumActivity.class);
        startActivity(myIntent);
    }

    public void RenameAlb(View view) {
        Button rename_Album = findViewById(R.id.rename_Box);
        showInputBox(albList.get(listViewPosition),listViewPosition);
    }

    public void OpenSearchPhoto(View view) {
        Intent someI = new Intent(MainActivity.this,SearchActivity.class);
        startActivity(someI);
    }

    public void DeleteAlbum(View view) {
        albums.remove(listViewPosition);
        albList.remove(listViewPosition);

        saveData();
        adapt.notifyDataSetChanged();
    }
}