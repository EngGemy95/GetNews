package com.fci_zu_eng_gemy_96.getNews.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fci_zu_eng_gemy_96.getNews.Common.Common;
import com.fci_zu_eng_gemy_96.getNews.Common.ISO8601DateParser;
import com.fci_zu_eng_gemy_96.getNews.DetailsActivity;
import com.fci_zu_eng_gemy_96.getNews.Interface.ItemClickListner;
import com.fci_zu_eng_gemy_96.getNews.ListNews;
import com.fci_zu_eng_gemy_96.getNews.Model.Article;
import com.fci_zu_eng_gemy_96.getNews.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class ListNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ItemClickListner itemClickListner;
    TextView article_title;
    RelativeTimeTextView article_time;
    CircleImageView article_image;

    public ListNewsViewHolder(@NonNull View itemView) {
        super(itemView);

        article_title = itemView.findViewById(R.id.article_title);
        article_time = itemView.findViewById(R.id.article_time);
        article_image = itemView.findViewById(R.id.article_image);

        itemView.setOnClickListener(this);
    }

    public ItemClickListner getItemClickListner() {
        return itemClickListner;
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }
}

public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsViewHolder> {

    private List<Article> articleList;
    private Context context;

    public ListNewsAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.news_layout, parent, false);
        return new ListNewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNewsViewHolder holder, int position) {

        try {
            Picasso.get().load(articleList.get(position).getUrlToImage()).into(holder.article_image);
        }catch (Exception ex) {
                Toast.makeText(context, "Something wrong on images url ", Toast.LENGTH_SHORT).show();
            }
        if (articleList.get(position).getTitle() != null || !(articleList.get(position).getTitle().equals(""))) {
            if (articleList.get(position).getTitle().length() > 65)
                holder.article_title.setText(articleList.get(position).getTitle().substring(0, 65) + "...");
            else
                holder.article_title.setText(articleList.get(position).getTitle());
        }
        if (articleList.get(position).getPublishedAt() != null
                || !(articleList.get(position).getPublishedAt().equals(""))) {
            Date date = null;
            try {
                date = ISO8601DateParser.parse(articleList.get(position).getPublishedAt());
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            //Log.d("ggggg",date.getTime()+"  55");
            try {
                holder.article_time.setReferenceTime(date.getTime());
            } catch (Exception ex) {
                holder.article_time.setText("---------");
            }
        } else {
            holder.article_time.setText("---------");
        }

        holder.setItemClickListner(new ItemClickListner() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (Common.isConnectedToInternet(context)) {
                    Intent details = new Intent(context, DetailsActivity.class);
                    details.putExtra("webUrl", articleList.get(position).getUrl());
                    details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(details);
                } else {
                    Toast.makeText(context, "Check Your Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
