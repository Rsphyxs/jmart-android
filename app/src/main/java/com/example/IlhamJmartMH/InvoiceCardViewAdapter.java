package com.example.IlhamJmartMH;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.IlhamJmartMH.model.Payment;
import com.example.IlhamJmartMH.model.Product;
import com.example.IlhamJmartMH.request.RequestFactory;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InvoiceCardViewAdapter extends RecyclerView.Adapter<InvoiceCardViewAdapter.InvoiceCardViewViewHolder>{
    private ArrayList<Payment> listPayment = new ArrayList<>();
    private Product product;
    private static final Gson gson = new Gson();

    public InvoiceCardViewAdapter(ArrayList<Payment> list) {
        this.listPayment = list;
    }

    @NonNull
    @Override
    public InvoiceCardViewAdapter.InvoiceCardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_cardview, viewGroup, false);
        return new InvoiceCardViewAdapter.InvoiceCardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InvoiceCardViewViewHolder holder, int position) {
        Payment payment = listPayment.get(position);
        Payment.Record lastRecord = payment.history.get(payment.history.size() -1);
        getProductData(holder, payment);
        holder.noInvoice.setText("#" + (position+1));
        holder.invoiceStatus.setText(lastRecord.status.toString());
        holder.invoiceDate.setText(lastRecord.date.toString());
        holder.invoiceAddress.setText(payment.shipment.address);
        holder.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.historyLayout.getVisibility() == View.GONE){
                    holder.rvHistory.setLayoutManager(new LinearLayoutManager(holder.noInvoice.getContext()));
                    HistoryCardViewAdapter historyCardViewAdapter = new HistoryCardViewAdapter(payment.history);
                    holder.rvHistory.setAdapter(historyCardViewAdapter);
                    holder.expandButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                    TransitionManager.beginDelayedTransition(holder.InvoiceCardview, new AutoTransition());
                    holder.historyLayout.setVisibility(View.VISIBLE);
                }
                else{
                    holder.expandButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    TransitionManager.beginDelayedTransition(holder.InvoiceCardview, new AutoTransition());
                    holder.historyLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPayment.size();
    }

    class InvoiceCardViewViewHolder extends RecyclerView.ViewHolder {
        TextView noInvoice, invoiceName, invoiceStatus, invoiceDate, invoiceAddress, invoiceCost;
        ImageButton expandButton;
        RecyclerView rvHistory;
        LinearLayout historyLayout;
        CardView InvoiceCardview;
        InvoiceCardViewViewHolder(View itemView) {
            super(itemView);
            noInvoice = itemView.findViewById(R.id.noInvoice);
            invoiceName = itemView.findViewById(R.id.invoiceName);
            invoiceStatus = itemView.findViewById(R.id.invoiceStatus);
            invoiceDate = itemView.findViewById(R.id.invoiceDate);
            invoiceAddress = itemView.findViewById(R.id.invoiceAddress);
            invoiceCost = itemView.findViewById(R.id.invoiceCost);
            expandButton = itemView.findViewById(R.id.invoiceExpand);
            rvHistory = itemView.findViewById(R.id.historyCardview);
            historyLayout = itemView.findViewById(R.id.historyDetail);
            InvoiceCardview = itemView.findViewById(R.id.invoiceCardView);
        }
    }

    public void getProductData(InvoiceCardViewAdapter.InvoiceCardViewViewHolder holder, Payment payment){
        Response.Listener<String> listenerProduct = new Response.Listener<String>() {       //listener
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    product = gson.fromJson(response, Product.class);
                    holder.invoiceName.setText(product.getName());
                    holder.invoiceCost.setText("Rp. " + (product.getPrice() * payment.productCount));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {       //errorListener jika tidak terkoneksi ke backend
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(holder.noInvoice.getContext(), "Take Information Failed", Toast.LENGTH_SHORT).show();
            }
        };

        RequestQueue queue = Volley.newRequestQueue(holder.noInvoice.getContext());
        queue.add(RequestFactory.getById("product", payment.productId, listenerProduct, errorListener));        //memasukkan request untuk mengambil informasi payment berdasarkan id
    }

}
