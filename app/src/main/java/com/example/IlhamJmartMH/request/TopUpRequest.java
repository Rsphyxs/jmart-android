package com.example.IlhamJmartMH.request;


import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request yang akan menambahkan balance dari user
 * @author Muhammad Ilham M S
 * @version 16 Desember 2021
 */
public class TopUpRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:813/account/%d/topUp";
    private final Map<String, String> params;

    /**
     * Constructor yang akan melakukan request POST untuk menambahkan balance user
     * @param balance jumlah balance yang akan ditambahkan kedalam balance user
     * @param id id dari akun yang balancenya akan ditambahkan
     * @param listener respone berhasil dari backend
     * @param errorListener respone gagal dari backend
     */
    public TopUpRequest(double balance, int id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(URL_FORMAT, id), listener, errorListener);
        params = new HashMap<>();
        params.put("balance", String.valueOf(balance));
    }

    public Map<String, String> getParams(){
        return params;
    }
}
