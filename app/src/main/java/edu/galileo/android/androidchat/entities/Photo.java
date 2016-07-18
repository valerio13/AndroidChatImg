package edu.galileo.android.androidchat.entities;

//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.google.firebase.database.IgnoreExtraProperties;


public class Photo {
    //@JsonIgnore
    private String id;

    //@JsonIgnore
    private boolean publishedByMe;

    private String url;
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPublishedByMe() {
        return publishedByMe;
    }

    public void setPublishedByMe(boolean publishedByMe) {
        this.publishedByMe = publishedByMe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
