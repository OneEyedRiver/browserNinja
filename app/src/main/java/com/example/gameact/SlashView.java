package com.example.gameact;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet; // ✅ ADD THIS
import android.view.View;
import android.graphics.PointF;
import java.util.ArrayList;
public class SlashView extends View {

    private Paint paint;
    private Path path;
    private Handler handler = new Handler();

    ArrayList<PointF> points = new ArrayList<>();
    int MAX_POINTS = 20; // 🔥 adjust this (higher = longer trail)

    // ✅ REQUIRED constructor for XML
    public SlashView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // (optional but good practice)
    public SlashView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xFFFFFFFF);
        paint.setStrokeWidth(15);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        path = new Path();
    }

    public void addPoint(float x, float y) {

        points.add(new PointF(x, y));

        // 🔥 LIMIT LENGTH
        if (points.size() > MAX_POINTS) {
            points.remove(0); // remove oldest
        }

        invalidate();

        handler.removeCallbacks(clearRunnable);
        handler.postDelayed(clearRunnable, 100);
    }

    public void start(float x, float y) {
        points.clear();
        points.add(new PointF(x, y));
        invalidate();
    }

    private Runnable clearRunnable = new Runnable() {
        @Override
        public void run() {
            points.clear(); // ✅ CLEAR THE ACTUAL DATA
            invalidate();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (points.size() < 2) return;

        for (int i = 0; i < points.size() - 1; i++) {
            PointF p1 = points.get(i);
            PointF p2 = points.get(i + 1);

            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
        }
    }

    public void clear() {
        points.clear();
        invalidate();
    }
}