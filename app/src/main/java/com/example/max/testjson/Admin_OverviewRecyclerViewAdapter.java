package com.example.max.testjson;

/**
 * Created by ASUS on 2018/4/11.
 */

    import android.support.v7.widget.RecyclerView;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;

    import com.example.max.testjson.Admin_OverviewFragment.OnListFragmentInteractionListener;


    import java.util.List;

public class Admin_OverviewRecyclerViewAdapter extends RecyclerView.Adapter<Admin_OverviewRecyclerViewAdapter.ViewHolder> {
    private final List<Admin_Overview> mValues;
    private final OnListFragmentInteractionListener mListener;

    public Admin_OverviewRecyclerViewAdapter(List<Admin_Overview> items, OnListFragmentInteractionListener listener)
    {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_admin_overview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
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
        public final TextView mLocationView;
        public final TextView mTypeView;
        public Admin_Overview mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLocationView = (TextView) view.findViewById(R.id.admin_location);
            mTypeView = (TextView) view.findViewById(R.id.admin_type);
        }

        @Override
        public String toString() {
            return super.toString() + " '"  + " '" + mTypeView.getText() + " '" + mLocationView.getText() + "'" +  " '";
        }
    }
}

