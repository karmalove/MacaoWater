package com.macaowater.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Kevin on 2016/4/25.
 */
public class OutputView extends View {
    private Paint mPaint;


    public OutputView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public OutputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public OutputView(Context context) {
        super(context);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        canvas.drawRect(10, 120, 600, 490, mPaint);
        canvas.drawLine(10,210,600,210,mPaint);
        canvas.drawLine(10,300,600,300,mPaint);
        canvas.drawLine(10,390,600,390,mPaint);
    }
}
