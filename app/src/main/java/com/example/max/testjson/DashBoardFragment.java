package com.example.max.testjson;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.max.testjson.dashboard.News;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.max.testjson.TestJson.wv;


public class DashBoardFragment extends Fragment {

    private final  String returnMessage = "Dear, \n\n\n This is the administrator from ATOM inventory system from KU Leuven. \n\n The item you borrowed is expired. Please return it as soon as possible. \n\nFor more details, please check your personal dashboard.\n\n\nBest regards,\nAtom ";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private DashBoardRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DashBoardFragment() {
    }

    public static DashBoardFragment newInstance(int columnCount) {
        DashBoardFragment fragment = new DashBoardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dashlist);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            ArrayList<News> personalNews = TestJson.getUser().getDashboard();
            adapter = new DashBoardRecyclerViewAdapter(personalNews, mListener);
            adapter.setContext(getContext());
            recyclerView.setAdapter(adapter);
            TextView emptyDash = (TextView) view.findViewById(R.id.empty);
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.sendAlertEmail);
            Log.i("frag dash size ", Integer.toString(personalNews.size()));


            if(personalNews.isEmpty()){
                emptyDash.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.GONE);
            }
            else
            {
                emptyDash.setVisibility(View.GONE);
                if(TestJson.getUser().getUserType().equals("Student"))
                {
                    imageButton.setVisibility(View.GONE);
                }
                else
                {
                    imageButton.setVisibility(View.VISIBLE);

                    Log.i("type", TestJson.getUser().getUserType());
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.i("here", "clicked");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Do you want to send alert email to all borrowers in the black list?");
                            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    sendAlertEmail();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    });
                }
            }






        return view;
    }

    private void sendAlertEmail() {
        Log.i("Send email", "");
        String TO[] = new String[((Worker)TestJson.getUser()).getExpiredItems().size()];
        int i = 0;
        for(ExpiredItem item:((Worker)TestJson.getUser()).getExpiredItems()){
            TO[i] = item.getBorrowPersonEmail();
            i++;
        }
        Log.i("alert email number", Integer.toString(i));
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Returning item reminder");
        emailIntent.putExtra(Intent.EXTRA_TEXT, returnMessage);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            // getActivity().finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(News item);
    }
}
