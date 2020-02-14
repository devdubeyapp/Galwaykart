package com.galwaykart.testimonial;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.galwaykart.R;
import com.squareup.picasso.Picasso;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TestimonialAdapter extends RecyclerView.Adapter<TestimonialAdapter.ViewHolder> {


    private List<TestimonialModel> teModelList;
    private Context context;
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView date_tv, name_tv, title_tv,description_tv;
        public CircleImageView author_image;
        public LinearLayout main_row_lay;
        public View layout;

        public ViewHolder(View view) {
            super(view);
            layout = view;
            name_tv = view.findViewById(R.id.name_tv);
            title_tv = view.findViewById(R.id.title_tv);
            description_tv = view.findViewById(R.id.description_tv);
            date_tv = view.findViewById(R.id.date_tv);
            author_image = view.findViewById(R.id.author_image);
            main_row_lay = view.findViewById(R.id.main_row_lay);
        }
    }

    public TestimonialAdapter(Context context, List<TestimonialModel> teModelList) {
        this.context = context;
        this.teModelList = teModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_row_testimonial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name_tv.setText(teModelList.get(position).getAuthor());
        holder.date_tv.setText(teModelList.get(position).getCreation_time());
        holder.title_tv.setText(Html.fromHtml(teModelList.get(position).getTitle()));
        holder.description_tv.setText(Html.fromHtml(teModelList.get(position).getTestimonial_content()));

        if(teModelList.get(position).getImage()!=null && !teModelList.get(position).getImage().equals("")) {
            Picasso.get()
                    .load(teModelList.get(position).getImage())
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)      // optional
                    //.resize(300, 300)
                    //.rotate(90)                             // optional
                    //.networkPolicy(NetworkPolicy.)
                    .into(holder.author_image);
        }
        else
        {
            Picasso.get()
                    .load(R.drawable.noimage)
                    .placeholder(R.drawable.imageloading)   // optional
                    .error(R.drawable.noimage)      // optional
                   // .resize(300, 300)
                    //.rotate(90)                             // optional
                    //.networkPolicy(NetworkPolicy.)
                    .into(holder.author_image);
        }

    }

    @Override
    public int getItemCount() {
        return teModelList.size();
    }





}


