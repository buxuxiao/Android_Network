package com.example.user.myapplication;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout mapView;

    private ImageView imageView;

    public final int TIME = 500;
    public final int CUT_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        mapView = (RelativeLayout) findViewById(R.id.mapView);
        imageView = (ImageView) findViewById(R.id.image);

        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cutLine();
                imageView.setImageResource(R.mipmap.map);
                drawMarks();
                postDelay(imageView, 0);
            }
        });
    }

    private void initData() {
        imageView.setImageResource(R.mipmap.map);
        imageView.setImageMatrix(new Matrix());

        int[][] pos = {
                {300, 300},
                {100, 300},
                {100, 500},
                {400, 500},
                {400, 800},
        };

        for (int i = 0; i < pos.length; i++) {
            ImageView markView = new ImageView(this);
            markView.setImageResource(R.mipmap.ic_launcher);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            markView.setLayoutParams(params);
            PointF pointF = new PointF(pos[i][0], pos[i][1]);
            addView(markView, pointF);
        }
    }

    private List<View> markViews = new ArrayList<>();
    private List<PointF> markViewPositions = new ArrayList<>();

    private List<PointF> linePosition = new ArrayList<>();

    private void addView(View view, PointF pointF) {
        markViews.add(view);
        markViewPositions.add(pointF);
    }

    private void drawLine(int i) {
        if (i == linePosition.size() - 1) {
            return;
        }

        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(bitmap2);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        PointF start = linePosition.get(i);
        PointF end = linePosition.get(++i);

        canvas.drawLine(start.x, start.y, end.x, end.y, paint);

        imageView.setImageBitmap(bitmap2);

        postDelay(imageView, i);
    }

    private void postDelay(View view, final int i) {
        int delay = TIME / CUT_COUNT;
        if (i == 0) {
            delay = TIME;
        }
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawLine(i);
            }
        }, delay);
    }

    private void cutLine() {
        linePosition.clear();

        PointF end = null;
        for (int i = 0; i < markViewPositions.size() - 1; i++) {
            PointF start = markViewPositions.get(i);
            end = markViewPositions.get(i + 1);

            float dx = end.x - start.x;
            float dy = end.y - start.y;

            dx /= CUT_COUNT;
            dy /= CUT_COUNT;

            linePosition.add(start);
            for (int j = 0; j < CUT_COUNT - 1; j++) {
                PointF pointF = new PointF();
                pointF.x = start.x + dx;
                pointF.y = start.y + dy;
                linePosition.add(pointF);
            }
        }
        linePosition.add(end);
    }

    private void drawMarks() {
        mapView.removeAllViews();
        mapView.addView(imageView);
        for (int i = 0; i < markViews.size(); i++) {
            final View markView = markViews.get(i);
            PointF pointF = markViewPositions.get(i);
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) markView.getLayoutParams();
            int marginX = (int) pointF.x - 72;
            int marginY = (int) pointF.y - 72;

            markView.setTranslationY(marginY);
            markView.setTranslationX(marginX);

            addAnimator(markView, marginX, marginY, i);

            mapView.addView(markView);
        }
    }

    private void addAnimator(final View view, int marginX, int marginY, int pos) {
        view.setVisibility(View.INVISIBLE);

        ObjectAnimator animator = ObjectAnimator
                .ofFloat(view, "translationY", marginY - 300, marginY)
                .setDuration(TIME);
        animator.setStartDelay(TIME * pos);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
}
