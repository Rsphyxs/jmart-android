package com.example.IlhamJmartMH.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request yang akan memasukan data user baru kedalam backend
 * @author Muhammad Ilham M S
 * @version 16 Desember 2021
 */
public class RegisterRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:813/account/register";
    private final Map<String,String> params;

    /**
     * Method RegisterRequset yang akan melakukan request kepada backend untuk memasukan data baru
     * @param name nama dari akun baru
     * @param email email dari akun baru
     * @param password password dari akun baru
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public RegisterRequest(String name, String email, String password, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
    }

    /**
     * Method yang akan mengambil parameter yang diberukan
     */
    public Map<String, String> getParams(){
        return params;
    }
}
