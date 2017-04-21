package com.codingstudiosuk.flirds;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class DebugFragment extends ListFragment {
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    int clickCounter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_debug, container, false);
    }

    @Override
    public void onCreate(Bundle instance){
        super.onCreate(instance);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        setListAdapter(adapter);

    }

    public void addItems(View v){
        listItems.add("Clicked : "+clickCounter++);
        adapter.notifyDataSetChanged();
    }
}
