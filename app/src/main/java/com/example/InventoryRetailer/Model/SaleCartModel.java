package com.example.InventoryRetailer.Model;

public class SaleCartModel {

    String product_id, product_name, price, qr_code_no, quantity, gst_amount, image, retailer_percentage;

    public SaleCartModel(String product_id, String product_name, String price, String qr_code_no, String quantity, String gst_amount, String image, String retailer_percentage) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.price = price;
        this.qr_code_no = qr_code_no;
        this.quantity = quantity;
        this.gst_amount = gst_amount;
        this.image = image;
        this.retailer_percentage = retailer_percentage;
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

    public String getGst_amount() {
        return gst_amount;
    }

    public String getImage() {
        return image;
    }

    public String getRetailer_percentage() {
        return retailer_percentage;
    }
}
