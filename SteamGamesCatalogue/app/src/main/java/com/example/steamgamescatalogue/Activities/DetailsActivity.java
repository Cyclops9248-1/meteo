package com.example.steamgamescatalogue.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.steamgamescatalogue.Model.Game;
import com.example.steamgamescatalogue.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    private Game game;
    private TextView gameTitleIdDetails, gameReleaseIdDetails,gameDescriptionIdDetails;
    private ImageView gameImageIdDetails;
    private RequestQueue queue;
    int gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        queue = Volley.newRequestQueue(this);
        game = (Game) getIntent().getSerializableExtra("game");
        gameId = game.getAppId();
        gameTitleIdDetails = findViewById(R.id.gameTitleIdDetails);
        gameDescriptionIdDetails = findViewById(R.id.gameDescriptionIdDetails);
        gameReleaseIdDetails = findViewById(R.id.gameReleaseIdDetails);
        gameImageIdDetails = findViewById(R.id.gameImageIdDetails);
        getGameDetails(gameId);
    }

    private void getGameDetails(int id){
        String myUrl = "https://steam2.p.rapidapi.com/appDetail/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    gameTitleIdDetails.setText(response.getString("title"));
                    gameReleaseIdDetails.setText("Released: " + response.getString("released"));
                    gameDescriptionIdDetails.setText(response.getString("description"));
                    Log.e("imgurl", response.getString("imgUrl"));
                    Picasso.get().load(response.getString("imgUrl")).fit().into(gameImageIdDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", "Err" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-rapidapi-host", "steam2.p.rapidapi.com");
                params.put("x-rapidapi-key", "a709b30976msh634c715872b1d69p168e56jsn15ee9596cce7");
                return params;
            }
        };

        queue.add(jsonObjectRequest);
    }

}
