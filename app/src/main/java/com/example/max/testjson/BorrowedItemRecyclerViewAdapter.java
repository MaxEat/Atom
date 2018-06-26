package com.example.max.testjson;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.max.testjson.BorrowedFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;
import java.util.List;


public class BorrowedItemRecyclerViewAdapter extends RecyclerView.Adapter<BorrowedItemRecyclerViewAdapter.ViewHolder> {

    private final List<BorrowedItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;


    public BorrowedItemRecyclerViewAdapter(List<BorrowedItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void setContext(Context aContext) {
        context = aContext;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mClassification.setText(holder.mItem.getClassification());
        holder.mLocation.setText(holder.mItem.getBorrowedLocation());
        holder.mDate.setText(holder.mItem.getBorrowedTimeStamp());
        String pictureUrl = TestJson.pictureMap.get(holder.mItem.getClassification());
        Log.i("location", holder.mItem.getBorrowedLocation());
        Log.i("classification", holder.mItem.getClassification());
        Picasso.with(context).load(pictureUrl).fit().into(holder.mImage);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImage;
     //   public final TextView mContentView;
        public final TextView mDate;
        public final TextView mLocation;
        public final TextView mClassification;

        public BorrowedItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView)view.findViewById(R.id.imageView);
            mDate = (TextView)view.findViewById(R.id.borrow_date);
            mLocation = (TextView)view.findViewById(R.id.borrow_location);
            mClassification = (TextView)view.findViewById(R.id.classfication);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mClassification + " '" + mDate.getText() + " '" + mLocation.getText();
        }
    }
}
