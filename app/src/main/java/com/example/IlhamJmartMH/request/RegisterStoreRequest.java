package com.example.IlhamJmartMH.request;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request yang akan mendaftarkan toko dari akun terkait
 * @author Muhammad Ilham M S
 * @version 16 Desember 2021
 */
public class RegisterStoreRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:813/account/%d/registerStore";
    private final Map<String, String> params;

    /**
     * Method RegisterStoreRequest yang akan melakukan request untuk mendaftarkan toko baru
     * @param id sebagai id dari user sebagai pemilik
     * @param storeName sebagai nama dari toko yang didaftarkan
     * @param storeAddress sebagai alamat dari toko yang didaftarkan
     * @param storePhoneNumber sebagai nomor dari toko yang didaftarkan
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public RegisterStoreRequest(int id, String storeName, String storeAddress, String storePhoneNumber, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(URL_FORMAT, id), listener, errorListener);
        params = new HashMap<>();
        params.put("name", storeName);
        params.put("address", storeAddress);
        params.put("phoneNumber", storePhoneNumber);
    }

    public Map<String, String> getParams(){
        return params;
    }
}
