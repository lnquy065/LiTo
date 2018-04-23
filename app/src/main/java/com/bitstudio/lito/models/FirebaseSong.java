package com.bitstudio.lito.models;

/**
 * Created by LN Quy on 14/04/2018.
 */

public class FirebaseSong {
    private String id;
    private String name;
    private String url;
    private String img;
    private String artist;

    public FirebaseSong() {
    }

    public FirebaseSong(String id, String name,String artist, String url, String img) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.img = img;
        this.artist = artist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
