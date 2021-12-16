package com.example.IlhamJmartMH.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request yang akan meminta data invoice dari backend
 * @author Muhammad Ilham M S
 * @version 16 Desember 2021
 */
public class InvoiceRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:813/payment/%s?%s=%s";
    private final Map<String, String> params;

    /**
     * Method InvoiceRequest akan melakukan request permintaan invoice dari backend
     * @param id sebagai id dari akun
     * @param byAccount yang akan menentukan apakah request dilakukan oleh toko atau oleh user
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public InvoiceRequest(int id, boolean byAccount, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, byAccount ? "getByAccountId" : "getByStoreId", byAccount ? "buyerId" : "storeId", id), listener, errorListener);
        params = new HashMap<>();
        params.put(byAccount ? "buyerId" : "storeId", String.valueOf(id));
    }

    public Map<String, String> getParams(){
        return params;
    }
}
