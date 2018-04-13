package com.example.max.testjson;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Picasso.with(this.context).load(mValues.get(position).getImageURL());
        holder.mContentView.setText(mValues.get(position).getBorrowedLocation() + " "+ mValues.get(position).getBorrowedTimeStamp());

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
        public final TextView mContentView;

        public BorrowedItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView)view.findViewById(R.id.imageView);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
