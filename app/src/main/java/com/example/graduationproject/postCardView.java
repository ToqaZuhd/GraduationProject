package com.example.graduationproject;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.example.graduationproject.Model.Post;


import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class postCardView extends RecyclerView.Adapter<postCardView.ViewHolder> {


    private Context context;
    List<Post>posts;
    ImageLoader imgLoader;

    public postCardView(Context context, List<Post>posts){
        this.context=context;
        this.posts=posts;
      }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_post_card_view,
                parent,
                false);

        return new ViewHolder(v);
    }

    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, null, context.getPackageName());

        return drawableResourceId;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CardView cardView = holder.cardView;
        ImageView img = (ImageView) cardView.findViewById(R.id.imageView);
        Glide.with(context).load(posts.get(position).getImageURL()).into(img);


        TextView tvName= cardView.findViewById(R.id.Name);
        Post post=posts.get(position);
        tvName.setText(post.getName());

        TextView tvDate=cardView.findViewById(R.id.Date);
        tvDate.setText(post.getDate());

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


//
//public class postCardView extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post_card_view);
//    }
//}