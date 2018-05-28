package com.example.max.testjson;

/**
 * Created by ASUS on 2018/4/20.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.max.testjson.Admin_AvailableItemFragment.OnListFragmentInteractionListener;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;


public class Admin_AvailableItemAdapter extends RecyclerView.Adapter<Admin_AvailableItemAdapter.ViewHolder> implements Filterable{

    private List<AvailableItem> mValues;
    private List<AvailableItem> tempValue;
    private final Admin_AvailableItemFragment.OnListFragmentInteractionListener mListener;

    Admin_AvailableItemAdapter.TestFilter myFilter;

    public Admin_AvailableItemAdapter(List<AvailableItem> items, Admin_AvailableItemFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        tempValue = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_admin_available_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final Admin_AvailableItemAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String content = holder.mItem.getClassification() + " at "+holder.mItem.getItemLocation();
        holder.mContent.setText(content);

        if(((Worker)TestJson.getUser()).inMaintainList(holder.mItem))
            holder.mImage.setVisibility(View.VISIBLE);
        else
            holder.mImage.setVisibility(View.GONE);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }


    public AvailableItem getItem(int position){
        return mValues.get(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContent;
        public AvailableItem mItem;
        public ImageView mImage;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContent = (TextView) view.findViewById(R.id.admin_content);
            mImage = (ImageView) view.findViewById(R.id.maintaining);
        }

        @Override
        public String toString() {
            return super.toString()+mContent.getText();
        }
    }

    @Override
    public Filter getFilter() {

        if (myFilter == null) {
            myFilter = new TestFilter();
        }
        return myFilter;
    }

    class TestFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<AvailableItem> newItemList = new ArrayList<AvailableItem>();
            if (constraint != null && constraint.toString().trim().length() > 0) {
                for (int i = 0; i < tempValue.size(); i++) {
                    String content = tempValue.get(i).getClassification() + " "+tempValue.get(i).getItemLocation();
                    if (content.contains(constraint)) {
                        newItemList.add(tempValue.get(i));

                    }
                }

            }else {
                newItemList = tempValue;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.count = newItemList.size();
            filterResults.values = newItemList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mValues = (List)results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                mValues = new ArrayList(){};
                notifyDataSetChanged();
            }

        }

    }



}

