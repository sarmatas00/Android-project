package com.example.myapplication;

//extends item class to include amount for an item
public class EnchancedItem extends Item{
    private double value;
    public EnchancedItem(){
    }
    public EnchancedItem(String name,double value){
        super(name);
        this.value=value;
    }


    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
