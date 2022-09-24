package com.example.InventoryRetailer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.InventoryRetailer.ApiClient.ApiClient;
import com.example.InventoryRetailer.Interface.MyInterface;
import com.example.InventoryRetailer.R;
import com.example.InventoryRetailer.User.User;
import com.example.InventoryRetailer.databinding.ActivityHomePageBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity {

    ActivityHomePageBinding binding;
    User user;
    MyInterface myInterface;
    ProgressDialog progressDialog;
    TextView txt_cart_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);


        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            binding.txtGoodMorning.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            binding.txtGoodMorning.setText("Good Afternoon");

        }else if(timeOfDay >= 16 && timeOfDay < 21){

            binding.txtGoodMorning.setText("Good Evening");

        }else if(timeOfDay >= 21 && timeOfDay < 24){

            binding.txtGoodMorning.setText("Good Night");

        }



        binding.saleOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePageActivity.this, SaleOrderHistoryActivity.class));
                //finish();
            }
        });


        binding.saleCartProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePageActivity.this, SaleCartActivity.class));
                //finish();
            }
        });


        binding.saleScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePageActivity.this,MainActivity.class));
                //finish();
            }
        });


        binding.orderScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePageActivity.this,OrderProductActivity.class));
                //finish();
            }
        });



        binding.orderCartProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePageActivity.this, OrderCartActivity.class));
                //finish();
            }
        });


        binding.stocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePageActivity.this, StocksActivity.class));
                //finish();
            }
        });

        binding.orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePageActivity.this, OrderHistoryActivity.class));
                //finish();
            }
        });


        fetchProfileDetails();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_log_out, menu);
        /*getMenuInflater().inflate(R.menu.main_log_out, menu);

        final MenuItem menuItem = menu.findItem(R.id.menu_cart);
        View view = menuItem.getActionView();
        txt_cart_count = view.findViewById(R.id.txt_cart_count);

        txt_cart_count.setText(MainActivity.txt_cart_count.getText().toString());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePageActivity.this,CartActivity.class));
            }
        });
*/
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menu_log_out:
                user.remove();
                startActivity(new Intent(HomePageActivity.this,LoginActivity.class));
                finishAffinity();

                break;

        }
        return true;
    }



    private void fetchProfileDetails()
    {
        Call<String> call = myInterface.fetch_profile(user.getUser_id());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {

                        binding.userName.setText(jsonObject.getString("name"));
                        binding.email.setText(jsonObject.getString("email"));
                        progressDialog.dismiss();

                    }
                    else
                    {
                        //Toast.makeText(HomePageActivity.this, "Details not found", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(HomePageActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(HomePageActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }



    @Override
    public void onBackPressed() {

        finishAffinity();
    }
}