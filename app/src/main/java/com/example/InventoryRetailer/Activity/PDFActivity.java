package com.example.InventoryRetailer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebViewClient;

import com.example.InventoryRetailer.R;
import com.example.InventoryRetailer.databinding.ActivityPDFBinding;

public class PDFActivity extends AppCompatActivity {

    ActivityPDFBinding binding;
    String encoded_invoice_no="";
    final String pdf_url ="http://tiwaryleather.com/New_Inventory/invoice.php?invoice=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPDFBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        encoded_invoice_no = getIntent().getStringExtra("invoice_no");

        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl(pdf_url+encoded_invoice_no);

    }
}