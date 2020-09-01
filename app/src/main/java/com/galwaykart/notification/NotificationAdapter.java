package com.galwaykart.notification;

import android.content.Context;

import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galwaykart.Legal.CallWebUrlActivity;
import com.galwaykart.R;
import com.galwaykart.essentialClass.CommonFun;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationModel> nfModelList;
    private Context context;
    String st_url="";
    boolean is_url_contain;
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView date_tv, title_tv,description_tv,tv_click_more;
        public LinearLayout main_row_lay;
         public View layout;

        public ViewHolder(View view) {
            super(view);
            layout = view;
            title_tv = view.findViewById(R.id.title_tv);
            description_tv = view.findViewById(R.id.description_tv);
            date_tv = view.findViewById(R.id.date_tv);
            main_row_lay = view.findViewById(R.id.main_row_lay);
            tv_click_more=view.findViewById(R.id.tv_click_more);
        }
    }

    public NotificationAdapter(Context context, List<NotificationModel> nfModelList) {
        this.context = context;
        this.nfModelList = nfModelList;
    }


    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recycler_view_row_notification, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        viewHolder.date_tv.setText(nfModelList.get(i).getCreated());
        viewHolder.title_tv.setText(Html.fromHtml(nfModelList.get(i).getTitle()));

        viewHolder.tv_click_more.setVisibility(View.GONE);
        String notice_text=(nfModelList.get(i).getMessage());

        is_url_contain=false;
        st_url="";
        String msg_description=nfModelList.get(i).getMessage();
//        final String URL_REGEX = "^((http|https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
//
//        Pattern p = Pattern.compile(URL_REGEX);
//        Matcher m = p.matcher(msg_description);//replace with string to compare
//        if(m.find()) {
//            is_url_contain=true;
//        }

        if(msg_description.contains("http:")||msg_description.contains("https:"))
            is_url_contain=true;

    /**
     * if url contain, remove url from text and show click more button
     * Ankesh Kumar
     * Aug 1, 2020
     */
        if(is_url_contain==true){

            st_url= CommonFun.extractUrls(msg_description);
            viewHolder.tv_click_more.setVisibility(View.VISIBLE);
            msg_description=msg_description.replace(st_url,"");

            viewHolder.tv_click_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(is_url_contain==true)
                        openWebViewActivity(st_url);
                    else {

                    }
                }
            });

        }

         viewHolder.description_tv.setText(Html.fromHtml(msg_description));


//         viewHolder.description_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(is_url_contain==true)
//                    openWebViewActivity();
//                else {
//
//                }
//            }
//        });
    }

    private void openWebViewActivity(String st_pass_url) {
        Log.d("st_url",st_pass_url);

        Intent intent=new Intent(context, CallWebUrlActivity.class);
        intent.putExtra("comefrom",st_pass_url);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return nfModelList.size();
    }

 }
