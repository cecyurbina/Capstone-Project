package com.udacity.surbi.listnow;

public class ItemList {
    private String title;
    private boolean completed;

    public ItemList(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
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

}
