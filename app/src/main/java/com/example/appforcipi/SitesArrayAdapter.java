package com.example.appforcipi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class SitesArrayAdapter extends RecyclerView.Adapter<SitesArrayAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<SitesBean> homeListDatas;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return homeListDatas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(View view){
            super(view);
        }
    }
}

