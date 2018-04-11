package com.example.max.testjson;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.max.testjson.AvailableItemFragment.OnListFragmentInteractionListener;
import com.example.max.testjson.dummy.DummyContent.DummyItem;

import java.util.List;


public class AvailableItemRecyclerViewAdapter extends RecyclerView.Adapter<AvailableItemRecyclerViewAdapter.ViewHolder> {

    private final List<AvailableItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public AvailableItemRecyclerViewAdapter(List<AvailableItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_availableitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
      //  holder.mIdView.setText(mValues.get(position).getId());
        holder.mLocationView.setText(mValues.get(position).getItemLocation());
        holder.mTypeView.setText(mValues.get(position).getType());

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
        public final TextView mTypeView;
        public AvailableItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mLocationView = (TextView) view.findViewById(R.id.location);
            mTypeView = (TextView) view.findViewById(R.id.type);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + " '" + mTypeView.getText() + " '" + mLocationView.getText() + "'" +  " '";
        }
    }
}
