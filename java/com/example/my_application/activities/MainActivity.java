package com.example.my_application.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
// import android.support.v7.app.AppCompatActivity;

import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.my_application.CleaningService;
import com.example.my_application.R;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private Button button;
private TextView titulo;

    protected static final int REQUESTCODE_SECOND_ACTIVITY = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, CleaningService.class);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        button.setOnClickListener(this);
        titulo=findViewById(R.id.tvTitle);
        titulo.setTypeface(ResourcesCompat.getFont(this, R.font.courierprime_bolditalic));


    }
    public void onClick(View v){
        sendMessage();
    }

    public void sendMessage(){
        Intent intent = new Intent(this,ListadoTareasActivity.class);
        startActivityForResult(intent, REQUESTCODE_SECOND_ACTIVITY);
    }

}