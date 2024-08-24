package com.cyd.wechat;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.net.Uri;


public class discovery extends AppCompatActivity {
    private LinearLayout section1, section2, section3, section4,moments,search,miniapp,channels;
    private ImageView image1, image2, image3, image4;
    private TextView text1, text2, text3, text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discovery);
        /*EdgeToEdge.enable(this);*/
        section1 = findViewById(R.id.section1);
        section2 = findViewById(R.id.section2);
        section4 = findViewById(R.id.section4);
        moments = findViewById(R.id.moments_container);
        search = findViewById(R.id.search_container);
        miniapp = findViewById(R.id.miniapp_container);
        channels = findViewById(R.id.channels_container);

        section1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动另一个Activity
                Intent intent = new Intent();
                intent.setClass(discovery.this,home_page.class);
                startActivity(intent);
            }
        });
        section2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动另一个Activity
                Intent intent = new Intent();
                intent.setClass(discovery.this,contacts.class);
                startActivity(intent);
            }
        });
        section4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动另一个Activity
                Intent intent = new Intent();
                intent.setClass(discovery.this,me.class);
                startActivity(intent);
            }
        });
        moments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动另一个Activity
                openWeChat("moment");
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动另一个Activity
                openWeChat("search");
            }
        });
        miniapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动另一个Activity
                openWeChat("miniapp");
            }
        });
        channels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动另一个Activity
                openWeChat("channels");
            }
        });


    }
    public void openWeChat(String p) {
        Intent intent = new Intent();
        String open_cls = "";

        switch (p) {
            case "main":
                // 打开微信主页面
                open_cls = "com.tencent.mm.ui.LauncherUI";
                break;
            case "moment":
                // 打开微信朋友圈
                open_cls = "com.tencent.mm.plugin.sns.ui.SnsTimeLineUI";  // 更新为正确的朋友圈入口
                break;
            case "miniapp":
                // 打开微信小程序
                open_cls = "com.tencent.mm.plugin.appbrand.launching.AppBrandLaunchProxyUI";
                break;
            case "channels":
                // 打开视频号
                open_cls = "com.tencent.mm.plugin.finder.ui.FinderHomeUI";
                break;
            case "search":
                // 打开微信搜一搜
                open_cls = "com.tencent.mm.plugin.websearch.ui.WebSearchVoiceUI";  // 搜一搜页面
                break;
            case "scan":
                // 打开微信扫一扫
                open_cls = "com.tencent.mm.plugin.scanner.ui.BaseScanUI";  // 扫一扫页面
                break;
            default:
                // 默认打开微信主页面
                open_cls = "com.tencent.mm.ui.LauncherUI";
                break;
        }

        try {
            intent.setComponent(new android.content.ComponentName("com.tencent.mm", open_cls));
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(intent);
        } catch (Exception e) {
            // 如果微信未安装，或者出现问题，打开应用市场
            try {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("weixin://"));
                startActivity(intent);
            } catch (Exception ex) {
                // 如果应用市场也不可用，提示用户
                // Toast.makeText(this, "Cannot open WeChat or App Store", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
