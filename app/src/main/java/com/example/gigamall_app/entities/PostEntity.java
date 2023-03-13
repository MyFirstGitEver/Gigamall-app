package com.example.gigamall_app.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PostEntity implements Parcelable {
    private Integer id;
    private String title;
    private int likeCnt;

    private BrandEntity brand;
    private List<MediaEntity> images;

    private boolean isLiked = false;

    public PostEntity() {
    }

    protected PostEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();
        likeCnt = in.readInt();
        brand = in.readParcelable(BrandEntity.class.getClassLoader());
        images = in.createTypedArrayList(MediaEntity.CREATOR);
        isLiked = in.readByte() != 0;
    }

    public static final Creator<PostEntity> CREATOR = new Creator<PostEntity>() {
        @Override
        public PostEntity createFromParcel(Parcel in) {
            return new PostEntity(in);
        }

        @Override
        public PostEntity[] newArray(int size) {
            return new PostEntity[size];
        }
    };

    public Integer getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public int getLikeCnt() {
        return likeCnt;
    }

    public List<MediaEntity> getImages() {
        return images;
    }

    public BrandEntity getBrand() {
        return brand;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(title);
        parcel.writeInt(likeCnt);
        parcel.writeParcelable(brand, i);
        parcel.writeTypedList(images);
        parcel.writeByte((byte) (isLiked ? 1 : 0));
    }
}