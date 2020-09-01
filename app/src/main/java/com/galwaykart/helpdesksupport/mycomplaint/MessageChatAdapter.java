package com.galwaykart.helpdesksupport.mycomplaint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galwaykart.R;

import java.util.List;


public class MessageChatAdapter extends RecyclerView.Adapter<MessageChatAdapter.ViewHolder> {

    private List<ComplModel> complModels;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_admin_remark_label, tv_admin_remark_label1, tv_remark, tv_admin_remark;
        public LinearLayout main_row_lay;
        public View layout;


        public ViewHolder(View view) {
            super(view);
            layout = view;
            tv_admin_remark_label = view.findViewById(R.id.tv_admin_remark_label);
            tv_admin_remark_label1 = view.findViewById(R.id.tv_admin_remark_label1);
            tv_admin_remark = view.findViewById(R.id.tv_admin_remark);
            main_row_lay = view.findViewById(R.id.main_row_lay);


        }
    }

    public MessageChatAdapter(Context context, List<ComplModel> complModels) {
        this.context = context;
        this.complModels = complModels;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_cust_and_cc_team_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        if(complModels.get(position).getName().equalsIgnoreCase("admin"))
        {
            holder.tv_admin_remark.setText(complModels.get(position).getMessage_chat());

            /*if(position>=0)
            {
                holder.tv_admin_remark.setText(complModels.get(position).getMessage_chat());
            }
            else
            {
                holder.tv_admin_remark_label.setVisibility(View.GONE);
                holder.tv_admin_remark_label1.setVisibility(View.GONE);
            }*/

        }


    }

    @Override
    public int getItemCount() {
        return complModels.size();
    }




}
