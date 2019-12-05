package com.example.firebaseauthgoogle;

public class ImageUploadInfo {

    public String imageName;

    public String imageURL;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String name, String url) {

        this.imageName = name;
        this.imageURL = url;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageURL() {
        System.out.println("print url"+ getImageURL());
        return imageURL;
    }
}
