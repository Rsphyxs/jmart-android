package com.example.IlhamJmartMH.model;

import java.util.Date;

/**
 * Model untuk tipe model Invoice
 * @author Mario Claudius
 * @version 16 Desember 2021
 */
public abstract class Invoice extends Serializable{
    public enum Status
    {
        WAITING_CONFIRMATION,
        CANCELLED,
        DELIVERED,
        ON_PROGRESS,
        ON_DELIVERY,
        COMPLAINT,
        FINISHED,
        FAILED
    }

    enum Rating
    {
        NONE,
        BAD,
        NEUTRAL,
        GOOD
    }

    public Date date;
    public int buyerId;
    public int productId;
    public int complaintId;
    public Rating rating;
}
