package com.example.InventoryRetailer.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.example.InventoryRetailer.Activity.PDFActivity;
import com.example.InventoryRetailer.ApiClient.ApiClient;
import com.example.InventoryRetailer.Interface.MyInterface;
import com.example.InventoryRetailer.Model.Sale_Order_History_Model;
import com.example.InventoryRetailer.Model.Sale_All_Product_Model;
import com.example.InventoryRetailer.R;
import com.example.InventoryRetailer.databinding.CustomSaleOrdersHistoryBinding;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleOrderHistoryAdapter extends RecyclerView.Adapter<SaleOrderHistoryAdapter.MyViewHolder> {

    ArrayList<Sale_Order_History_Model> arrayList_catagory;
    ArrayList<Sale_All_Product_Model> sale_all_products;
    Context context;
    RecyclerView rv_sale_product;
    MyInterface myInterface;
    private static final String img_url = "http://tiwaryleather.com/New_Inventory/public/product_pic/";

    public SaleOrderHistoryAdapter(Context context, ArrayList<Sale_Order_History_Model> arrayList_catagory) {
        this.arrayList_catagory = arrayList_catagory;
        this.context=context;
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_sale_orders_history,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.invoiceNo.setText("Invoice No : "+arrayList_catagory.get(position).getInvoice_no());
        holder.binding.txtGstAmount.setText("Total GST : ₹ "+arrayList_catagory.get(position).getTotal_gst_amount());
        holder.binding.priceWithOutGst.setText("Price ( With Out GST ) : ₹ "+arrayList_catagory.get(position).getPrice_with_out_gst());
        holder.binding.priceWithGst.setText("Price ( With GST ) : ₹ "+arrayList_catagory.get(position).getPrice_with_gst());
        holder.binding.txtRetailerCommission.setText("Total Commission : ₹ "+arrayList_catagory.get(position).getRetailer_commission());


        try {
            DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = inputFormatter.parse(arrayList_catagory.get(position).getCreate_date());
            DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
            holder.binding.cDate.setText("Date : "+outputFormatter.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.binding.btnDetails.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_sale_product);

                rv_sale_product = dialog.findViewById(R.id.rv_sale_product);

                fetchSaleProduct(arrayList_catagory.get(position).getInvoice_no());

                dialog.show();

            }
        });


        holder.binding.btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    Base64.Encoder encoder = Base64.getEncoder();
                    String invoice_no = arrayList_catagory.get(position).getInvoice_no();
                    String encoded_invoice_no = encoder.encodeToString(invoice_no.getBytes());

                    context.startActivity(new Intent(context, PDFActivity.class).putExtra("invoice_no",encoded_invoice_no));
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList_catagory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CustomSaleOrdersHistoryBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomSaleOrdersHistoryBinding.bind(itemView);

        }
    }


    private void fetchSaleProduct(String invoice_no)
    {
        sale_all_products = new ArrayList<>();

        Call<String> call = myInterface.fetch_sale_product(invoice_no);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {

                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        rv_sale_product.setVisibility(View.GONE);
                        Toast.makeText(context, "No Product", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        rv_sale_product.setVisibility(View.VISIBLE);
                        sale_all_products.clear();

                        for (int i =0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String product_name = jsonObject.getString("product_name");
                            String price = jsonObject.getString("price");
                            String quantity = jsonObject.getString("quantity");
                            String product_image = img_url+jsonObject.getString("product_image");

                            Sale_All_Product_Model product = new Sale_All_Product_Model(product_name,quantity,price,product_image);
                            sale_all_products.add(product);
                        }

                        rv_sale_product.setAdapter(new SaleAllProductAdapter(context,sale_all_products));
                    }

                } catch (JSONException e) {

                    Toast.makeText(context, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(context, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
