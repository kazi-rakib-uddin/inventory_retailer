package com.example.InventoryRetailer.Model;

public class StocksModel {

    String product_id, product_name, quantity, image;

    public StocksModel(String product_id, String product_name, String quantity, String image) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.quantity = quantity;
        this.image = image;
    }


    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getImage() {
        return image;
    }
}
