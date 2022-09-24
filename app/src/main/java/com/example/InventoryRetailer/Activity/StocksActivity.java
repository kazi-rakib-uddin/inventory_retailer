package com.example.InventoryRetailer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.InventoryRetailer.Adapter.StocksAdapter;
import com.example.InventoryRetailer.ApiClient.ApiClient;
import com.example.InventoryRetailer.Interface.MyInterface;
import com.example.InventoryRetailer.Model.StocksModel;
import com.example.InventoryRetailer.R;
import com.example.InventoryRetailer.User.User;
import com.example.InventoryRetailer.databinding.ActivityStocksBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StocksActivity extends AppCompatActivity {

    ActivityStocksBinding binding;
    MyInterface myInterface;
    ProgressDialog progressDialog;
    User user;
    ArrayList<StocksModel> stocksModels;
    StocksAdapter adapter;
    private static final String img_url = "http://tiwaryleather.com/New_Inventory/public/product_pic/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStocksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        fetchStocks();

        adapter = new StocksAdapter(this,stocksModels);
        binding.rvCart.setAdapter(adapter);

        binding.searchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==0){
                    binding.rvCart.setVisibility(View.GONE);
                    binding.notFound.setVisibility(View.VISIBLE);
                    //clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")){
                    binding.rvCart.setVisibility(View.VISIBLE);
                    binding.notFound.setVisibility(View.GONE);
                    fetchStocks();
                    //clear.setVisibility(View.GONE);
                }else{
                    filter(s.toString());
                    //clear.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    public void filter(String query) {
        ArrayList<StocksModel> filteredList = new ArrayList<>();
        for (StocksModel model : stocksModels) {
            if(model.getProduct_name().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(model);
            }
        }

        if (filteredList.isEmpty())
        {
            binding.rvCart.setVisibility(View.GONE);
            binding.notFound.setVisibility(View.VISIBLE);
            //Toast.makeText(this, "Medicine Not Found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            binding.rvCart.setVisibility(View.VISIBLE);
            binding.notFound.setVisibility(View.GONE);
            adapter.filterList(filteredList);
        }

    }


    private void fetchStocks()
    {
        stocksModels = new ArrayList<>();

        Call<String> call = myInterface.fetch_stocks(user.getUser_id());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        Toast.makeText(StocksActivity.this, "Stock Not Found", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else
                    {

                        stocksModels.clear();
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String product_id = jsonObject.getString("product_id");
                            String product_name = jsonObject.getString("product_name");
                            String stock = jsonObject.getString("stock");
                            String img = img_url+jsonObject.getString("image");

                            StocksModel model = new StocksModel(product_id,product_name,stock,img);

                            stocksModels.add(model);
                        }

                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {

                    Toast.makeText(StocksActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(StocksActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}