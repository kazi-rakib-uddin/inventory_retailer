package com.example.InventoryRetailer.Model;

public class OrderCartModel {

    String product_id, product_name, price, qr_code_no, quantity, image;

    public OrderCartModel(String product_id, String product_name, String price, String qr_code_no, String quantity, String image) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.price = price;
        this.qr_code_no = qr_code_no;
        this.quantity = quantity;
        this.image = image;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getPrice() {
        return price;
    }

    public String getQr_code_no() {
        return qr_code_no;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getImage() {
        return image;
    }
}
