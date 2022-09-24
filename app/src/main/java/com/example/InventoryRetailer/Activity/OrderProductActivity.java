package com.example.InventoryRetailer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.InventoryRetailer.ApiClient.ApiClient;
import com.example.InventoryRetailer.Interface.MyInterface;
import com.example.InventoryRetailer.R;
import com.example.InventoryRetailer.User.User;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderProductActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    Button btn_add_to_request, btn_cancel;
    MyInterface myInterface;
    ProgressDialog progressDialog;
    TextView txt_product_name, txt_price;
    public static TextView txt_cart_count;
    EditText et_quantity;
    ImageView img_product;
    User user;
    String hold_image, hold_gst_amount, hold_retailer_percent, hold_product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);

        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);


        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void handleResult(Result rawResult) {


        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_order_item);
        dialog.setCancelable(false);
        btn_add_to_request = dialog.findViewById(R.id.btn_add_to_request);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        txt_product_name = dialog.findViewById(R.id.product_name);
        txt_price = dialog.findViewById(R.id.price);
        et_quantity = dialog.findViewById(R.id.et_quantity);
        img_product = dialog.findViewById(R.id.img_product);

        btn_add_to_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_quantity.getText().toString().isEmpty())
                {
                    Toast.makeText(OrderProductActivity.this, "Quantity is required", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    addProduct(rawResult.getText(),user.getUser_id(),hold_product_id,et_quantity.getText().toString(),dialog);

                }



            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                scannerView.setResultHandler(OrderProductActivity.this::handleResult);
                scannerView.startCamera();

            }
        });


        fetchProduct(rawResult.getText(), dialog);

        dialog.show();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.menu_cart);
        View view = menuItem.getActionView();
        txt_cart_count = view.findViewById(R.id.txt_cart_count);

        fetchOrderCount(txt_cart_count);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(OrderProductActivity.this, OrderCartActivity.class));
            }
        });

        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }



    private void fetchProduct(String qr_code_no, Dialog dialog)
    {
        Call<String> call = myInterface.fetch_product(qr_code_no);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        txt_product_name.setText(jsonObject.getString("prod_name"));
                        hold_product_id= jsonObject.getString("id");

                        hold_image = jsonObject.getString("image");

                         /*txt_price.setText(jsonObject.getString("amount_no_gst"));
                        hold_gst_amount= jsonObject.getString("gst_amount");
                        hold_retailer_percent= jsonObject.getString("retailer_percent");*/

                        //String img = img_url+jsonObject.getString("image");

                       /* Glide.with(MainActivity.this)
                                .load(img)
                                .into(img_product);*/

                        progressDialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(OrderProductActivity.this, "Product Not Found", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        dialog.dismiss();

                       /* scannerView.setResultHandler(OrderProductActivity.this::handleResult);
                        scannerView.startCamera();*/
                    }

                } catch (JSONException e) {

                    Toast.makeText(OrderProductActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(OrderProductActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private void addProduct(String qr_code_no, String user_id, String product_id,  String quantity, Dialog dialog)
    {
        Call<String> call = myInterface.add_order_product(qr_code_no,user_id,product_id,quantity);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {

                        Toast.makeText(OrderProductActivity.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        dialog.dismiss();
                        scannerView.setResultHandler(OrderProductActivity.this::handleResult);
                        scannerView.startCamera();

                        fetchOrderCount(txt_cart_count);

                    }

                    else if (jsonObject.getString("rec").equals("2"))
                    {
                        Toast.makeText(OrderProductActivity.this, "Product Already Add", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    else
                    {
                        Toast.makeText(OrderProductActivity.this, "Not Add", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        dialog.dismiss();

                       /* scannerView.setResultHandler(OrderProductActivity.this::handleResult);
                        scannerView.startCamera();*/
                    }

                } catch (JSONException e) {

                    Toast.makeText(OrderProductActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(OrderProductActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }




    private void fetchOrderCount(TextView txt_cart_count)
    {
        Call<String> call = myInterface.fetch_order_count(user.getUser_id());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        txt_cart_count.setText(jsonObject.getString("count"));
                    }
                    else
                    {
                        txt_cart_count.setText("0");
                    }

                } catch (JSONException e) {

                    Toast.makeText(OrderProductActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(OrderProductActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }



}