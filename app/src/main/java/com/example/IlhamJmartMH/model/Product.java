package com.example.IlhamJmartMH.model;

public class Product extends Serializable{
    public int accountId;
    public String name;
    public int weight;
    public boolean conditionUsed;
    public double price;
    public double discount;
    public byte shipmentPlans;
    public ProductCategory category;

    public String toString() {
        return  "Name : " + this.name + "\nWeight : " + this.weight +
                "\nconditionUsed : " + this.conditionUsed + "\nprice : " +
                this.price + "\ncategory : " + this.category + "\ndiscount : " +
                this.discount + "\naccountId : " + this.accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isConditionUsed() {
        return conditionUsed;
    }

    public void setConditionUsed(boolean conditionUsed) {
        this.conditionUsed = conditionUsed;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public byte getShipmentPlans() {
        return shipmentPlans;
    }

    public void setShipmentPlans(byte shipmentPlans) {
        this.shipmentPlans = shipmentPlans;
    }
}
