package com.example.InventoryRetailer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.InventoryRetailer.Adapter.SaleOrderHistoryAdapter;
import com.example.InventoryRetailer.ApiClient.ApiClient;
import com.example.InventoryRetailer.Interface.MyInterface;
import com.example.InventoryRetailer.Model.Sale_Order_History_Model;
import com.example.InventoryRetailer.User.User;
import com.example.InventoryRetailer.databinding.ActivitySaleOrderHistoryBinding;;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleOrderHistoryActivity extends AppCompatActivity {

    ActivitySaleOrderHistoryBinding binding;
    ArrayList<Sale_Order_History_Model> arrayList_order = new ArrayList<>();
    MyInterface myInterface;
    ProgressDialog progressDialog;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySaleOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        binding.txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar mcurrentDate = Calendar.getInstance();

                int mDay   = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mYear  = mcurrentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SaleOrderHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String fmonth, fDate;


                        try {
                            if (month < 10 && dayOfMonth < 10) {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                fDate = "0" + dayOfMonth;
                                String paddedMonth = String.format("%02d", month);
                                binding.txtDate.setText(year + "-" + paddedMonth + "-" + fDate);

                                fetchSaleOrderHistoryDateWise();

                            }
                            else if (month >= 10 && dayOfMonth < 10) {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                fDate = "0" + dayOfMonth;
                                String paddedMonth = String.format("%02d", month);
                                binding.txtDate.setText(year + "-" + paddedMonth + "-" + fDate);

                                fetchSaleOrderHistoryDateWise();
                            }
                            else {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                String paddedMonth = String.format("%02d", month);
                                binding.txtDate.setText(year + "-" + paddedMonth + "-" + dayOfMonth);

                                fetchSaleOrderHistoryDateWise();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();

            }
        });

        fetchSaleOrderHistory();
    }


    private void fetchSaleOrderHistory()
    {

        Call<String> call = myInterface.fetch_sale_order_history(user.getUser_id());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        binding.rvOrder.setVisibility(View.GONE);
                        Toast.makeText(SaleOrderHistoryActivity.this, "No Product", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        arrayList_order.clear();
                        binding.rvOrder.setVisibility(View.VISIBLE);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String invoice_no = jsonObject.getString("invoice_no");
                            String total_price = jsonObject.getString("total_price");
                            String grand_total = jsonObject.getString("grand_total");
                            String total_gst_amount = jsonObject.getString("total_gst_amount");
                            String created_at = jsonObject.getString("created_at");
                            String total_retailer_commission = jsonObject.getString("total_retailer_commission");

                            Sale_Order_History_Model model = new Sale_Order_History_Model(invoice_no,total_price,grand_total,total_gst_amount,created_at,total_retailer_commission);
                            arrayList_order.add(model);
                        }


                        binding.rvOrder.setAdapter(new SaleOrderHistoryAdapter(SaleOrderHistoryActivity.this,arrayList_order));
                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {

                    Toast.makeText(SaleOrderHistoryActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(SaleOrderHistoryActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }



    private void fetchSaleOrderHistoryDateWise()
    {

        Call<String> call = myInterface.fetch_sale_order_date_wise(user.getUser_id(),binding.txtDate.getText().toString());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        binding.rvOrder.setVisibility(View.GONE);
                        Toast.makeText(SaleOrderHistoryActivity.this, "No Product", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        arrayList_order.clear();
                        binding.rvOrder.setVisibility(View.VISIBLE);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String invoice_no = jsonObject.getString("invoice_no");
                            String total_price = jsonObject.getString("total_price");
                            String grand_total = jsonObject.getString("grand_total");
                            String total_gst_amount = jsonObject.getString("total_gst_amount");
                            String created_at = jsonObject.getString("created_at");
                            String total_retailer_commission = jsonObject.getString("total_retailer_commission");

                            Sale_Order_History_Model model = new Sale_Order_History_Model(invoice_no,total_price,grand_total,total_gst_amount,created_at,total_retailer_commission);
                            arrayList_order.add(model);
                        }


                        binding.rvOrder.setAdapter(new SaleOrderHistoryAdapter(SaleOrderHistoryActivity.this,arrayList_order));
                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {

                    Toast.makeText(SaleOrderHistoryActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(SaleOrderHistoryActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}