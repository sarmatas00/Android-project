package com.example.myapplication;

//Item class has a name which is unique used as primary key for db
//and cost more attributes can be added but DB MUST BE CHANGED

public class Item {
    private String name;
    private double cost;
    public Item(){}
    public Item(String name,double cost){
        this.cost=cost;
        this.name=name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
