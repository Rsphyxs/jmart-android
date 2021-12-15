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

public class HistoryCardViewAdapter extends RecyclerView.Adapter<HistoryCardViewAdapter.HistoryCardViewViewHolder>{
    private ArrayList<Payment.Record> listRecord = new ArrayList<>();

    public HistoryCardViewAdapter(ArrayList<Payment.Record> listRecord){
        this.listRecord = listRecord;
    }

    @NonNull
    @Override
    public HistoryCardViewAdapter.HistoryCardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_cardview, viewGroup, false);
        return new HistoryCardViewAdapter.HistoryCardViewViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return listRecord.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryCardViewAdapter.HistoryCardViewViewHolder holder, int position) {
        Payment.Record record = listRecord.get(position);
        holder.historyStatus.setText(record.status.toString());
        holder.historyDate.setText(record.date.toString());
    }

    class HistoryCardViewViewHolder extends RecyclerView.ViewHolder {
        TextView historyStatus, historyDate;

        HistoryCardViewViewHolder(View itemView) {
            super(itemView);
            historyStatus = itemView.findViewById(R.id.historyStatus);
            historyDate = itemView.findViewById(R.id.historyDate);
        }
    }
}
