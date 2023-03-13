package com.example.gigamall_app.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class BrandEntity implements Parcelable {
    private Integer id;

    private String name, logoUrl;

    protected BrandEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        logoUrl = in.readString();
    }

    public static final Creator<BrandEntity> CREATOR = new Creator<BrandEntity>() {
        @Override
        public BrandEntity createFromParcel(Parcel in) {
            return new BrandEntity(in);
        }

        @Override
        public BrandEntity[] newArray(int size) {
            return new BrandEntity[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public BrandEntity(String name, String url) {
        this.name = name;
        this.logoUrl = url;
    }

    public BrandEntity() {
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
        parcel.writeString(name);
        parcel.writeString(logoUrl);
    }
}