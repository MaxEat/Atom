package com.example.max.testjson;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.max.testjson.BorrowedFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
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
        Picasso.with(this.context).load(mValues.get(position).getImageURL());
        holder.mClassification.setText(mValues.get(position).getClassification());
        holder.mContentView.setText(mValues.get(position).getBorrowedLocation() + " "+ mValues.get(position).getBorrowedTimeStamp());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("itemClassfication", holder.mItem.getClassification());
        } catch(JSONException e){
            e.printStackTrace();
        }

        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getItemPictureURL, postdata.toString(),new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success","result----"+result);
                    try {
                        JSONObject json = new JSONObject(result);
                        String url = json.getString("pictureUrl");
                        holder.mItem.setImageURL(url);
                        Log.i("url",url);
                        Picasso.with(context).load(url).resize(120, 60).into(holder.mImage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onFailture() {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImage;
        public final TextView mContentView;
        public final TextView mClassification;

        public BorrowedItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView)view.findViewById(R.id.imageView);
            mContentView = (TextView) view.findViewById(R.id.content);
            mClassification = (TextView)view.findViewById(R.id.classfication);

        }


        @Override
        public String toString() {
            return super.toString() + " '" + mClassification + " '" + mContentView.getText() + " '";
        }
    }
}
