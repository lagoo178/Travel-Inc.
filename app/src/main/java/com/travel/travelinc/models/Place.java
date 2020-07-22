package com.travel.travelinc.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {
    private int id;
    private int image;
    private String title;
    private String desc;
    private float rate;
    private int imageMenu;
    private String details;

    public Place(int id, int image, String title, String desc, float rate, int imageMenu, String details) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.rate = rate;
        this.imageMenu = imageMenu;
        this.details = details;
    }

    protected Place(Parcel in) {
        id = in.readInt();
        image = in.readInt();
        title = in.readString();
        desc = in.readString();
        rate = in.readFloat();
        imageMenu = in.readInt();
        details = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(image);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeFloat(rate);
        dest.writeInt(imageMenu);
        dest.writeString(details);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getImageMenu() {
        return imageMenu;
    }

    public void setImageMenu(int imageMenu) {
        this.imageMenu = imageMenu;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
