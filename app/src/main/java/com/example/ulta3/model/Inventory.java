package com.example.ulta3.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "inventory", primaryKeys = {"store_id", "sku"})
public class Inventory {

    public int store_id, sku, stock;

    public Inventory(int store_id, int sku, int stock){
        this.sku = sku;
        this.store_id = store_id;
        this.stock = stock;
    }
}
