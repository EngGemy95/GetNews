package com.fci_zu_eng_gemy_96.getNews.Interface;

import com.fci_zu_eng_gemy_96.getNews.Common.Common;
import com.fci_zu_eng_gemy_96.getNews.Model.News;
import com.fci_zu_eng_gemy_96.getNews.Model.WebSite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface NewsApiService {

    @GET("v2/sources?apiKey="+ Common.API_KEY)
    Call<WebSite> getSources();

    @GET
    Call<News> getNewestArticles(@Url String url);


}
