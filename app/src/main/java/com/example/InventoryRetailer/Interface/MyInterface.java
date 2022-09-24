package com.example.InventoryRetailer.Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MyInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<String> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("fetch_product.php")
    Call<String> fetch_product(@Field("qr_code_no") String qr_code_no);

    @FormUrlEncoded
    @POST("add_product.php")
    Call<String> add_product(@Field("qr_code_no") String qr_code_no, @Field("product_name") String product_name, @Field("price") String price,
                             @Field("quantity") String quantity, @Field("user_id") String user_id,
                             @Field("gst_amount") String gst_amount, @Field("retailer_pecent") String retailer_percent,
                             @Field("image") String image, @Field("product_id") String product_id, @Field("retailer_commission") String retailer_commission);


    @FormUrlEncoded
    @POST("fetch_count.php")
    Call<String> fetch_count(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("fetch_cart.php")
    Call<String> fetch_cart(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("fetch_sale_order_history.php")
    Call<String> fetch_sale_order_history(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("fetch_sale_product.php")
    Call<String> fetch_sale_product(@Field("invoice_no") String invoice_no);


    @FormUrlEncoded
    @POST("remove_cart_product.php")
    Call<String> remove_cart_product(@Field("qr_code_no") String qr_code_no, @Field("quantity") String quantity, @Field("product_id") String product_id,
                                     @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("fetch_profile.php")
    Call<String> fetch_profile(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("generate_bill.php")
    Call<String> generate_bill(@Field("user_id") String user_id, @Field("total_price") String total_price, @Field("grand_total") String grand_total,
                               @Field("total_gst_amount") String total_gst_amount, @Field("total_commission") String total_commission);



    //Order Product

    @FormUrlEncoded
    @POST("add_order_product.php")
    Call<String> add_order_product(@Field("qr_code_no") String qr_code_no, @Field("user_id") String user_id, @Field("product_id") String product_id, @Field("quantity") String quantity);

    @FormUrlEncoded
    @POST("fetch_order_cart.php")
    Call<String> fetch_order_cart(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("fetch_order_count.php")
    Call<String>fetch_order_count(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("remove_order_cart_product.php")
    Call<String> remove_order_cart_product(@Field("qr_code_no") String qr_code_no);

    @FormUrlEncoded
    @POST("request_order.php")
    Call<String> request_order(@Field("user_id") String user_id, @Field("com_id") String com_id);

    @FormUrlEncoded
    @POST("fetch_stocks.php")
    Call<String> fetch_stocks(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("fetch_order_history.php")
    Call<String> fetch_order_history(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("fetch_order_product.php")
    Call<String> fetch_order_product(@Field("invoice_no") String invoice_no);

    @FormUrlEncoded
    @POST("fetch_order_history_date_wise.php")
    Call<String> fetch_order_history_date_wise(@Field("user_id") String user_id, @Field("date") String date);

    @FormUrlEncoded
    @POST("fetch_sale_order_date_wise.php")
    Call<String> fetch_sale_order_date_wise(@Field("user_id") String user_id, @Field("date") String date);

}
