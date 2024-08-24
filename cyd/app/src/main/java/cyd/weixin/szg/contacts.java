package cyd.weixin.szg;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.net.Uri;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.os.VibrationEffect;
import android.os.Vibrator;



public class contacts extends AppCompatActivity {
    private LinearLayout section1, section2, section3, section4,contact1;
    private ImageView image1, image2, image3, image4;
    private TextView text1, text2, text3, text4;
    private static final String CHANNEL_ID = "CYD_CHANNEL";
    private static final String CHANNEL_NAME = "CYD Notifications";
    private static final String CHANNEL_DESC = "CYD Notification Description";

    private int[] buttonIds = {R.id.contact1, R.id.contact2, R.id.contact3, R.id.contact4, R.id.contact5, R.id.contact6, R.id.contact7, R.id.contact8};
    private int[] notificationIcons = {
            R.drawable.new_friend,
            R.drawable.group_chat,
            R.drawable.label,
            R.drawable.gzh,
            R.drawable.avata1,
            R.drawable.avata2,
            R.drawable.avata3,
            R.drawable.avata4
    };

    private String packageNameToOpen = "com.tencent.mobileqq";  // 替换为目标应用的包名

    private void vibrateDevice() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // For Android O and above, use VibrationEffect
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE); // 震动500ms
                vibrator.vibrate(vibrationEffect);
            } else {
                // For devices below Android O
                vibrator.vibrate(200); // 震动500ms
            }
        }
    }


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
        createNotificationChannel();

        for (int i = 0; i < buttonIds.length; i++) {
            final int index = i;
            LinearLayout button = findViewById(buttonIds[i]);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendNotification(index);
                }
            });
        }

    }
    private void sendNotification(int buttonIndex) {
        // 创建通知点击后打开目标应用的Intent
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageNameToOpen);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            // 如果应用未安装，跳转到应用商店或者其他逻辑
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageNameToOpen));
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(notificationIcons[buttonIndex])
                .setContentTitle("CYD")
                .setContentText("FKCYD YLSZG")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(buttonIndex, builder.build());
        vibrateDevice();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
