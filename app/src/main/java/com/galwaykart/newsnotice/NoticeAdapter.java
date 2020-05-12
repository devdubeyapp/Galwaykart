package com.galwaykart.newsnotice;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.galwaykart.Legal.LegalAboutActivity;
import com.galwaykart.Legal.WebViewActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;
import com.galwaykart.productList.ProductListActivity;
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
        if(!noticeModels.get(position).getCat_id().equalsIgnoreCase("")
                || !noticeModels.get(position).getSku().equalsIgnoreCase("")
                || !noticeModels.get(position).getIdentifier().equalsIgnoreCase(""))
            {

                holder.tv_readmore.setVisibility(View.VISIBLE);
                holder.tv_readmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!noticeModels.get(position).getCat_id().equalsIgnoreCase(""))
                        {
                            //////Log.d("clicked", banner_image_catid[position]);
                            SharedPreferences pref;
                            pref= context.getSharedPreferences("GalwayKart", context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("selected_id",noticeModels.get(position).getCat_id());
                            editor.putString("selected_name","");
                            editor.commit();

                            Intent intent = new Intent(context, ProductListActivity.class);
                            intent.putExtra("onback","home");
                            context.startActivity(intent);
                            ((Activity)context).finish();

                        }
                        else if(!noticeModels.get(position).getSku().equalsIgnoreCase(""))
                        {
                            //////Log.d("clicked", banner_image_sku[position]);
                            SharedPreferences pref;
                            pref= CommonFun.getPreferences(context);
                            SharedPreferences.Editor editor=pref.edit();
                            editor.putString("showitemsku",noticeModels.get(position).getSku());
                            editor.commit();

                            Intent intent=new Intent(context, com.galwaykart.SingleProductView.MainActivity.class);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }

                        else if(!noticeModels.get(position).getIdentifier().equalsIgnoreCase(""))
                        {
                            Intent intent=new Intent(context,WebViewActivity.class);
                            intent.putExtra("comefrom","noticeModels.get(position).getIdentifier()");
                            context.startActivity(intent);
                            ((Activity)context).finish();

                        }

                        else
                        {
                            //////Log.d("clicked", "");
                        }

                      /*  Intent sl_detal_intent = new Intent(context, NoticeDetailActivity.class);
                        sl_detal_intent.putExtra("identifier_link",noticeModels.get(position).getIdentifier());
                        Log.e("identifier_link", noticeModels.get(position).getIdentifier());
                        context.startActivity(sl_detal_intent);*/
                    }
                });
            }




    }

    @Override
    public int getItemCount() {
        return noticeModels.size();
    }


}
