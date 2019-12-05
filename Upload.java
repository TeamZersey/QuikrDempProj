package com.example.firebaseauthgoogle;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Upload{

    public String name;
    public String url;
    public String title;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String url) {
        this.name = name;
        this.url= url;
      /*  this.title = title;*/
    }

    public Upload(String title) {
       this.title = title;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
}
