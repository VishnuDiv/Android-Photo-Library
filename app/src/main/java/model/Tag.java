package model;

import java.io.Serializable;

public class Tag implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7489694932761931203L;



    public String tagValue;
    public  String tagName;

    public Tag (String value, String name) {
        this.tagValue=value;
        this.tagName = name;
    }

    public String getTagValue() {
        return tagValue;
    }

    public String getTagName() {
        return tagName;
    }

    public String setTagName(String name) {
        this.tagName=name;
        return tagName;
    }

    public boolean equals(Tag second) {
        if(tagName.equals(second.tagName) && tagValue.equals(second.tagValue)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return this.tagName + " - " + tagValue;
    }

}
