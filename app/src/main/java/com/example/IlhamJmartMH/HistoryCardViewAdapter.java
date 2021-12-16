package com.example.IlhamJmartMH;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.IlhamJmartMH.model.Payment;

import java.util.ArrayList;

/**
 * HistoryCardViewAdapter sebagai Adapter dari History CardView
 * @author Muhammad Ilham M S
 * @version 16 Desember 2021
 */
public class HistoryCardViewAdapter extends RecyclerView.Adapter<HistoryCardViewAdapter.HistoryCardViewViewHolder>{
    private ArrayList<Payment.Record> listRecord = new ArrayList<>();

    /**
     * Method HistoryCardCiewAdapter
     * @param listRecord sebagai parameter dari ArrayList listRecord
     */
    public HistoryCardViewAdapter(ArrayList<Payment.Record> listRecord){
        this.listRecord = listRecord;
    }

    /**
     * Method onCreateViewHolder yang akan menampilkan layout cardview
     * @param viewGroup viewGroup
     * @param viewType viewType
     */
    @NonNull
    @Override
    public HistoryCardViewAdapter.HistoryCardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_cardview, viewGroup, false);
        return new HistoryCardViewAdapter.HistoryCardViewViewHolder(view);
    }

    /**
     * Method getItemCount yang akan memberi nilai return jumlah ArrayList yang akan ditampilkan
     */
    @Override
    public int getItemCount() {
        return listRecord.size();
    }

    /**
     * Method onBindViewHolder
     * @param holder sebagai holder dari cardview
     * @param position sebagai parameter dari posisi cardview
     */
    @Override
    public void onBindViewHolder(@NonNull final HistoryCardViewAdapter.HistoryCardViewViewHolder holder, int position) {
        Payment.Record record = listRecord.get(position);
        holder.historyStatus.setText(record.status.toString());
        holder.historyDate.setText(record.date.toString());
    }

    /**
     * HistoryCardViewViewHolder sebagai gilder dari CardView
     */
    class HistoryCardViewViewHolder extends RecyclerView.ViewHolder {
        TextView historyStatus, historyDate;

        HistoryCardViewViewHolder(View itemView) {
            super(itemView);
            historyStatus = itemView.findViewById(R.id.historyStatus);
            historyDate = itemView.findViewById(R.id.historyDate);
        }
    }
}
