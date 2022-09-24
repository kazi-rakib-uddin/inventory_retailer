package com.example.InventoryRetailer.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.InventoryRetailer.Adapter.SaleCartAdapter;
import com.example.InventoryRetailer.ApiClient.ApiClient;
import com.example.InventoryRetailer.Interface.MyInterface;
import com.example.InventoryRetailer.Model.SaleCartModel;
import com.example.InventoryRetailer.User.User;
import com.example.InventoryRetailer.databinding.ActivitySaleCartBinding;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleCartActivity extends AppCompatActivity implements SaleCartAdapter.btnRemoveInterface {

    ActivitySaleCartBinding binding;
    ArrayList<SaleCartModel> arrayList_cart = new ArrayList<>();
    MyInterface myInterface;
    ProgressDialog progressDialog;
    public static int total_price=0,total = 0, gst_total =0, total_retailer_commission=0;;
    User user;
    String gst_amount;
    private static final String img_url = "http://tiwaryleather.com/New_Inventory/public/product_pic/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySaleCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

       fetchCart();

       binding.btnBill.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               generateBill();
           }
       });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void fetchCart()
    {

        total_price=0;
        total = 0;
        gst_total=0;
        total_retailer_commission=0;


        Call<String> call = myInterface.fetch_cart(user.getUser_id());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        binding.rvCart.setVisibility(View.GONE);
                        binding.linOrderDetails.setVisibility(View.GONE);
                        //Toast.makeText(SaleCartActivity.this, "No Product", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        total = 0;
                        total_price = 0;
                        gst_total=0;
                        total_retailer_commission=0;
                        binding.total.setText("₹ 0");
                    }
                    else
                    {
                        binding.linOrderDetails.setVisibility(View.VISIBLE);
                        arrayList_cart.clear();
                        SaleCartActivity.this.binding.rvCart.setVisibility(View.VISIBLE);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String product_id = jsonObject.getString("product_id");
                            String qr_code_no = jsonObject.getString("qr_code_no");
                            String product_name = jsonObject.getString("product_name");
                            String price = jsonObject.getString("price");
                            String quantity = jsonObject.getString("quantity");
                            gst_amount = jsonObject.getString("gst_amount");
                            String retailer_commission = jsonObject.getString("retailer_commission");
                            String retailer_percentage = jsonObject.getString("retailer_percentage");
                            String image = img_url+jsonObject.getString("image");


                            gst_total = (gst_total + Integer.parseInt(gst_amount));

                            total = total + Integer.parseInt(price) * (Integer.parseInt(quantity));

                            total_price = total + gst_total;


                            total_retailer_commission = (total_retailer_commission + Integer.parseInt(retailer_commission));


                            SaleCartModel model = new SaleCartModel(product_id,product_name,price,qr_code_no,quantity,gst_amount,image,retailer_percentage);
                            arrayList_cart.add(model);


                        }

                        binding.total.setText("₹ "+String.valueOf(total_price));
                        binding.txtPrice.setText("₹ "+String.valueOf(total));
                        binding.txtGstAmount.setText("₹ "+String.valueOf(gst_total));
                        binding.txtTotal.setText("₹ "+String.valueOf(total_price));

                        SaleCartActivity.this.binding.rvCart.setAdapter(new SaleCartAdapter(SaleCartActivity.this,arrayList_cart, SaleCartActivity.this));
                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {

                    Toast.makeText(SaleCartActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(SaleCartActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void removeProduct(String qr_code_no, String quantity, String product_id) {

        deleteCartProduct(qr_code_no,quantity,product_id);

    }

    private void deleteCartProduct(String qr_code_no, String quantity, String product_id)
    {
        Call<String> call = myInterface.remove_cart_product(qr_code_no,quantity,product_id,user.getUser_id());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        Toast.makeText(SaleCartActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        fetchCart();
                    }
                    else
                    {
                        Toast.makeText(SaleCartActivity.this, "Not Removed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(SaleCartActivity.this, "somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(SaleCartActivity.this, "slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }



    private void generateBill()
    {
        Call<String> call = myInterface.generate_bill(user.getUser_id(), String.valueOf(total), String.valueOf(total_price), String.valueOf(gst_total), String.valueOf(total_retailer_commission));
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        Toast.makeText(SaleCartActivity.this, "Order Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        fetchCart();

                        String invoice_no = jsonObject.getString("invoice_no");

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                            Base64.Encoder encoder = Base64.getEncoder();
                            String encoded_invoice_no = encoder.encodeToString(invoice_no.getBytes());

                            startActivity(new Intent(SaleCartActivity.this, PDFActivity.class).putExtra("invoice_no",encoded_invoice_no));
                            finish();
                        }


                    }
                    else if (jsonObject.getString("rec").equals("2"))
                    {
                        progressDialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(SaleCartActivity.this, "Not Order", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(SaleCartActivity.this, "somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(SaleCartActivity.this, "slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}