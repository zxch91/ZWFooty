package com.example.cw;

import java.io.Serializable;

public class newsContent implements Serializable {
    private String name;
    private String headline;
    private String article;
    private int image;
    private int id;

    public newsContent() {
        // Default constructor required for calls to DataSnapshot.getValue(newsContent.class)
    }

    public newsContent(String name, String headline, String article, int image, int id) {
        this.name = name;
        this.headline = headline;
        this.article = article;
        this.image = image;
        this.id = id;
    }

    // Getter and setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
