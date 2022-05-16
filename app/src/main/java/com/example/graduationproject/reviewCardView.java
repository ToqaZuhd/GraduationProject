package com.example.graduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.example.graduationproject.Model.Post;

import java.util.List;

public class reviewCardView extends RecyclerView.Adapter<reviewCardView.ViewHolder> {

    private Context context;
    List<Post> posts;

    public reviewCardView(Context context, List<Post>posts){
        this.context=context;
        this.posts=posts;
    }

    @Override
    public reviewCardView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_review_card_view,
                parent,
                false);

        return new reviewCardView.ViewHolder(v);
    }



    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, null, context.getPackageName());

        return drawableResourceId;
    }

    @Override
    public void onBindViewHolder(reviewCardView.ViewHolder holder, int position) {

        CardView cardView = holder.cardView;
        ImageView img = (ImageView) cardView.findViewById(R.id.imageView);
        Glide.with(context).load(posts.get(position).getImageURL()).into(img);


        TextView tvName= cardView.findViewById(R.id.Name);
        Post post=posts.get(position);
        tvName.setText(post.getName());

        TextView tvDate=cardView.findViewById(R.id.Date);
        tvDate.setText(post.getDate());

        RatingBar tvRate=cardView.findViewById(R.id.rating_bar_small);
        tvRate.setRating(post.getReview());

        TextView tvPost=cardView.findViewById(R.id.Post);
        tvPost.setText(post.getText());


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;

        }

    }
}