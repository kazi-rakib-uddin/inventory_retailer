package com.example.InventoryRetailer.Model;

public class Order_History_Model {

    String invoice_no, create_date, status;

    public Order_History_Model(String invoice_no, String create_date, String status) {
        this.invoice_no = invoice_no;
        this.create_date = create_date;
        this.status = status;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public String getCreate_date() {
        return create_date;
    }

    public String getStatus() {
        return status;
    }
}
