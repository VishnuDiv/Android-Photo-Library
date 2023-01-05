package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Album implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = -5286076993299808160L;
    public String albumName;
    ArrayList<Photo> albumPhotos;

    public Album(String name) {
        this.albumName = name;
        this.albumPhotos = new ArrayList<Photo>();
    }

    public String getAlbum () {
        return albumName;
    }

    public String getName() {
        return this.albumName;
    }

    public void  setName(String albumName) {
        this.albumName = albumName;

    }
    public String setAlbum(String name) {
        this.albumName= name;
        return albumName;
    }


    public ArrayList<Photo> getPhoto() {
        return albumPhotos;
    }


    public void addPhoto(Photo photo) {
        albumPhotos.add(photo);
    }



    public String toString() {
        return this.albumName;
    }

    public boolean photoChecker(Photo second) {
        for(Photo photo: albumPhotos) {
            if(photo.photoName.trim().equals(second.photoName.trim())){


                return true;
            }
        }
        return false;
    }

    public boolean fPChecker(Photo second) {
        for(Photo photo: albumPhotos) {
            if(photo.filePath.trim().equals(second.filePath.trim())){


                return true;
            }
        }
        return false;
    }

}
