package com.example.max.testjson;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.max.testjson.DashBoardFragment.OnListFragmentInteractionListener;
import com.example.max.testjson.dashboard.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DashBoardRecyclerViewAdapter extends RecyclerView.Adapter<DashBoardRecyclerViewAdapter.ViewHolder> {

    private final List<News> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public DashBoardRecyclerViewAdapter(List<News> items, OnListFragmentInteractionListener listener) {
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
                .inflate(R.layout.fragment_news, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Log.i("item classification", holder.mItem.getItem().getClassification());
        String pictureUrl = TestJson.pictureMap.get(holder.mItem.getItem().getClassification());
        Log.i("url", pictureUrl);
        Picasso.with(context).load(pictureUrl).into(holder.imageView);

        holder.mTitleView.setText(mValues.get(position).getNewsTitle());
        holder.mContentView.setText(mValues.get(position).getNewsContent());

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
        public final TextView mTitleView;
        public final TextView mContentView;
        public final ImageView imageView;
        public News mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageView = (ImageView) view.findViewById(R.id.newsImage);
            mTitleView = (TextView) view.findViewById(R.id.newsTitle);
            mContentView = (TextView) view.findViewById(R.id.newsContent);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
