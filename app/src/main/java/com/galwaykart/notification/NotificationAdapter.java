package com.galwaykart.notification;

import android.content.Context;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galwaykart.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationModel> nfModelList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView date_tv, title_tv,description_tv;
        public LinearLayout main_row_lay;
         public View layout;

        public ViewHolder(View view) {
            super(view);
            layout = view;
            title_tv = view.findViewById(R.id.title_tv);
            description_tv = view.findViewById(R.id.description_tv);
            date_tv = view.findViewById(R.id.date_tv);
            main_row_lay = view.findViewById(R.id.main_row_lay);
        }
    }

    public NotificationAdapter(Context context, List<NotificationModel> nfModelList) {
        this.context = context;
        this.nfModelList = nfModelList;
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
        viewHolder.description_tv.setText(Html.fromHtml(nfModelList.get(i).getMessage()));
    }

    @Override
    public int getItemCount() {
        return nfModelList.size();
    }
}
