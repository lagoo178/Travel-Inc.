package com.travel.travelinc.post;

public class PostingSupport {
    private String Type;
    private String Area;
    private String Name;
    private String Description;
    private String Imageuri;
    private String Key;
    private String UserName;

    public PostingSupport() {

    }

    public PostingSupport(String type, String area, String name, String description, String imageuri, String key, String userName) {
        Type = type;
        Area = area;
        Name = name;
        Description = description;
        Imageuri = imageuri;
        Key = key;
        UserName = userName;
    }

    public String getType() {
        return Type;
    }

    public String getArea() {
        return Area;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getImageuri() {
        return Imageuri;
    }

    public String getKey() {
        return Key;
    }

    public String getUserName() {
        return UserName;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setArea(String area) {
        Area = area;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setImageuri(String imageuri) {
        Imageuri = imageuri;
    }

    public void setKey(String key) {
        Key = key;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}