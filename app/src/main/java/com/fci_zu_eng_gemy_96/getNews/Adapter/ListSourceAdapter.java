package com.fci_zu_eng_gemy_96.getNews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fci_zu_eng_gemy_96.getNews.Common.Common;
import com.fci_zu_eng_gemy_96.getNews.Interface.ItemClickListner;
import com.fci_zu_eng_gemy_96.getNews.ListNews;
import com.fci_zu_eng_gemy_96.getNews.Model.WebSite;
import com.fci_zu_eng_gemy_96.getNews.R;
import com.google.android.gms.ads.InterstitialAd;

import de.hdodenhof.circleimageview.CircleImageView;

class ListSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ItemClickListner itemClickListner;
    TextView source_name;
    CircleImageView source_image;

    public ListSourceViewHolder(@NonNull View itemView) {
        super(itemView);
        source_name = itemView.findViewById(R.id.source_name);
        source_image = itemView.findViewById(R.id.source_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }

    public ItemClickListner getItemClickListner() {
        return itemClickListner;
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}

public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceViewHolder> {

    public Context context;
    public WebSite webSite;


    public ListSourceAdapter(Context context, WebSite webSite) {
        this.context = context;
        this.webSite = webSite;
    }

    @NonNull
    @Override
    public ListSourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.source_layout, parent, false);
        return new ListSourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListSourceViewHolder holder, int position) {


        if(webSite.getSourceList().size()>0) {
            holder.source_name.setText(webSite.getSourceList().get(position).getName());

            holder.setItemClickListner(new ItemClickListner() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (Common.isConnectedToInternet(context)) {
                        Intent intent = new Intent(context, ListNews.class);
                        intent.putExtra("source", webSite.getSourceList().get(position).getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context, "Check Your Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(context, "There is no advertisment until now !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return webSite.getSourceList().size();
    }
}
