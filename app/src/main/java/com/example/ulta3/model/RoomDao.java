package com.example.ulta3.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Query("UPDATE product SET scents=:scents WHERE sku = :id")
    void addScents(int id, String scents);

    @Query("UPDATE product SET prob = prob + :i WHERE sku = :id")
    void incrementProb(int id, int i);

    @Query("UPDATE product SET prob=0")
    void resetProb();

    @Query("SELECT prob FROM product WHERE sku = :sku")
    int getProb(int sku);

    @Query("SELECT COUNT(*) FROM product")
    int getCount();

    @Query("SELECT sku FROM product WHERE prob!=0 ORDER BY prob DESC")
    List<Integer> getProbNonzeroSorted();

    @Query("SELECT sku FROM product WHERE category=:category")
    List<Integer> getProductsByCategory(String category);

    @Query("SELECT sku FROM product WHERE shortdesc LIKE :str OR longdesc LIKE :str")
    List<Integer> getProductsByDescription(String str);

    @Query("SELECT sku FROM product WHERE shortdesc LIKE :s OR shortdesc LIKE :s1 OR shortdesc LIKE :s2 OR longdesc LIKE :s OR longdesc LIKE :s1 OR longdesc LIKE :s2")
    List<Integer> getManProducts(String s, String s1, String s2);

    @Query("SELECT sku FROM product WHERE category LIKE :s")
    List<Integer> getSearchCategory(String s);

    @Query("SELECT sku FROM product WHERE name LIKE :s")
    List<Integer> getSearchName(String s);

    @Query("SELECT sku FROM product WHERE brand LIKE :s")
    List<Integer> getSearchBrand(String s);

    @Query("SELECT sku FROM product WHERE shortdesc LIKE :s OR longdesc LIKE :s")
    List<Integer> getSearchDesc(String s);

    @Query("SELECT name FROM product WHERE sku = :sku")
    String getName(int sku);

    @Query("SELECT brand FROM product WHERE sku = :sku")
    String getBrand(int sku);

    @Query("SELECT price FROM product WHERE sku = :sku")
    double getPrice(int sku);

    @Query("SELECT category FROM product WHERE sku = :sku")
    String getCategory(int sku);

    @Query("SELECT shortdesc FROM product WHERE sku = :sku")
    String getShortDesc(int sku);

    @Query("SELECT longdesc FROM product WHERE sku = :sku")
    String getLongDesc(int sku);
}