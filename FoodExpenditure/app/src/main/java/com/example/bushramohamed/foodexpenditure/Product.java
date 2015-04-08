package com.example.bushramohamed.foodexpenditure;

/**
 * Created by bushramohamed on 2015-03-23.
 */
public class Product {

    public static double totalSpent2012 = 0.0;
    public static double totalSpent2013 = 0.0;

    private int value;
    private String productType;

    //Total spent on specific food product
    private double spending_12;
    private double spending_13;

    private int pctFromTotalSpending_12;
    private int pctFromTotalSpending_13;

    //Setters

    public void setValue(int value){this.value = value;}

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setSpending_12(double spending_12) {
        this.spending_12 = spending_12;
    }

    public void setSpending_13(double spending_13) {
        this.spending_13 = spending_13;
    }

    public void setPctFromTotalSpending_12(int pctFromTotalSpending_12) {
        this.pctFromTotalSpending_12 = pctFromTotalSpending_12;
    }

    public void setPctFromTotalSpending_13(int pctFromTotalSpending_13) {
        this.pctFromTotalSpending_13 = pctFromTotalSpending_13;
    }


    public Product(){ spending_12 = 0.0; spending_13 = 0.0; }

    //Getters
    public int getValue(){return value;}

    public String getProductType() {
        return productType;
    }

    public double getSpending_12() {
        return spending_12;
    }

    public int getPctFromTotalSpending_12() {
        return pctFromTotalSpending_12;
    }

    public double getSpending_13() {
        return spending_13;
    }

    public int getPctFromTotalSpending_13() {
        return pctFromTotalSpending_13;
    }
}
