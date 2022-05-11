package com.example.myapplication;

public class Item {
    private String name,username;
    private int amount;
    public Item(){}
    public Item(String name,int amount,String username){
        this.amount=amount;
        this.name=name;
        this.username=username;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
