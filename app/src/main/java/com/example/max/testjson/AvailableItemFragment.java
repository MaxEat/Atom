package com.example.max.testjson;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;


public class AvailableItemFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private SearchView searchView;
    private Spinner chooseLocation;
    private Spinner chooseType;
    private AvailableItemRecyclerViewAdapter adapter;
    private Toolbar toolbar;

    public AvailableItemFragment() {
    }

    public static AvailableItemFragment newInstance(int columnCount) {
        AvailableItemFragment fragment = new AvailableItemFragment();
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
        String[] locations = new String[]{"1", "2", "three"};
        ArrayAdapter<String> spinnerLocationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, locations);
        chooseLocation.setAdapter(spinnerLocationAdapter);


        chooseType = (Spinner)view.findViewById(R.id.choose_type);
        String[] types = new String[]{"4", "5", "6"};
        ArrayAdapter<String> spinnerTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, types);
        chooseType.setAdapter(spinnerTypeAdapter);

        toolbar= (Toolbar)view.findViewById(R.id.toolbar);
        searchView= (SearchView)view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);

        adapter = new AvailableItemRecyclerViewAdapter(TestJson.getUser().availableItems, mListener);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(
                    getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        AvailableItem city = adapter.getItem(position);
//
//        adapter.getFilter().filter(Long.toString(city.getId()),new Filter.FilterListener() {
//            @Override
//            public void onFilterComplete(int count) {
//
//            }
//        });
//    }

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
