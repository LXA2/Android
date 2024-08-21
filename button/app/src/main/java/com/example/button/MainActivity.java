package com.example.button;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.btn1);

        //click event
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn1.setText("clicked");
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,activity1.class);
                startActivity(intent);
            }
        });


        btn1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btn1.setText("long clicked");
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,activity1.class);
                startActivity(intent);
                return true;
            }
        });

        /*btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                btn1.setText("touched");
                return false;
            }
        });*/
    }
}