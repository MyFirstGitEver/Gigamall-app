package com.example.gigamall_app.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductEntity implements Parcelable {
    private Integer id;

    private String title, type, url, description;
    private float price;
    private int sold, star;

    protected ProductEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();
        type = in.readString();
        url = in.readString();
        description = in.readString();
        price = in.readFloat();
        sold = in.readInt();
        star = in.readInt();
    }

    public static final Creator<ProductEntity> CREATOR = new Creator<ProductEntity>() {
        @Override
        public ProductEntity createFromParcel(Parcel in) {
            return new ProductEntity(in);
        }

        @Override
        public ProductEntity[] newArray(int size) {
            return new ProductEntity[size];
        }
    };

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public int getSold() {
        return sold;
    }
    public void setSold(int sold) {
        this.sold = sold;
    }
    public int getStar() {
        return star;
    }
    public void setStar(int star) {
        this.star = star;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public ProductEntity(String title, String type, String url, String description, float price, int sold, int star) {
        this.title = title;
        this.type = type;
        this.url = url;
        this.description = description;
        this.price = price;
        this.sold = sold;
        this.star = star;
    }

    public ProductEntity() {
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
        parcel.writeString(type);
        parcel.writeString(url);
        parcel.writeString(description);
        parcel.writeFloat(price);
        parcel.writeInt(sold);
        parcel.writeInt(star);
    }

    @Override
    public String toString() {
        return title;
    }
}