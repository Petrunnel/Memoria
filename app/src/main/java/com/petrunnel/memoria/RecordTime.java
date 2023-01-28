package com.petrunnel.memoria;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
 
public class RecordTime extends ListActivity {
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_tab);
        
        RecordAdapter ra = new RecordAdapter (this);
        ArrayList<String> arr = ra.getRecTime();

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.item, arr);
        setListAdapter(mAdapter);
    }
}