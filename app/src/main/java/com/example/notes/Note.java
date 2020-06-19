package com.example.notes;

import java.util.Date;

public class Note {

    private String title,description,user_id;
    private Boolean complete;
    private Date timestamp;


    public Note(){
        //firebase required this constructor
    }

    public Note(String title, String description, String user_id, Boolean complete, Date timestamp) {
        this.title = title;
        this.description = description;
        this.user_id = user_id;
        this.complete = complete;
        this.timestamp = timestamp;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", user_id='" + user_id + '\'' +
                ", complete=" + complete +
                ", timestamp=" + timestamp +
                '}';
    }
}
