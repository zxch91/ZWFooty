package com.example.cw;

public class Comment {
    private String author;
    private String content;
    private int articleId;

    public Comment() {
        // Default constructor required for calls to DataSnapshot
    }

    public Comment(String author, String content, int articleId) {
        this.author = author;
        this.content = content;
        this.articleId = articleId;
    }

    // Getter and setter methods
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }
}
