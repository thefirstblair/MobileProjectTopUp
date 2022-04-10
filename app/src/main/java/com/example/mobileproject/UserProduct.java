package com.example.mobileproject;

public class UserProduct {
    String productName, productCode, productTime;


    public UserProduct(){};

    public UserProduct(String productName, String productCode, String productTime) {
        this.productName = productName;
        this.productCode = productCode;
        this.productTime = productTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductTime() {
        return productTime;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }
}
