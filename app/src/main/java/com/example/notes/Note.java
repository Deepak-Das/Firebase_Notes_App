package com.example.notes;

import com.google.firebase.Timestamp;

public class Note {

    private String title,description,user_id;
    private Boolean complete;
    private Timestamp oncreate;


    public Note(){
        //firebase required this constructor
    }

    public Note(String title, String description, String user_id, Boolean complete, Timestamp oncreate) {
        this.title = title;
        this.description = description;
        this.user_id = user_id;
        this.complete = complete;
        this.oncreate = oncreate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public Timestamp getOncreate() {
        return oncreate;
    }

    public void setOncreate(Timestamp oncreate) {
        this.oncreate = oncreate;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", user_id='" + user_id + '\'' +
                ", complete=" + complete +
                ", oncreate=" + oncreate +
                '}';
    }
}
