package com.udacity.surbi.listnow;

public class ItemList {
    private int id;
    private String title;
    private boolean completed;
    private boolean favorite;

    public ItemList(int id, String title, boolean completed, boolean favorite) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.favorite = favorite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

}
