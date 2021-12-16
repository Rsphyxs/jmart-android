package com.example.IlhamJmartMH.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request yang akan mengendalikan proses payment
 * @author Muhammad Ilham M S
 * @version 16 Desember 2021
 */
public class PaymentRequest extends StringRequest {
    private static final String CREATE_URL = "http://10.0.2.2:813/payment/create";
    private static final String SUBMIT_URL = "http://10.0.2.2:813/payment/%d/submit";
    private static final String ACCEPT_URL = "http://10.0.2.2:813/payment/%d/accept";
    public static final String CANCEL_URL = "http://10.0.2.2:813/payment/%d/cancel";

    private final Map<String, String> params;

    /**
     * Method PaymentRequest yang akan membuat payment baru
     * @param buyerId sebagai id dari pembeli
     * @param productId sebagai id dari produk yang dibeli
     * @param productCount sebagai jumlah produk yang dibeli
     * @param shipmentAddress sebagai alamat dari pembeli
     * @param shipmentPlan sebagai tipe pengiriman yang akan digunakan
     * @param storeId sebagai id dari toko pemilik barang tersebut
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public PaymentRequest(int buyerId, int productId, int productCount, String shipmentAddress, byte shipmentPlan, int storeId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, CREATE_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("buyerId", String.valueOf(buyerId));
        params.put("productId", String.valueOf(productId));
        params.put("productCount", String.valueOf(productCount));
        params.put("shipmentAddress", shipmentAddress);
        params.put("shipmentPlan", String.valueOf(shipmentPlan));
        params.put("storeId", String.valueOf(storeId));
    }

    /**
     * Method PaymentRequest yang akan menerima pembelian barang baru
     * @param id sebagai id dari produk yang akan di accept
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public PaymentRequest(int id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(ACCEPT_URL, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
    }

    /**
     * Method PaymentRequest yang akan menolak pembelian barang baru
     * @param id sebagai id dari produk yang akan di accept
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public PaymentRequest(Response.Listener<String> listener ,int id, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(CANCEL_URL, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
    }

    /**
     * Method PaymentRequest yang akan melanjutkan proses pembelian dan memasukan resi
     * @param id sebagai id dari produk yang akan di accept
     * @param receipt sebagai resi dari barang yang dikirimkan
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public PaymentRequest(int id, String receipt, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(SUBMIT_URL, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
        params.put("receipt", receipt);
    }

    public Map<String, String> getParams(){
        return params;
    }
}
