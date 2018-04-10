package com.example.max.testjson;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.max.testjson.BorrowedFragment.OnListFragmentInteractionListener;

import java.util.List;


public class BorrowedItemRecyclerViewAdapter extends RecyclerView.Adapter<BorrowedItemRecyclerViewAdapter.ViewHolder> {

    private final List<BorrowedItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public BorrowedItemRecyclerViewAdapter(List<BorrowedItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
        holder.mIdView.setText(mValues.get(position).getItemTag());
        holder.mLocationView.setText("Location: "+mValues.get(position).getBorrwedLocation());
        holder.mTimestampView.setText("Borrowed Time: "+mValues.get(position).getBorrowedTimeStamp().substring(0,10));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
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
        public final TextView mIdView;
        public final TextView mLocationView;
        public final TextView mTimestampView;

        public Item mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView)view.findViewById(R.id.id);
            mLocationView = (TextView) view.findViewById(R.id.location);
            mTimestampView = (TextView) view.findViewById(R.id.timestamp);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLocationView.getText() + "'" + mTimestampView.getText() + "'";
        }
    }
}
