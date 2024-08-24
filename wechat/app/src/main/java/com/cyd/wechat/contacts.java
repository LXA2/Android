package com.cyd.wechat;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.net.Uri;

public class contacts extends AppCompatActivity {
    private LinearLayout section1, section2, section3, section4,contact1;
    private ImageView image1, image2, image3, image4;
    private TextView text1, text2, text3, text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        /*EdgeToEdge.enable(this);*/
        section1 = findViewById(R.id.section1);
        section3 = findViewById(R.id.section3);
        section4 = findViewById(R.id.section4);
        contact1 = findViewById(R.id.contact1);

        section1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动另一个Activity
                Intent intent = new Intent();
                intent.setClass(contacts.this,home_page.class);
                startActivity(intent);
            }
        });
        section3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动另一个Activity
                Intent intent = new Intent();
                intent.setClass(contacts.this,discovery.class);
                startActivity(intent);
            }
        });
        section4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动另一个Activity
                Intent intent = new Intent();
                intent.setClass(contacts.this,me.class);
                startActivity(intent);
            }
        });
        contact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }
}
