package com.example.graduationproject.CarProvider;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.example.graduationproject.Model.Post;
import com.example.graduationproject.Model.Reservation;
import com.example.graduationproject.R;


import java.util.List;

public class reservationsAdapter extends RecyclerView.Adapter<reservationsAdapter.ViewHolder> {


    private Context context;
    List<Reservation>reservations;


    public reservationsAdapter(Context context, List<Reservation> reservations) {
        this.context = context;
        this.reservations = reservations;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_card_view,
                parent,
                false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CardView cardView = holder.cardView;
        Reservation reservation=reservations.get(position);

        TextView carID= cardView.findViewById(R.id.car_number);
        carID.setText("رقم السيارة : "+reservation.getCarID()+"");

        TextView carType= cardView.findViewById(R.id.car_type);
        carType.setText("نوع السيارة : "+reservation.getCarType());

        TextView rentDate= cardView.findViewById(R.id.rentDate);
        rentDate.setText("تاريخ حجز السيارة : "+reservation.getReservationStartDate()+"");

        TextView endDate= cardView.findViewById(R.id.endDate);
        endDate.setText("تاريخ انتهاء الحجز : "+reservation.getReservationEndDate()+"");

        TextView PassengerName= cardView.findViewById(R.id.passengerName);
        PassengerName.setText("اسم المسافر : "+reservation.getPassengerName());

        TextView passengerMobile= cardView.findViewById(R.id.passengerMobile);
        passengerMobile.setText("رقم المسافر : "+reservation.getPassengerPhoneNumber());




    }




    @Override
    public int getItemCount() {
        return reservations.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;

        }

    }
}

