package com.galwaykart.newsnotice;

import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galwaykart.Legal.WebViewActivity;
import com.galwaykart.R;
import com.google.android.material.snackbar.Snackbar;


import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private List<NoticeModel> noticeModels;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView date_tv, news_tv,tv_readmore;
        public ImageView image;
        public LinearLayout main_row_lay;
        public View layout;

        public ViewHolder(View view) {
            super(view);
            layout = view;
            Snackbar snackbar;

            news_tv = view.findViewById(R.id.news_tv);
            image = view.findViewById(R.id.image);
            main_row_lay = view.findViewById(R.id.main_row_lay);
            tv_readmore= view.findViewById(R.id.tv_readmore);
        }
    }

    public NoticeAdapter(Context context, List<NoticeModel> noticeModels) {
        this.context = context;
        this.noticeModels = noticeModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_row_notice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.news_tv.setText(noticeModels.get(position).getTitle());

        holder.tv_readmore.setVisibility(View.GONE);
        if(!noticeModels.get(position).getIdentifier().equals("0"))
        {

            holder.tv_readmore.setVisibility(View.VISIBLE);
            holder.tv_readmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sl_detal_intent = new Intent(context, NoticeDetailActivity.class);
                    sl_detal_intent.putExtra("identifier_link",noticeModels.get(position).getIdentifier());
                    Log.e("identifier_link", noticeModels.get(position).getIdentifier());
                    context.startActivity(sl_detal_intent);
                }
            });

//            holder.news_tv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    {
//                        Intent sl_detal_intent = new Intent(context, NoticeDetailActivity.class);
//                        sl_detal_intent.putExtra("identifier_link",noticeModels.get(position).getIdentifier());
//                        Log.e("identifier_link", noticeModels.get(position).getIdentifier());
//                        context.startActivity(sl_detal_intent);
//                    }
//                }
//            });
        }




    }

    @Override
    public int getItemCount() {
        return noticeModels.size();
    }


}
