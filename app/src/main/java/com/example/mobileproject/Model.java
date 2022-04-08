package com.example.mobileproject;

public class Model {

    String productName;
    String productDetail;
    int productPhoto;

    public Model(String productName, String productDetail, int productPhoto) {
        this.productName = productName;
        this.productDetail = productDetail;
        this.productPhoto = productPhoto;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public int getProductPhoto() {
        return productPhoto;
    }
}
