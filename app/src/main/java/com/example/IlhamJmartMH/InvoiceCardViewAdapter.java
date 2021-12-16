package com.example.IlhamJmartMH;

import android.app.Dialog;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.IlhamJmartMH.model.Account;
import com.example.IlhamJmartMH.model.Invoice;
import com.example.IlhamJmartMH.model.Payment;
import com.example.IlhamJmartMH.model.Product;
import com.example.IlhamJmartMH.request.PaymentRequest;
import com.example.IlhamJmartMH.request.RequestFactory;
import com.example.IlhamJmartMH.request.TopUpRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * InvoiceCardViewAdapter sebagai Adapter dari History CardView
 * @author Muhammad Ilham M S
 * @version 16 Desember 2021
 */
public class InvoiceCardViewAdapter extends RecyclerView.Adapter<InvoiceCardViewAdapter.InvoiceCardViewViewHolder> {
    private ArrayList<Payment> listPayment = new ArrayList<>();
    private Product product;
    private static final Gson gson = new Gson();
    private Dialog dialog;
    private Payment.Record lastRecord;
    private Account account = LoginActivity.getLoggedAccount();

    /**
     * Method InvoiceCardCiewAdapter
     * @param list sebagai parameter list
     */
    public InvoiceCardViewAdapter(ArrayList<Payment> list) {
        this.listPayment = list;
    }

