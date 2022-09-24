package com.example.InventoryRetailer.Model;

public class Sale_Order_History_Model {

    String invoice_no, price_with_out_gst, price_with_gst, total_gst_amount, create_date, retailer_commission;

    public Sale_Order_History_Model(String invoice_no, String price_with_out_gst, String price_with_gst, String total_gst_amount, String create_date, String retailer_commission) {
        this.invoice_no = invoice_no;
        this.price_with_out_gst = price_with_out_gst;
        this.price_with_gst = price_with_gst;
        this.total_gst_amount = total_gst_amount;
        this.create_date = create_date;
        this.retailer_commission = retailer_commission;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public String getPrice_with_out_gst() {
        return price_with_out_gst;
    }

    public String getPrice_with_gst() {
        return price_with_gst;
    }

    public String getTotal_gst_amount() {
        return total_gst_amount;
    }

    public String getCreate_date() {
        return create_date;
    }

    public String getRetailer_commission() {
        return retailer_commission;
    }
}
