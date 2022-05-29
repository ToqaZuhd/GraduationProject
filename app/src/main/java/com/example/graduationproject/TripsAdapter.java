package com.example.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.Model.Trip;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder>  {

    private int[] tripsIDs;
    private int[] NumOfBags;
    private String[] TripsDates;
    private int[] rewardPoints;


    public TripsAdapter(int[] tripsIDs, int[] numOfBags, String[] tripsDates, int[] rewardPoints) {
        this.tripsIDs = tripsIDs;
        NumOfBags = numOfBags;
        TripsDates = tripsDates;
        this.rewardPoints = rewardPoints;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.tripstyle,
                parent,
                false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        Trip trip = new Trip();

        TextView tripID = (TextView)cardView.findViewById(R.id.tripID);
        tripID.setText( " رقم الرحلة : "+tripsIDs[position]);

        TextView numofbag = (TextView)cardView.findViewById(R.id.NumOfBags);
        numofbag.setText(" عدد الحقائب : "+NumOfBags[position] +" حقيبة ");

        TextView date = (TextView)cardView.findViewById(R.id.TripDate);
        date.setText(" تاريخ الرحلة : "+TripsDates[position] );

        TextView reward = (TextView)cardView.findViewById(R.id.rewardPoints);
        reward.setText(" النقاط المكتسبة : "+rewardPoints[position] +" نقاط ");
    }



    @Override
    public int getItemCount() {
        return tripsIDs.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }

    }
}
