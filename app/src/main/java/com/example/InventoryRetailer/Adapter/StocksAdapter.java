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
import com.example.InventoryRetailer.Model.SaleCartModel;
import com.example.InventoryRetailer.Model.StocksModel;
import com.example.InventoryRetailer.R;
import com.example.InventoryRetailer.databinding.CustomCartItemBinding;
import com.example.InventoryRetailer.databinding.CustomStocksItemBinding;

import java.util.ArrayList;
import java.util.List;

public class StocksAdapter extends RecyclerView.Adapter<StocksAdapter.MyViewHolder> {

    ArrayList<StocksModel> arrayList_catagory;
    Context context;
    public static int total=0;
    List<StocksModel> filteredList;

    public StocksAdapter(Context context, ArrayList<StocksModel> arrayList_catagory) {
        this.arrayList_catagory = arrayList_catagory;
        this.context=context;
        filteredList = arrayList_catagory;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_stocks_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.productName.setText(arrayList_catagory.get(position).getProduct_name());
        holder.binding.quantity.setText("QTY : "+arrayList_catagory.get(position).getQuantity());



        Glide.with(context)
                .load(arrayList_catagory.get(position).getImage())
                .into(holder.binding.image);




    }



    @Override
    public int getItemCount() {
        return arrayList_catagory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CustomStocksItemBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomStocksItemBinding.bind(itemView);

        }
    }

    public void filterList(ArrayList<StocksModel> filterdNames) {
        this.arrayList_catagory = filterdNames;
        notifyDataSetChanged();
    }
}
