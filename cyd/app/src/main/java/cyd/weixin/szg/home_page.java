package cyd.weixin.szg;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import android.media.MediaPlayer;


public class home_page extends AppCompatActivity {
    private LinearLayout section1, section2, section3, section4;
    private ImageView image1, image2, image3, image4;
    private TextView text1, text2, text3, text4;
    private MediaPlayer mediaPlayer;
    private Integer[] items;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        /*EdgeToEdge.enable(this);*/
        section2 = findViewById(R.id.section2);
        section3 = findViewById(R.id.section3);
        section4 = findViewById(R.id.section4);

        section2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mediaPlayer.stop();
                // 启动另一个Activity
                Intent intent = new Intent();
                intent.setClass(home_page.this,contacts.class);
                startActivity(intent);
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
        });
        section3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mediaPlayer.stop();
                // 启动另一个Activity
                Intent intent = new Intent();
                intent.setClass(home_page.this,discovery.class);
                startActivity(intent);
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
        });
        section4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mediaPlayer.stop();
                // 启动另一个Activity
                Intent intent = new Intent();
                intent.setClass(home_page.this,me.class);
                startActivity(intent);
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
        });


        listView = findViewById(R.id.recycler_view);

        // Create an array with 1000 elements
        items = new Integer[1000];
        for (int i = 0; i < 1000; i++) {
            items[i] = i;
        }

        // Create custom adapter
        CustomAdapter adapter = new CustomAdapter(this, R.layout.item, items);
        listView.setAdapter(adapter);
    }

    private class CustomAdapter extends ArrayAdapter<Integer> {
        private int resource;
        private int[] images = {R.drawable.avata1, R.drawable.avata2, R.drawable.avata3, R.drawable.avata4};
        private Random random = new Random();
        private int[] audioFiles = {
                R.raw.audio1, R.raw.audio2, R.raw.audio3, R.raw.audio4, R.raw.audio5,
                R.raw.audio6, R.raw.audio7, R.raw.audio8, R.raw.audio9, R.raw.audio10,
                R.raw.audio11, R.raw.audio12, R.raw.audio13, R.raw.audio14, R.raw.audio15,
                R.raw.audio16, R.raw.audio17, R.raw.audio18, R.raw.audio19, R.raw.audio20,
                R.raw.audio21,R.raw.audio22
        };
        private List<int[]> shuffledAudioFilesList = new ArrayList<>();

        public CustomAdapter(Context context, int resource, Integer[] objects) {
            super(context, resource, objects);
            this.resource = resource;
            shuffleAudioFiles();
        }

        private void shuffleAudioFiles() {
            for (int i = 0; i < items.length / audioFiles.length; i++) {
                int[] shuffledAudioFiles = audioFiles.clone();
                List<Integer> audioList = Arrays.asList(Arrays.stream(shuffledAudioFiles).boxed().toArray(Integer[]::new));
                Collections.shuffle(audioList);
                shuffledAudioFiles = audioList.stream().mapToInt(Integer::intValue).toArray();
                shuffledAudioFilesList.add(shuffledAudioFiles);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.img1);
            // 随机选择图片
            int randomIndex = random.nextInt(images.length);
            imageView.setImageResource(images[randomIndex]);

            // 设置点击事件监听器
            convertView.setOnClickListener(v -> {

                // 停止任何正在播放的音频
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }

                // 计算音频文件索引
                int groupIndex = position / audioFiles.length;
                int audioIndex = position % audioFiles.length;

                // 播放对应的音频文件
                mediaPlayer = MediaPlayer.create(getContext(), shuffledAudioFilesList.get(groupIndex)[audioIndex]);
                mediaPlayer.start();
            });

            return convertView;
        }
    }
}
