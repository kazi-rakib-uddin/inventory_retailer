package com.example.InventoryRetailer.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.InventoryRetailer.Adapter.OrderCartAdapter;
import com.example.InventoryRetailer.Adapter.SaleCartAdapter;
import com.example.InventoryRetailer.ApiClient.ApiClient;
import com.example.InventoryRetailer.Interface.MyInterface;
import com.example.InventoryRetailer.Model.OrderCartModel;
import com.example.InventoryRetailer.Model.SaleCartModel;
import com.example.InventoryRetailer.User.User;
import com.example.InventoryRetailer.databinding.ActivityOrderCartBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderCartActivity extends AppCompatActivity implements OrderCartAdapter.btnRemoveInterface {

    ActivityOrderCartBinding binding;
    ArrayList<OrderCartModel> orderCartModels = new ArrayList<>();
    MyInterface myInterface;
    ProgressDialog progressDialog;
    User user;
    private static final String img_url = "http://tiwaryleather.com/New_Inventory/public/product_pic/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderCartBinding.inflate(getLayoutInflater());
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


        binding.btnRequestProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestOrder();
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

        Call<String> call = myInterface.fetch_order_cart(user.getUser_id());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        binding.rvCart.setVisibility(View.GONE);
                        binding.btnRequestProduct.setVisibility(View.GONE);
                        //Toast.makeText(OrderCartActivity.this, "No Product", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        orderCartModels.clear();
                        binding.rvCart.setVisibility(View.VISIBLE);
                        binding.btnRequestProduct.setVisibility(View.VISIBLE);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String product_id = jsonObject.getString("product_id");
                            String qr_code_no = jsonObject.getString("qr_code_no");
                            String product_name = jsonObject.getString("product_name");
                            String price = jsonObject.getString("product_price");
                            String quantity = jsonObject.getString("quantity");
                            String image = img_url+jsonObject.getString("image");

                            OrderCartModel model = new OrderCartModel(product_id,product_name,price,qr_code_no,quantity,image);
                            orderCartModels.add(model);
                        }


                        binding.rvCart.setAdapter(new OrderCartAdapter(OrderCartActivity.this,orderCartModels,OrderCartActivity.this));
                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {

                    Toast.makeText(OrderCartActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(OrderCartActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public void removeProduct(String qrCodeNo) {

        deleteCartProduct(qrCodeNo);
    }

    private void deleteCartProduct(String qr_code_no)
    {
        Call<String> call = myInterface.remove_order_cart_product(qr_code_no);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        Toast.makeText(OrderCartActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        fetchCart();
                    }
                    else
                    {
                        Toast.makeText(OrderCartActivity.this, "Not Removed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(OrderCartActivity.this, "somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(OrderCartActivity.this, "slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }




    private void requestOrder() {

        Call<String> call = myInterface.request_order(user.getUser_id(), user.getCompany_code());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        Toast.makeText(OrderCartActivity.this, "Order Request Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        fetchCart();
                    }
                    else if (jsonObject.getString("rec").equals("2"))
                    {
                        progressDialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(OrderCartActivity.this, "Not Request", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(OrderCartActivity.this, "somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(OrderCartActivity.this, "slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }




}