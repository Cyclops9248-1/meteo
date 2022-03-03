package com.example.steamgamescatalogue.Data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.steamgamescatalogue.Activities.DetailsActivity;
import com.example.steamgamescatalogue.Model.Game;
import com.example.steamgamescatalogue.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GameRecyclerViewAdapter extends RecyclerView.Adapter<GameRecyclerViewAdapter.ViewHolder>{

    private Context context;
    private List<Game> gameList;

    public GameRecyclerViewAdapter(Context context, List<Game> gameList){
        this.context = context;
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public GameRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.film, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull GameRecyclerViewAdapter.ViewHolder holder, int position){
        Game game = gameList.get(position);
        String posterLink = game.getImgUrl();
        holder.gameTitleTxt.setText(game.getTitle());
        holder.priceTxt.setText("Price: " + game.getPrice());

        Picasso.get().load(posterLink).fit()
                .placeholder(android.R.drawable.ic_btn_speak_now)
                .into(holder.gameImageID);
    }

    @Override
    public int getItemCount(){ return gameList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView gameTitleTxt, priceTxt;
        ImageView gameImageID;

        public ViewHolder(@NonNull View itemView, Context ctx){
            super(itemView);
            Log.e("RecyclerView", "5");
            context = ctx;
            gameTitleTxt = itemView.findViewById(R.id.gameTitleTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            gameImageID = itemView.findViewById(R.id.gameImageID);
            Log.e("RecyclerView", "6");
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Game game = gameList.get(getAdapterPosition());
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("game", game);
                    ctx.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }


}
