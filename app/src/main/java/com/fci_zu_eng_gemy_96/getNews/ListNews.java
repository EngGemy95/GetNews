package com.fci_zu_eng_gemy_96.getNews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fci_zu_eng_gemy_96.getNews.Adapter.ListNewsAdapter;
import com.fci_zu_eng_gemy_96.getNews.Common.Common;
import com.fci_zu_eng_gemy_96.getNews.Interface.NewsApiService;
import com.fci_zu_eng_gemy_96.getNews.Model.Article;
import com.fci_zu_eng_gemy_96.getNews.Model.News;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListNews extends AppCompatActivity {

    KenBurnsView kenBurnsView;
    AlertDialog dialog;
    NewsApiService newsApiService;
    TextView top_auhor, top_title;
    SwipeRefreshLayout swipeRefreshLayout;
    DiagonalLayout diagonalLayout;

    String source = "", webHotURL = "";

    ListNewsAdapter adapter;
    RecyclerView newsList;
    RecyclerView.LayoutManager layoutManager;

    public InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        newsApiService = Common.getNewsServices();

        dialog = new SpotsDialog.Builder().setContext(this).build();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9768024421342512/3826514777");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d("onAdFailedToLoad",errorCode+" interstitial wasn't loaded ");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        });

        swipeRefreshLayout = findViewById(R.id.newsSwipRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source, true);
            }
        });

        diagonalLayout = findViewById(R.id.diagonalLayout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details = new Intent(getBaseContext(), DetailsActivity.class);
                details.putExtra("webUrl", webHotURL);
                startActivity(details);
            }
        });
        kenBurnsView = findViewById(R.id.top_image);
        top_auhor = findViewById(R.id.top_author);
        top_title = findViewById(R.id.top_title);

        newsList = findViewById(R.id.listNews);
        newsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        newsList.setLayoutManager(layoutManager);


        if (getIntent() != null) {
            source = getIntent().getStringExtra("source");
            if (!source.isEmpty()) {
                loadNews(source, false);
            }
        }

    }

    private void loadNews(String source, boolean isRefreshed) {
        if (!isRefreshed) {
            dialog.show();
            newsApiService.getNewestArticles(Common.getAPIUrl(source, Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            if (response.body() != null) {
                                dialog.dismiss();

                                if (response.body().getArticles().get(0).getUrlToImage() != null) {
                                    try {
                                        Picasso.get().load(response.body().getArticles().get(0).getUrlToImage())
                                                .into(kenBurnsView);
                                    } catch (Exception ex) {
                                        Toast.makeText(ListNews.this, "Something wrong on get url of Banner", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(ListNews.this, "Something wrong on get url of Banner", Toast.LENGTH_SHORT).show();
                                }
                                if (response.body().getArticles().get(0).getTitle() != null
                                        || response.body().getArticles().get(0).getAuthor() != null) {
                                    top_title.setText(response.body().getArticles().get(0).getTitle());
                                    top_auhor.setText(response.body().getArticles().get(0).getAuthor());
                                } else {
                                    Toast.makeText(ListNews.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                }
                                if (response.body().getArticles().get(0).getUrl() != null) {
                                    webHotURL = response.body().getArticles().get(0).getUrl();
                                } else {
                                    Toast.makeText(ListNews.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                }

                                List<Article> removeFirstItem = response.body().getArticles();
                                if (removeFirstItem.size() > 0) {
                                    //because we already load first item in diagonal layout
                                    //so we need remove it
                                    removeFirstItem.remove(0);
                                    adapter = new ListNewsAdapter(removeFirstItem, getBaseContext());
                                    adapter.notifyDataSetChanged();
                                    newsList.setAdapter(adapter);
                                } else {
                                    Toast.makeText(ListNews.this, "There is no data !", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ListNews.this, "There is no advertisment until now !", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {
                        }
                    });

        } else {
            dialog.show();
            newsApiService.getNewestArticles(Common.getAPIUrl(source, Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            if (response.body() != null) {
                                dialog.dismiss();
                                Picasso.get().load(response.body().getArticles().get(0).getUrlToImage())
                                        .into(kenBurnsView);
                                top_title.setText(response.body().getArticles().get(0).getTitle());
                                top_auhor.setText(response.body().getArticles().get(0).getAuthor());

                                webHotURL = response.body().getArticles().get(0).getUrl();

                                List<Article> removeFirstItem = response.body().getArticles();
                                //because we already load first item in diagonal layout
                                //so we need remove it
                                removeFirstItem.remove(0);
                                adapter = new ListNewsAdapter(removeFirstItem, getBaseContext());
                                adapter.notifyDataSetChanged();
                                newsList.setAdapter(adapter);
                            } else {
                                Toast.makeText(ListNews.this, "There is no advertisment until now !", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
