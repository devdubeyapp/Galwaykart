package com.galwaykart.SingleProductView;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.galwaykart.R;

import java.util.List;

/**
 * Created by itsoftware on 04/01/2018.
 */

public class CustomerReviewAdapter extends RecyclerView.Adapter<CustomerReviewAdapter.ViewHolder> {


    private List<DataModelProductReview> mValues;
    Context mContext;
    protected ItemListener mListener;
    View view;

//    private String TAG_title="title";
//    private String TAG_detail="detail";
//    private String TAG_nickname="nickname";
//    private String TAG_rating="rating";

    //Holder holder=null;
   // Context mContext;
    //ArrayList<HashMap<String, String>> arrayList;

    public CustomerReviewAdapter(Context context, List<DataModelProductReview> values) {

        mContext=context;
        mValues=values;


    }

//        class Holder{
//        RatingBar ratingBar;
//        TextView text_title;
//        TextView text_detail;
//        TextView text_nickname;
//    }

    @Override
    public int getItemCount() {

        ////Log.d("All size", String.valueOf(mValues.size()));
        return mValues.size();
    }


    @Override
    public CustomerReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_customer_review, parent, false);

        return new ViewHolder(view);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        RatingBar ratingBar;
        TextView text_title;
        TextView text_detail;
        TextView text_nickname;
        DataModelProductReview item;

        public ViewHolder(View v) {

            super(v);

//            v.setOnClickListener(this);
//            textView_name = (TextView) v.findViewById(R.id.textView_name);
//            textView_price = (TextView) v.findViewById(R.id.textView_price);
//            imageView_product = (ImageView) v.findViewById(R.id.imageView_product);



            ratingBar = (RatingBar) v.findViewById(R.id.rating_customer);
            ratingBar.setNumStars(5);
            text_detail = (TextView) v.findViewById(R.id.text_desc);
            text_title = (TextView) v.findViewById(R.id.text_title);
            text_nickname = (TextView) v.findViewById(R.id.text_nickname);


        }



//        public void setData(DataModel item) {
//            this.item = item;
//
//            textView_name.setText(item.st_tv_product_name);
//            textView_price.setText(item.st_tv_product_price);
//
//
//            Picasso.get()
//                    .load(item.st_image)
//                    .placeholder(R.drawable.imageloading)   // optional
//                    .error(R.drawable.noimage)      // optional
//                    .resize(200, 300)
//
//                    .into(imageView_product);
//
//
//        }


//        @Override
//        public void onClick(View view) {
//            if (mListener != null) {
//                mListener.onItemClick(item);
//            }
//        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final DataModelProductReview dataModel = mValues.get(position);

        final String st_detail=dataModel.getTAG_detail().toString();
        final String st_title=dataModel.getTAG_title().toString();
        final String st_nickname=dataModel.getTAG_nickname().toString();
        final String st_rating=dataModel.getTAG_rating().toString();


        holder.text_detail.setText(st_detail);
        holder.text_nickname.setText(st_nickname);
        holder.text_title.setText(st_title);
        holder.ratingBar.setRating(Float.parseFloat(st_rating));



    }

    private void finishScreen(){

    }


    public interface ItemListener {
        void onItemClick(DataModelProductReview item);
    }


    //    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if(convertView==null){
//
//            convertView = layoutInflater.inflate(R.layout.product_customer_review, null);
//            holder = new Holder();
//
//            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_customer);
//            holder.ratingBar.setNumStars(5);
//            holder.text_detail = (TextView) convertView.findViewById(R.id.text_desc);
//            holder.text_title = (TextView) convertView.findViewById(R.id.text_title);
//            holder.text_nickname = (TextView) convertView.findViewById(R.id.text_nickname);
//
//            convertView.setTag(holder);
//        }
//
//        holder.text_detail.setText(arrayList.get(position).get(TAG_detail).toString());
//        holder.text_nickname.setText(arrayList.get(position).get(TAG_nickname).toString());
//        holder.text_title.setText(arrayList.get(position).get(TAG_title).toString());
//        holder.ratingBar.setRating(Float.parseFloat(arrayList.get(position).get(TAG_rating).toString()));
//
//
//       return  convertView;
//
//    }
}
