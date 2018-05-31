package com.example.max.testjson;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.example.max.testjson.AvailableItemFragment.OnListFragmentInteractionListener;
import com.example.max.testjson.dashboard.Wish_Item_Available_News;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AvailableItemRecyclerViewAdapter extends RecyclerView.Adapter<AvailableItemRecyclerViewAdapter.ViewHolder> implements Filterable{

    private List<AvailableItem> mValues;
    private List<AvailableItem> tempValue;
    private final OnListFragmentInteractionListener mListener;

    TestFilter myFilter;

    public AvailableItemRecyclerViewAdapter(List<AvailableItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        tempValue = items;
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
        String content = holder.mItem.getClassification() + " at "+holder.mItem.getItemLocation();
        holder.mContent.setText(content);


        if(holder.mItem.getStatus().equals("available"))
            holder.mQuantity.setText(Integer.toString(holder.mItem.getQuantity())+" items left");
        else
            holder.mQuantity.setText("Not available");

        if(holder.mItem.getInWishList()){
            holder.mCheckBox.setChecked(true);
        }
        else{
            holder.mCheckBox.setChecked(false);
        }
//        if(((Student)TestJson.getUser()).inWishList(holder.mItem))
//        {
//            holder.mCheckBox.setChecked(true);
//            Log.i("location", holder.mItem.getItemLocation());
//            Log.i("classification", holder.mItem.getClassification());
//        }
        ((Student)TestJson.getUser()).printAvailable();
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                try {
                    if ( isChecked )
                    {
                        ((Student)TestJson.getUser()).addToDashBoard(holder.mItem);
                    }
                    else
                    {
                        ((Student)TestJson.getUser()).removeWishFromDashBoard(holder.mItem);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

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
        public final TextView mQuantity;
        public AvailableItem mItem;
        public CheckBox mCheckBox;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContent = (TextView) view.findViewById(R.id.content);
            mCheckBox = (CheckBox)view.findViewById(R.id.add_to_wish);
            mQuantity = (TextView) view.findViewById(R.id.item_quantity);
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
                    Log.i("constrain", constraint.toString());
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
