package com.keio.marketplace.entity;

public class Articles {
    private String name;
    private int price;
    private int stock;
    private String image;
    private long id;

    public Articles(String nom, int prix, int stock, String image) {
        this.name = nom;
        this.price = prix;
        this.stock = stock;
        this.image = image;
    }

    public Articles(String nom, int prix, int stock, String image, long id) {
        this.name = nom;
        this.price = prix;
        this.stock = stock;
        this.image = image;
        this.id = id;
    }

    public Articles() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return (int)price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Articles articles = (Articles) o;

        return id == articles.id;
    }
}
