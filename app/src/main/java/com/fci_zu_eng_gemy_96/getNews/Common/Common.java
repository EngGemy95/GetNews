package com.fci_zu_eng_gemy_96.getNews.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fci_zu_eng_gemy_96.getNews.Interface.NewsApiService;
import com.fci_zu_eng_gemy_96.getNews.Remote.RetrofitClient;

public class Common {
    public static final String BASE_URL = "https://newsapi.org/";
    public static final String API_KEY = "42fb3c945c3d458da7284cb258889360";

    public static NewsApiService getNewsServices(){
        return RetrofitClient.getRetrofitClient(BASE_URL).create(NewsApiService.class);
    }

    public static String getAPIUrl(String source , String apiKEY){
        StringBuilder apiUrl = new StringBuilder("http://newsapi.org/v2/top-headlines?sources=");
        return apiUrl.append(source.trim())
                .append("&apiKey=")
                .append(apiKEY.trim())
                .toString();
    }
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}