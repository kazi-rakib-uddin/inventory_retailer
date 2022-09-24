package com.example.InventoryRetailer.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.InventoryRetailer.Model.OrderCartModel;
import com.example.InventoryRetailer.Model.SaleCartModel;
import com.example.InventoryRetailer.R;
import com.example.InventoryRetailer.databinding.CustomCartItemBinding;

import java.util.ArrayList;

public class OrderCartAdapter extends RecyclerView.Adapter<OrderCartAdapter.MyViewHolder> {

    ArrayList<OrderCartModel> orderCartModels;
    Context context;
    btnRemoveInterface btnRemoveInterface;
    public static int total=0;

    public OrderCartAdapter(Context context, ArrayList<OrderCartModel> orderCartModels, btnRemoveInterface btnRemoveInterface) {
        this.orderCartModels = orderCartModels;
        this.context=context;
        this.btnRemoveInterface=btnRemoveInterface;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_order_cart_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.productName.setText(orderCartModels.get(position).getProduct_name());
        //holder.binding.qrCodeNo.setText("QR Code No : "+orderCartModels.get(position).getQr_code_no());
        holder.binding.price.setText("₹ "+orderCartModels.get(position).getPrice());
        holder.binding.quantity.setText("QTY : "+orderCartModels.get(position).getQuantity());



        Glide.with(context)
                .load(orderCartModels.get(position).getImage())
                .into(holder.binding.image);


      holder.binding.btnRemove.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              new AlertDialog.Builder(context)
                      .setTitle("Delete entry")
                      .setMessage("Are you sure you want to delete this product?")

                      .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int which) {

                              btnRemoveInterface.removeProduct(orderCartModels.get(position).getQr_code_no());
                          }
                      })

                      .setNegativeButton(android.R.string.no, null)
                      .setIcon(android.R.drawable.ic_dialog_alert)
                      .show();


          }
      });


    }

    public interface btnRemoveInterface
    {
        void removeProduct(String qrCodeNo);
    }

    @Override
    public int getItemCount() {
        return orderCartModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CustomCartItemBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomCartItemBinding.bind(itemView);

        }
    }
}
