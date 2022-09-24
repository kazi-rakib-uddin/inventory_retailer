package com.example.InventoryRetailer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.InventoryRetailer.Adapter.OrderHistoryAdapter;
import com.example.InventoryRetailer.Adapter.SaleOrderHistoryAdapter;
import com.example.InventoryRetailer.ApiClient.ApiClient;
import com.example.InventoryRetailer.Interface.MyInterface;
import com.example.InventoryRetailer.Model.Order_History_Model;
import com.example.InventoryRetailer.Model.Sale_Order_History_Model;
import com.example.InventoryRetailer.User.User;
import com.example.InventoryRetailer.databinding.ActivityOrderHistoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    ActivityOrderHistoryBinding binding;
    ArrayList<Order_History_Model> arrayList_order = new ArrayList<>();
    MyInterface myInterface;
    ProgressDialog progressDialog;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                                fetchsOrderHistoryDateWise();

                            }
                            else if (month >= 10 && dayOfMonth < 10) {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                fDate = "0" + dayOfMonth;
                                String paddedMonth = String.format("%02d", month);
                                binding.txtDate.setText(year + "-" + paddedMonth + "-" + fDate);

                                fetchsOrderHistoryDateWise();
                            }
                            else {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                String paddedMonth = String.format("%02d", month);
                                binding.txtDate.setText(year + "-" + paddedMonth + "-" + dayOfMonth);

                                fetchsOrderHistoryDateWise();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();

            }
        });

        fetchsOrderHistory();
    }


    private void fetchsOrderHistory()
    {

        Call<String> call = myInterface.fetch_order_history(user.getUser_id());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        binding.rvOrderHistory.setVisibility(View.GONE);
                        Toast.makeText(OrderHistoryActivity.this, "No Product", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        arrayList_order.clear();
                        binding.rvOrderHistory.setVisibility(View.VISIBLE);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String invoice_no = jsonObject.getString("invoice_no");
                            String created_at = jsonObject.getString("created_at");
                            String status = jsonObject.getString("status");

                            Order_History_Model model = new Order_History_Model(invoice_no,created_at,status);
                            arrayList_order.add(model);
                        }


                        binding.rvOrderHistory.setAdapter(new OrderHistoryAdapter(OrderHistoryActivity.this,arrayList_order));
                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {

                    Toast.makeText(OrderHistoryActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(OrderHistoryActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private void fetchsOrderHistoryDateWise()
    {

        Call<String> call = myInterface.fetch_order_history_date_wise(user.getUser_id(),binding.txtDate.getText().toString());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        binding.rvOrderHistory.setVisibility(View.GONE);
                        Toast.makeText(OrderHistoryActivity.this, "No Product", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        arrayList_order.clear();
                        binding.rvOrderHistory.setVisibility(View.VISIBLE);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String invoice_no = jsonObject.getString("invoice_no");
                            String created_at = jsonObject.getString("created_at");
                            String status = jsonObject.getString("status");

                            Order_History_Model model = new Order_History_Model(invoice_no,created_at,status);
                            arrayList_order.add(model);
                        }


                        binding.rvOrderHistory.setAdapter(new OrderHistoryAdapter(OrderHistoryActivity.this,arrayList_order));
                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {

                    Toast.makeText(OrderHistoryActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(OrderHistoryActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}