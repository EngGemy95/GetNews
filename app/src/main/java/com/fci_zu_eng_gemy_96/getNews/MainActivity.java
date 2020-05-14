package com.fci_zu_eng_gemy_96.getNews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fci_zu_eng_gemy_96.getNews.Adapter.ListSourceAdapter;
import com.fci_zu_eng_gemy_96.getNews.Common.Common;
import com.fci_zu_eng_gemy_96.getNews.Interface.NewsApiService;
import com.fci_zu_eng_gemy_96.getNews.Model.WebSite;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView_website;
    RecyclerView.LayoutManager layoutManager;
    NewsApiService newsApiService;
    ListSourceAdapter listSourceAdapter;
    AlertDialog dialog;
    SwipeRefreshLayout swipeRefresh;

    private AdView mAdView;
/*
    private InterstitialAd mInterstitialAd;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            MobileAds.initialize(this, "ca-app-pub-9768024421342512~4209658151");
            mAdView = findViewById(R.id.adView1);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            //init cache
            Paper.init(this);

            //init Service
            newsApiService = Common.getNewsServices();

            //init View
            swipeRefresh = findViewById(R.id.swipRefresh);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    if(!Common.isConnectedToInternet(getApplicationContext())){
                        Toast.makeText(getApplicationContext(), "Check Your Internet", Toast.LENGTH_SHORT).show();
                        recyclerView_website.setAdapter(null);
                        swipeRefresh.setRefreshing(false);
                        return;
                    }else {
                        loadNewSources(true);
                    }
                }
            });

            recyclerView_website = findViewById(R.id.list_source);
            recyclerView_website.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView_website.setLayoutManager(layoutManager);

            dialog = new SpotsDialog.Builder().setContext(this).build();

        if(!Common.isConnectedToInternet(this)){
            Toast.makeText(this, "Check Your Internet", Toast.LENGTH_SHORT).show();
            recyclerView_website.setAdapter(null);
            return;
        }else {
            loadNewSources(false);
        }
    }

    private void loadNewSources(boolean isRefreshed) {

        if (!isRefreshed) {Paper.book().destroy();
            String cache = Paper.book().read("cache");

            Log.d("caaaaaaaach",cache+" 123");
            if (cache != null && !cache.isEmpty() && !cache.equals("null")) { // if have cache
                WebSite newsData = new Gson().fromJson(cache, WebSite.class);
                listSourceAdapter = new ListSourceAdapter(this, newsData);
                listSourceAdapter.notifyDataSetChanged();
                recyclerView_website.setAdapter(listSourceAdapter);
            }
            else {  // if not have cache
                dialog.show();
                newsApiService.getSources().enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                        Log.d("boooooody",new Gson().toJson(response.body(),WebSite.class));
                        listSourceAdapter = new ListSourceAdapter(getBaseContext(), response.body());
                        listSourceAdapter.notifyDataSetChanged();
                        recyclerView_website.setAdapter(listSourceAdapter);

                        Paper.book().write("cache", new Gson().toJson(response.body()));

                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<WebSite> call, Throwable t) {

                    }
                });
            }
        } else { // if from swip to refresh
            swipeRefresh.setRefreshing(true);
            newsApiService.getSources().enqueue(new Callback<WebSite>() {
                @Override
                public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                    listSourceAdapter = new ListSourceAdapter(getBaseContext(), response.body());
                    listSourceAdapter.notifyDataSetChanged();
                    recyclerView_website.setAdapter(listSourceAdapter);

                    //save to cache
                    Paper.book().write("cache", new Gson().toJson(response.body()));
                    //dismiss refresh progressing
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<WebSite> call, Throwable t) {

                }
            });
        }
    }
}
