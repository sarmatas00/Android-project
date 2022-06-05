package com.example.myapplication;

//Item class has a name which is unique

public class Item {
    private String name;
    private double cost;
    public Item(){}
    public Item(String name){

        this.name=name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
