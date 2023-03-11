package com.example.gigamall_app.entities;

public class BrandEntity {
    private Integer id;

    private String name, logoUrl;

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
}