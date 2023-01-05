package model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

//import javafx.scene.image.WritableImage;

public class Photo implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = -32432423423434324L;
    public String photoName;
    private String photoCaption;
    private Calendar photoDate;
    ArrayList <Tag> photoTags;
    //private Image image;
//private String photo;
    public String filePath;



    ArrayList <Tag> tags;
    public Photo(String name, String filePath) {
        this.photoTags = new ArrayList<Tag>();
        this.photoName = name;
//	this.photoTags= new ArrayList<Tag>();
        //this.photoCaption = photoCaption;
        //this.photo=photo;

      //  this.photoDate = date;
//        this.photoDate.set(Calendar.MILLISECOND, 0);
        this.filePath = filePath;
        this.tags = new ArrayList<Tag>();
        //this.photoTags = new ArrayList<Tag>();
    }



    public String getPhoto() {
        return photoName;
    }

    public void setPhoto(String photoName) {
        this.photoName =photoName;
    }
    public String getCaption() {
        return photoCaption;
    }

    public String setCaption(String caption) {
        this.photoCaption = caption;
        return photoCaption;
    }

    public Calendar getDate() {
        return photoDate;
    }

    public ArrayList <Tag> getTag () {
        //System.out.println(photoTags);
        if(this.tags==null) {
            tags = new ArrayList<Tag>();
        }
        return tags;
    }

    public void addTag (Tag tag) {
        //System.out.println(photoTags);
        tags.add(tag);
    }
    public String toString() {
        return this.photoName;
    }

    public String printPhotoTags() {
        ArrayList<Tag> tags = this.getTag();

        for(Tag s : tags)
            return s.getTagName()+ "|" + s.getTagValue() + " ";

        return "";
    }






    public String getFilePath() {
        return this.filePath;
    }

    public String setFilePath(String fp) {
        this.filePath = fp;
        return filePath;
    }

    public boolean equals(Photo second) {
        if(photoName.equals(second.photoName)) {
            return true;
        }
        return false;
    }

    public boolean tagChecker(Tag second) {
        for(Tag t: tags) {
            if(t.tagName.trim().equals(second.tagName.trim()) && t.tagValue.trim().equals(second.tagValue.trim())){
                return true;
            }
        }
        return false;
    }

}
