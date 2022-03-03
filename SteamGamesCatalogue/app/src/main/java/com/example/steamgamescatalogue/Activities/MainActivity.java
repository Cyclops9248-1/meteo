package com.example.steamgamescatalogue.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.steamgamescatalogue.Data.GameRecyclerViewAdapter;
import com.example.steamgamescatalogue.Model.Game;
import com.example.steamgamescatalogue.R;
import com.example.steamgamescatalogue.Util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button prevButton;
    private Button nextButton;
    private GameRecyclerViewAdapter gameRecyclerViewAdapter;
    private List<Game> gameList;
    private RequestQueue requestQueue;
    String chercheTerme = "Counter";
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);

        prevButton = findViewById(R.id.prevPage);
        nextButton = findViewById(R.id.nextPage);
        prevButton.setVisibility(View.INVISIBLE);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gameList = new ArrayList<>();
        Prefs prefs = new Prefs(MainActivity.this);
        String search = prefs.getSearch();
        chercheTerme = search;

        gameList = getGames(chercheTerme);
        Log.e("MainActivity", gameList.size() + "");
        gameRecyclerViewAdapter = new GameRecyclerViewAdapter(this, gameList);
        recyclerView.setAdapter(gameRecyclerViewAdapter);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page > 1){
                    page -= 1;
                    gameList = getGames(chercheTerme);
                    if(page == 1){
                        prevButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page += 1;
                gameList = getGames(chercheTerme);
                prevButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.recherche, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Nom du jeu (ex. Battlefield)");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                chercheTerme = s;
                page = 1;
                prevButton.setVisibility(View.INVISIBLE);
                getGames(chercheTerme);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(getCurrentFocus() != null){
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public List<Game> getGames(String searchTerm){
        gameList.clear();
        String myUrl = "https://steam2.p.rapidapi.com/search/" + searchTerm + "/page/" + page;

        //unknow param error starts here
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.e("MainActivity2", "" + response.length());
                    for(int i = 0; i < response.length(); i++){
                        JSONObject gameObj = response.getJSONObject(i);
                        Game game = new Game();
                        try{
                            game.setAppId(gameObj.getInt("appId"));
                            game.setTitle(gameObj.getString("title"));
                            game.setImgUrl(gameObj.getString("imgUrl"));
                            game.setReleaseDate(gameObj.getString("released"));
                            //game.setReviewSummary(gameObj.getString("reviewSummary"));
                            game.setPrice(gameObj.getString("price"));
                            gameList.add(game);
                        }catch (Exception e){

                        }

                    }
                    Log.e("MainActivity2",gameList.size() + "...");
                    gameRecyclerViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", "Err" + error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("x-rapidapi-host", "steam2.p.rapidapi.com");
                params.put("x-rapidapi-key", "a709b30976msh634c715872b1d69p168e56jsn15ee9596cce7");
                return params;
            }
        };

        requestQueue.add(jsonArrayRequest);
        return gameList;
    }

}