package com.mmuhamadamirzaidi.qwisapp.Model;

public class Category {
    private String Name, Image, Description, Key, IconImage;

    public Category() {
    }

    public Category(String name, String image, String description, String key, String iconImage) {
        Name = name;
        Image = image;
        Description = description;
        Key = key;
        IconImage = iconImage;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getIconImage() {
        return IconImage;
    }

    public void setIconImage(String iconImage) {
        IconImage = iconImage;
    }
}