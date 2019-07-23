package com.mmuhamadamirzaidi.qwisapp.Model;

public class Category {

    private String Description, IconImage, Image, Name;

    public Category() {
    }

    public Category(String description, String iconImage, String image, String name) {
        Description = description;
        IconImage = iconImage;
        Image = image;
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getIconImage() {
        return IconImage;
    }

    public void setIconImage(String iconImage) {
        IconImage = iconImage;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}