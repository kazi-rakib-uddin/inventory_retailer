package com.example.InventoryRetailer.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.InventoryRetailer.ApiClient.ApiClient;
import com.example.InventoryRetailer.Interface.MyInterface;
import com.example.InventoryRetailer.Model.OrderCartModel;
import com.example.InventoryRetailer.Model.Order_History_Model;
import com.example.InventoryRetailer.Model.Sale_All_Product_Model;
import com.example.InventoryRetailer.R;
import com.example.InventoryRetailer.databinding.CustomCartItemBinding;
import com.example.InventoryRetailer.databinding.CustomOrdersBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    ArrayList<Order_History_Model> order_history_models;
    ArrayList<Sale_All_Product_Model> sale_all_products;
    Context context;
    RecyclerView rv_sale_product;
    MyInterface myInterface;
    private static final String img_url = "http://tiwaryleather.com/New_Inventory/public/product_pic/";

    public OrderHistoryAdapter(Context context, ArrayList<Order_History_Model> order_history_models) {
        this.order_history_models = order_history_models;
        this.context=context;
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_orders,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.invoiceNo.setText("Invoice No : "+order_history_models.get(position).getInvoice_no());
        holder.binding.status.setText(order_history_models.get(position).getStatus());

        try {
            DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = inputFormatter.parse(order_history_models.get(position).getCreate_date());
            DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
            holder.binding.cDate.setText("Date : "+outputFormatter.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }



        holder.binding.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_sale_product);

                rv_sale_product = dialog.findViewById(R.id.rv_sale_product);

                fetchOrderProduct(order_history_models.get(position).getInvoice_no());

                dialog.show();

            }
        });



    }



    @Override
    public int getItemCount() {
        return order_history_models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CustomOrdersBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomOrdersBinding.bind(itemView);

        }
    }




    private void fetchOrderProduct(String invoice_no)
    {
        sale_all_products = new ArrayList<>();

        Call<String> call = myInterface.fetch_order_product(invoice_no);
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
                            String quantity = jsonObject.getString("quantity");
                            String product_image = img_url+jsonObject.getString("product_image");

                            Sale_All_Product_Model product = new Sale_All_Product_Model(product_name,quantity,"",product_image);
                            sale_all_products.add(product);
                        }

                        rv_sale_product.setAdapter(new OrderAllProductAdapter(context,sale_all_products));
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
