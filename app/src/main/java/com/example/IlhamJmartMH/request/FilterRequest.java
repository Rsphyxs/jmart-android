package com.example.IlhamJmartMH.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.IlhamJmartMH.model.ProductCategory;

import java.util.HashMap;
import java.util.Map;

/**
 * Request yang akan melakukan filter terhadap daftar produk
 * @author Muhammad Ilham M S
 * @version 16 Desember 2021
 */
public class FilterRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:813/product/getFiltered?page=%s&pageSize=%s&accountId=%s&search=%s&minPrice=%s&maxPrice=%s&category=%s";
    private final Map<String, String> params;

    /**
     * Method FilterRequest yang dilakukan ketika tidak ada data yang kosong
     * @param page sebagai halaman dari data yang akan ditampilkan
     * @param pageSize sebagai jumlah data yang akan ditampilkan perhalaman
     * @param accountId sebagai id dari akun
     * @param search sebagai parameter text yang disearch
     * @param minPrice sebagai harga batas bawah
     * @param maxPrice sebagai harga batas atas
     * @param category sebagai category
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public FilterRequest(int page, int pageSize, int accountId, String search, int minPrice, int maxPrice, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, pageSize, accountId, search, minPrice, maxPrice, category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", String.valueOf(minPrice));
        params.put("maxPrice", String.valueOf(maxPrice));
        params.put("category", category.toString());
    }

    /**
     * Method FilterRequest yang dilakukan ketika data harga batas bawah dan atas kosong
     * @param page sebagai halaman dari data yang akan ditampilkan
     * @param pageSize sebagai jumlah data yang akan ditampilkan perhalaman
     * @param accountId sebagai id dari akun
     * @param search sebagai parameter text yang disearch
     * @param category sebagai category
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public FilterRequest(int page, int pageSize, int accountId, String search, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, pageSize, accountId, search, "", "", category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", "");
        params.put("maxPrice", "");
        params.put("category", category.toString());
    }

    /**
     * Method FilterRequest yang dilakukan ketika data harga batas atas kosong
     * @param page sebagai halaman dari data yang akan ditampilkan
     * @param pageSize sebagai jumlah data yang akan ditampilkan perhalaman
     * @param accountId sebagai id dari akun
     * @param search sebagai parameter text yang disearch
     * @param category sebagai category
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public FilterRequest(int page, int pageSize, int accountId, int minPrice, String search, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, pageSize, accountId, search, minPrice, "", category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", String.valueOf(minPrice));
        params.put("maxPrice", "");
        params.put("category", category.toString());
    }

    /**
     * Method FilterRequest yang dilakukan ketika data harga batas bawah kosong
     * @param page sebagai halaman dari data yang akan ditampilkan
     * @param pageSize sebagai jumlah data yang akan ditampilkan perhalaman
     * @param accountId sebagai id dari akun
     * @param search sebagai parameter text yang disearch
     * @param category sebagai category
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public FilterRequest(String search, int page, int pageSize,int accountId, int maxPrice, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, pageSize, accountId, search, "", maxPrice, category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", "");
        params.put("maxPrice", String.valueOf(maxPrice));
        params.put("category", category.toString());
    }

    public Map<String, String> getParams(){
        return params;
    }
}