    /**
     * Method onCreateViewHolder yang akan menampilkan layout cardview
     * @param viewGroup viewGroup
     * @param viewType viewType
     */
    @NonNull
    @Override
    public InvoiceCardViewAdapter.InvoiceCardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_cardview, viewGroup, false);
        return new InvoiceCardViewAdapter.InvoiceCardViewViewHolder(view);
    }

    /**
     * Method onBindViewHolder
     * @param holder sebagai holder dari cardview
     * @param position sebagai parameter dari posisi cardview
     */
    @Override
    public void onBindViewHolder(@NonNull final InvoiceCardViewViewHolder holder, int position) {
        Payment payment = listPayment.get(position);
        lastRecord = payment.history.get(payment.history.size() - 1);
        getProductData(holder, payment);
        holder.noInvoice.setText("#" + payment.id);
        holder.invoiceStatus.setText(lastRecord.status.toString());
        holder.invoiceDate.setText(lastRecord.date.toString());
        holder.invoiceAddress.setText(payment.shipment.address);
        holder.layoutConfirmation.setVisibility(View.GONE);
        holder.buttonSubmit.setVisibility(View.GONE);
        holder.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.historyLayout.getVisibility() == View.GONE) {
                    holder.rvHistory.setLayoutManager(new LinearLayoutManager(holder.noInvoice.getContext()));
                    HistoryCardViewAdapter historyCardViewAdapter = new HistoryCardViewAdapter(payment.history);
                    holder.rvHistory.setAdapter(historyCardViewAdapter);
                    holder.expandButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                    TransitionManager.beginDelayedTransition(holder.InvoiceCardview, new AutoTransition());
                    holder.historyLayout.setVisibility(View.VISIBLE);
                } else {
                    holder.expandButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    TransitionManager.beginDelayedTransition(holder.InvoiceCardview, new AutoTransition());
                    holder.historyLayout.setVisibility(View.GONE);
                }
            }
        });
        if (InvoiceActivity.isUser) {
            holder.layoutConfirmation.setVisibility(View.GONE);
        } else {
            if (lastRecord.status.equals(Invoice.Status.WAITING_CONFIRMATION)) {
                holder.layoutConfirmation.setVisibility(View.VISIBLE);
            }
        }
        holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listenerAcceptPayment = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean isAccepted = Boolean.valueOf(response);     //response dalam bentuk boolean
                        if (isAccepted) {
                            Toast.makeText(holder.InvoiceCardview.getContext(), "Payment Accepted!", Toast.LENGTH_SHORT).show();
                            payment.history.add(new Payment.Record(Invoice.Status.ON_PROGRESS, "Payment Accepted!"));
                            lastRecord = payment.history.get(payment.history.size() - 1);
                            holder.invoiceStatus.setText(lastRecord.status.toString());
                            holder.layoutConfirmation.setVisibility(View.GONE);
                            holder.buttonSubmit.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(holder.InvoiceCardview.getContext(), "Accept failed, Payment cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Response.ErrorListener errorListenerAcceptPayment = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(holder.InvoiceCardview.getContext(), "Connection Failed!", Toast.LENGTH_SHORT).show();
                    }
                };

                PaymentRequest paymentRequest = new PaymentRequest(payment.id, listenerAcceptPayment, errorListenerAcceptPayment);
                RequestQueue queue = Volley.newRequestQueue(holder.InvoiceCardview.getContext());
                queue.add(paymentRequest);
            }
        });

        holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listenerCancelPayment = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean isAccepted = Boolean.valueOf(response);
                        if (isAccepted) {
                            double price = Double.valueOf(holder.invoiceCost.getText().toString().trim().substring(3));
                            Response.Listener<String> listener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Boolean object = Boolean.valueOf(response);
                                    if (object) {
                                        Toast.makeText(holder.InvoiceCardview.getContext(), "Balance has been returned", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(holder.InvoiceCardview.getContext(), "Balance can't be returned", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            };

                            Response.ErrorListener errorListener = new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(holder.InvoiceCardview.getContext(), "Failed Connection", Toast.LENGTH_SHORT).show();
                                }
                            };
                            TopUpRequest topUpRequest = new TopUpRequest(price, payment.buyerId, listener, errorListener);
                            RequestQueue queue = Volley.newRequestQueue(holder.InvoiceCardview.getContext());
                            queue.add(topUpRequest);
                            Toast.makeText(holder.InvoiceCardview.getContext(), "Payment Cancelled", Toast.LENGTH_SHORT).show();
                            payment.history.add(new Payment.Record(Invoice.Status.CANCELLED, "Payment Cancelled!"));
                            lastRecord = payment.history.get(payment.history.size() - 1);
                            holder.invoiceStatus.setText(lastRecord.status.toString());
                            holder.layoutConfirmation.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(holder.InvoiceCardview.getContext(), "The payment can't be cancelled!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Response.ErrorListener errorListenerCancelPayment = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(holder.InvoiceCardview.getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                    }
                };

                PaymentRequest cancelPaymentRequest = new PaymentRequest(listenerCancelPayment, payment.id, errorListenerCancelPayment);
                RequestQueue queue = Volley.newRequestQueue(holder.InvoiceCardview.getContext());
                queue.add(cancelPaymentRequest);
            }
        });

        holder.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(holder.buttonAccept.getContext());
                dialog.setContentView(R.layout.resi_dialog);
                EditText editRecipt = dialog.findViewById(R.id.editReceipt);
                Button buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
                dialog.show();
                buttonSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dataRecipt = editRecipt.getText().toString().trim();
                        Response.Listener<String> listenerSubmitPayment = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Boolean isSubmitted = Boolean.valueOf(response);
                                if (isSubmitted) {
                                    Toast.makeText(holder.InvoiceCardview.getContext(), "Payment Submitted", Toast.LENGTH_SHORT).show();
                                    payment.history.add(new Payment.Record(Invoice.Status.ON_DELIVERY, "Payment has been Submitted"));
                                    lastRecord = payment.history.get(payment.history.size() - 1);
                                    holder.invoiceStatus.setText(lastRecord.status.toString());
                                    holder.buttonSubmit.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(holder.InvoiceCardview.getContext(), "This payment can't be submitted! " + payment.id, Toast.LENGTH_SHORT).show();
                                }
                            }
                        };

                        Response.ErrorListener errorListenerSubmitPayment = new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(holder.InvoiceCardview.getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                            }
                        };
                        PaymentRequest submitPaymentRequest = new PaymentRequest(payment.id, dataRecipt, listenerSubmitPayment, errorListenerSubmitPayment);
                        RequestQueue queue = Volley.newRequestQueue(holder.InvoiceCardview.getContext());
                        queue.add(submitPaymentRequest);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * Method getItemCount yang akan memberi nilai return jumlah ArrayList yang akan ditampilkan
     */
    @Override
    public int getItemCount() {
        return listPayment.size();
    }

    /**
     * InvoiceCardViewViewHolder sebagai gilder dari CardView
     */
    class InvoiceCardViewViewHolder extends RecyclerView.ViewHolder {
        TextView noInvoice, invoiceName, invoiceStatus, invoiceDate, invoiceAddress, invoiceCost;
        ImageButton expandButton;
        RecyclerView rvHistory;
        LinearLayout historyLayout;
        CardView InvoiceCardview;
        Button buttonAccept, buttonSubmit, buttonCancel;
        LinearLayout layoutConfirmation;

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
            buttonAccept = itemView.findViewById(R.id.buttonAccept);
            buttonSubmit = itemView.findViewById(R.id.buttonSubmit);
            buttonCancel = itemView.findViewById(R.id.buttonCancel);
            layoutConfirmation = itemView.findViewById(R.id.layoutConfirmation);
        }
    }

    /**
     * Method getProductData yang akan mengambil informasi data berdasarkan data payment
     * @param holder holder
     * @param payment payment
     */
    public void getProductData(InvoiceCardViewAdapter.InvoiceCardViewViewHolder holder, Payment payment) {
        Response.Listener<String> listenerProduct = new Response.Listener<String>() {
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

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(holder.noInvoice.getContext(), "Take Information Failed", Toast.LENGTH_SHORT).show();
            }
        };

        RequestQueue queue = Volley.newRequestQueue(holder.noInvoice.getContext());
        queue.add(RequestFactory.getById("product", payment.productId, listenerProduct, errorListener));
    }


}
