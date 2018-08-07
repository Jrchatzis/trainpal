package com.example.john.travelinfo;


public class ItemData {
    private final String text;
    private final Integer imageId;

    //Constructor of text and images used for the navigation states
    public ItemData(String text, Integer imageId) {
        this.text = text;
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public Integer getImageId() {
        return imageId;
    }
}
