package com.example.IlhamJmartMH.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.IlhamJmartMH.model.ProductCategory;

import java.util.HashMap;
import java.util.Map;

/**
 * Request yang akan mendaftarkan produk baru
 * @author Muhammad Ilham M S
 * @version 16 Desember 2021
 */
public class CreateProductRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:813/product/create";
    private final Map<String, String> params;

    /**
     * Method CreateProductRequest yang akan melakukan request untuk mendaftarkan produk baru
     * @param id sebagai id dari akun yang mendaftarkan produk
     * @param nameProduct sebagai nama dari produk
     * @param weightProduct sebagai berat dari produk
     * @param conditionProduct sebagai kondisi dari produk
     * @param priceProduct sebagai harga dari produk
     * @param discountProduct sebagai diskon dari produk
     * @param productCategory sebagai kategori dari produk
     * @param shipmentPlans sebagai rencana pengiriman dari produk
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public CreateProductRequest(int id, String nameProduct, int weightProduct, boolean conditionProduct, double priceProduct, double discountProduct, ProductCategory productCategory, byte shipmentPlans, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,URL, listener, errorListener);
        params = new HashMap<>();
        params.put("accountId", String.valueOf(id));
        params.put("name", nameProduct);
        params.put("weight", String.valueOf(weightProduct));
        params.put("conditionUsed", String.valueOf(conditionProduct));
        params.put("price", String.valueOf(priceProduct));
        params.put("discount", String.valueOf(discountProduct));
        params.put("category", productCategory.toString());
        params.put("shipmentPlans", String.valueOf(shipmentPlans));
    }

    public Map<String, String> getParams(){
        return params;
    }
}
