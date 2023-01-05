package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class User implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String username;

    //public ArrayList<Album> albums = new ArrayList<Album>();
//	private static final long serialVersionUID;

    ArrayList <Album> userAlbums;

    public User(String name) {
        this.username = name;
        this.userAlbums= new ArrayList<Album>();
    }


    public String getUsername() {
        return username;
    }

//	public void addAlbum(Album a) {
//		albums.add(a);
//	}


    public ArrayList<Album> getAlbums () {
        return userAlbums;
    }



    public boolean checker(Album second) {
        for(Album album: userAlbums) {
            if(album.albumName.trim().equals(second.albumName.trim())){


                return true;
            }
        }
        return false;
    }

    public String toString() {
        return this.username;
    }







}
