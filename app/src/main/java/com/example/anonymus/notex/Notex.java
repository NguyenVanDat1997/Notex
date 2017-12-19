package com.example.anonymus.notex;

public class Notex {
    private String title,content;
    private int id;

    public Notex (){

    }
    public Notex(String title, String content, int id) {
        this.title = title;
        this.content = content;
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Notex{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", id=" + id +
                '}';
    }
}