package com.example.max.testjson;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import android.widget.ProgressBar;

import com.squareup.leakcanary.RefWatcher;


public class Admin_AvailableItemFragment extends Fragment implements SearchView.OnQueryTextListener{

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    public static Handler handler;
    private Admin_AvailableItemFragment.OnListFragmentInteractionListener mListener;
    private SearchView searchView;
    private Admin_AvailableItemAdapter adapter;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    public Admin_AvailableItemFragment() {
    }

    public static Admin_AvailableItemFragment newInstance(int columnCount) {
        Admin_AvailableItemFragment fragment = new Admin_AvailableItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_available_item_list, container, false);

        Context context = view.getContext();
        final RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.admin_available_list);
        recyclerView.setVisibility(View.INVISIBLE);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        progressBar = (ProgressBar)view.findViewById(R.id.loading_admin_overview);


        toolbar= (Toolbar)view.findViewById(R.id.admin_toolbar);
        searchView= (SearchView)view.findViewById(R.id.admin_search_view);
        searchView.setOnQueryTextListener(this);
        Log.i("itemlsit size in frag", Integer.toString(((Worker)TestJson.getUser()).getManageItems().size()));

        adapter = new Admin_AvailableItemAdapter(((Worker)TestJson.getUser()).getManageItems(), mListener);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(
                getContext(), DividerItemDecoration.VERTICAL));

        handler = new Handler() {
            @Override
            public void handleMessage(Message inputMessage) {
                switch (inputMessage.what) {
                    case 5:
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Admin_AvailableItemFragment.OnListFragmentInteractionListener) {
            mListener = (Admin_AvailableItemFragment.OnListFragmentInteractionListener) context;
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ManageItem item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = TestJson.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
