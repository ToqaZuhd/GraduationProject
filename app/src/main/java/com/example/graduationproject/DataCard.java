package com.example.graduationproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.graduationproject.Model.PostEmployee;

import java.util.List;

public class DataCard extends RecyclerView.Adapter<DataCard.ViewHolder>{

    List<PostEmployee> employeeList;
    Context context;

    public DataCard(Context context, List<PostEmployee> postEmployees) {
        this.context = context;
        this.employeeList = postEmployees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.data_card,
                parent,
                false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView dateTxtView = (TextView) cardView.findViewById(R.id.dateTxtPass);
        TextView dataTxtView = (TextView) cardView.findViewById(R.id.dataTxtPass);

        PostEmployee postEmployee = employeeList.get(position);
        dateTxtView.setText(postEmployee.getDate());
        dataTxtView.setText(postEmployee.getText());


    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;

        }

    }
}
