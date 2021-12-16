package com.example.IlhamJmartMH.request;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request yang akan mengambil data user dan juga data produk dari backend
 * @author Muhammad Ilham M S
 * @version 16 Desember 2021
 */
public class RequestFactory {
    public static final String URL_FORMAT_ID = "http://10.0.2.2:813/%s/%d";
    public static final String URL_FORMAT_PAGE = "http://10.0.2.2:813/%s/page?page=%s&pageSize=%s";

    /**
     * Method getById yang akan mengambil detail informasi user berdasarkan id
     * @param parentURI sebagai URI untuk request
     * @param id id akun yang akan diambil informasinya
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public static StringRequest getById(String parentURI, int id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = String.format(URL_FORMAT_ID, parentURI, id);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }

    /**
     * Method getPage yang akan mengambil data produk yang telah terpaginasi
     * @param parentURI sebagai URI untuk request
     * @param page halaman dari data produk yang akan diambil
     * @param pageSize jumlah data per halaman yang akan diambil
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public static StringRequest getPage(String parentURI, int page, int pageSize, Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = String.format(URL_FORMAT_PAGE, parentURI, page, pageSize);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }
}
