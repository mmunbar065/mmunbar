package com.example.my_application.activities;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback.
    }
}