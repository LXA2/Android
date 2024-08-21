package com.example.arposition;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class SensorDataRenderer {

    private final SurfaceView surfaceView;
    private final Paint paint;
    private final List<float[]> positions;
    private long interval;
    private Thread renderThread;
    private boolean isRendering = false;

    public SensorDataRenderer(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        this.positions = new ArrayList<>();
        this.paint = new Paint();
        this.paint.setColor(Color.RED);
        this.paint.setStrokeWidth(5);
    }

    public void startRendering(long interval) {
        this.interval = interval;
        isRendering = true;
        renderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRendering) {
                    drawPositions();
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        renderThread.start();
    }

    public void stopRendering() {
        isRendering = false;
        if (renderThread != null) {
            try {
                renderThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPosition(float[] position) {
        synchronized (positions) {
            positions.add(position.clone());
        }
    }

    public long getInterval() {
        return interval;
    }

    private void drawPositions() {
        SurfaceHolder holder = surfaceView.getHolder();
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            canvas.drawColor(Color.BLACK); // 清除之前的内容
            synchronized (positions) {
                for (float[] position : positions) {
                    float x = canvas.getWidth() / 2 + position[0] * 100;
                    float y = canvas.getHeight() / 2 + position[1] * 100;
                    canvas.drawPoint(x, y, paint);
                }
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }
}
