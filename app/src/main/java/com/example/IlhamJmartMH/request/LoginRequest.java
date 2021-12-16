package com.example.IlhamJmartMH.request;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.IlhamJmartMH.LoginActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Request yang akan mencocokan kredensial user saat login
 * @author Muhammad Ilham M S
 * @version 16 Desember 2021
 */
public class LoginRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:813/account/login";
    private final Map<String,String> params;

    /**
     * Method LoginRequest yang akan menolak pembelian barang baru
     * @param email sebagai email input user
     * @param password sebagai password input user
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public LoginRequest(String email, String password, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
    }


    public Map<String, String> getParams(){
        return params;
    }

}
