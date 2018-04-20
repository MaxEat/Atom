package com.example.max.testjson;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.max.testjson.dummy.DummyContent;
import com.example.max.testjson.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

public class Admin_AvailableItemFragment extends Fragment implements SearchView.OnQueryTextListener{

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private AvailableItemFragment.OnListFragmentInteractionListener mListener;
    private SearchView searchView;
    private Spinner chooseLocation;
    private AvailableItemRecyclerViewAdapter adapter;
    private Toolbar toolbar;

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
        View view = inflater.inflate(R.layout.fragment_availableitem_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.available_list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        chooseLocation = (Spinner)view.findViewById(R.id.choose_location);
        String[] items = new String[]{"1", "2", "three"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        chooseLocation.setAdapter(spinnerAdapter);

        toolbar= (Toolbar)view.findViewById(R.id.toolbar);
        searchView= (SearchView)view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);

        adapter = new AvailableItemRecyclerViewAdapter(TestJson.getUser().availableItems, mListener);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(
                getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AvailableItemFragment.OnListFragmentInteractionListener) {
            mListener = (AvailableItemFragment.OnListFragmentInteractionListener) context;
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
        void onListFragmentInteraction(AvailableItem item);
    }

}
