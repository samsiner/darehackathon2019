package com.example.ulta3.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "product")
public class Product {

    @PrimaryKey
    @NonNull
    public final Integer sku;
    public int prob;
    public String prodID, name, brand, category, shortdesc, longdesc, scents;
    public double price;

    public Product(@NonNull Integer sku, String prodID, String name, String brand, double price, String category, String shortdesc, String longdesc){
        this.sku = sku;
        this.prodID = prodID;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.category = category;
        this.shortdesc = shortdesc;
        this.longdesc = longdesc;
        this.scents = "";
        this.prob = 0;
    }
}
