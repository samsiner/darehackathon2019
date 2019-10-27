package com.example.ulta3.model;

public class Store {

    private int id, cursor, dist;
    private String address;

    public Store(int id){
        this.id = id;
    }

    public int getID(){return id;}

    public void setCursor(int cursor){ this.cursor = cursor;}
    public int getCursor(){ return cursor;}

    public void setDistance(int dist){ this.dist = dist;}
    public int getDistance(){ return dist;}

    public void setAddress(String address){this.address=address;}
    public String getAddress(){return address;}
}
