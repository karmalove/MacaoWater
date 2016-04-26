package com.macaowater.app.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.macaowater.app.R;

/**
 * Created by Karma on 2016/4/20.
 * 量程表
 */
public class KnobView extends View {

    private int mWidth;
    private int mHeight;
    // 直径
    private int diameter = 500;

    // 圆心
    private float centerX;
    private float centerY;

    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint curSpeedPaint;
    /**
     * kevin 绘制指针
     */
    private Paint p;
    // 矩形
    private RectF bgRect;
    // 做动画的时间引擎
    private ValueAnimator progressAnimator;
    // 开始的角度
    private float startAngle = 135;
    // sweep 扫 Angle角度 扫过的角度
    private float sweepAngle = 360;
    // 当前的角度
    private float currentAngle = 0;
    // 最后的角度
    private float lastAngle;
    // 颜色数组(渐变色)
    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED,
            Color.RED};
    private float maxValues = 60;
    private float curValues = 0;
    private float bgArcWidth = dipToPx(2);
    private float progressWidth = dipToPx(10);
    private float textSize = dipToPx(60);
    private float hintSize = dipToPx(15);
    private float curSpeedSize = dipToPx(13);
    private int aniSpeed = 1000;
    // degree 程度，等级；度；学位；阶层
    private float longdegree = dipToPx(13);
    private float shortdegree = dipToPx(5);
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(8);
    private String hintColor = "#676767";
    private String longDegreeColor = "#111111";
    private String shortDegreeColor = "#111111";
    private String bgArcColor = "#111111";
    private boolean isShowCurrentSpeed = true;
    private String hintString = "Km/h";
    private boolean isNeedTitle;
    private boolean isNeedUnit;
    private boolean isNeedDial;
    private boolean isNeedContent;
    private String titleString;

    // sweepAngle / maxValues 的值
    private float k;

    /**
     * 在代码中直接 new
     *
     * @param context
     */
    public KnobView(Context context) {
        super(context, null);
        initView();
    }

    /**
     * 在布局文件中设置控件
     *
     * @param context
     * @param attrs
     */
    public KnobView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initCofig(context, attrs);
        initView();
    }

    public KnobView(Context context, AttributeSet attrs,
                    int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        initView();
    }

    /**
     * 初始化布局配置
     *
     * @param context
     * @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        // 获得布局文件中对控件设置的参数集合
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ColorArcProgressBar);
        int color1 = a.getColor(R.styleable.ColorArcProgressBar_front_color1,
                Color.GREEN);
        int color2 = a.getColor(R.styleable.ColorArcProgressBar_front_color2,
                color1);
        int color3 = a.getColor(R.styleable.ColorArcProgressBar_front_color3,
                color1);
        colors = new int[]{color1, color2, color3, color3};

        sweepAngle = a.getInteger(R.styleable.ColorArcProgressBar_total_engle,
                360);
        bgArcWidth = a.getDimension(R.styleable.ColorArcProgressBar_back_width,
                dipToPx(2));
        progressWidth = a.getDimension(
                R.styleable.ColorArcProgressBar_front_width, dipToPx(10));
        isNeedTitle = a.getBoolean(
                R.styleable.ColorArcProgressBar_is_need_title, false);
        isNeedContent = a.getBoolean(
                R.styleable.ColorArcProgressBar_is_need_content, false);
        isNeedUnit = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_unit,
                false);
        isNeedDial = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_dial,
                false);
        hintString = a.getString(R.styleable.ColorArcProgressBar_string_unit);
        titleString = a.getString(R.styleable.ColorArcProgressBar_string_title);
        curValues = a
                .getFloat(R.styleable.ColorArcProgressBar_current_value, 0);
        maxValues = a.getFloat(R.styleable.ColorArcProgressBar_max_value, 60);
        setCurrentValues(curValues);
        setMaxValues(maxValues);
        //回收资源
        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        int height = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        setMeasuredDimension(width, height);
    }

    private void initView() {
        // 设置直径的大小
        diameter = 3 * getScreenWidth() / 5;
        // 弧形的矩阵区域
        bgRect = new RectF();
        bgRect.top = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.left = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter
                + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter
                + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);

        // 圆心
        centerX = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;
        centerY = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;

        // 外部刻度线
        degreePaint = new Paint();
        degreePaint.setStyle(Paint.Style.FILL);
        degreePaint.setColor(Color.parseColor(longDegreeColor));

        // 整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        // allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStyle(Paint.Style.FILL);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(Color.parseColor(bgArcColor));
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        // 当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        // 填充方式为描边
        progressPaint.setStyle(Paint.Style.STROKE);
        // 设置画笔转弯去的连接风格
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);
        // paint.setDither(true);// 使用抖动效果

        // 内容显示文字(显示进度的文字)
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(Color.BLACK);
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        // 显示单位文字(百分比 步 km/h)
        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
        hintPaint.setColor(Color.parseColor(hintColor));
        // hintPaint.setColor(Color.RED);
        // 设置文字的位置
        hintPaint.setTextAlign(Paint.Align.CENTER);

        // 显示标题文字(截止当前已走 当前速度)
        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
        curSpeedPaint.setColor(Color.parseColor(hintColor));
        // curSpeedPaint.setColor(Color.RED);
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);

        //绘制指针
        p = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // System.out.println("++++++++++");

        // 设置画布绘图无锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
        // 是否需要画刻度
        if (isNeedDial) {
            // 画刻度线
            for (int i = 0; i < 36; i++) {
                if (i > 12 && i < 24) {
                    /**
                     * 第一个参数:每次偏移的角度
                     */
                    canvas.rotate(10, centerX, centerY);
                    continue;
                }
//				if (i % 5 == 0) {
//					// 设置画笔的粗细
//					degreePaint.setStrokeWidth(dipToPx(2));
//					degreePaint.setColor(Color.parseColor(longDegreeColor));
//					// canvas.drawLine(centerX, centerY - diameter / 2
//					// - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE,
//					// centerX, centerY - diameter / 2 - progressWidth / 2
//					// - DEGREE_PROGRESS_DISTANCE - longdegree,
//					// degreePaint);
//
//					canvas.drawCircle(centerX, centerY - diameter / 2
//							- progressWidth / 2 - DEGREE_PROGRESS_DISTANCE, 6,
//							degreePaint);
//
//				} else {
                degreePaint.setStrokeWidth(dipToPx(1.4f));
                degreePaint.setColor(Color.parseColor(shortDegreeColor));
                // canvas.drawLine(centerX, centerY - diameter / 2
                // - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE
                // - (longdegree - shortdegree) / 2, centerX, centerY
                // - diameter / 2 - progressWidth / 2
                // - DEGREE_PROGRESS_DISTANCE
                // - (longdegree - shortdegree) / 2 - shortdegree,
                // degreePaint);

                canvas.drawCircle(centerX, centerY - diameter / 3
                        - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE
                        - (longdegree - shortdegree) / 2, 10, degreePaint);
//				}

                canvas.rotate(10, centerX, centerY);
            }
        }

        /**
         * 整个弧 第一个参数:弧形所在的矩形的边框 第二个参数:开始的角度 第三个参数:扫过的角度 第四个参数:是否显示开始/结束点到圆心的直径
         */
        //canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);
        /**
         * kevin
         */
        //绘制内圆
        allArcPaint.setARGB(255, 54, 54, 54);
        allArcPaint.setStyle(Paint.Style.FILL);
        allArcPaint.setStrokeWidth(50);
        canvas.drawCircle(centerX, centerY, diameter / 3, allArcPaint);//以该圆为半径向内外扩展至厚度为10px
        //绘制外圆
        allArcPaint.setARGB(255, 0, 0, 0);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(30);
        canvas.drawCircle(centerX, centerY, diameter / 3, allArcPaint);
        //绘制指针
        p.setColor(Color.WHITE);
        p.setStrokeWidth(4);
        canvas.drawLine(centerX, centerY, centerX - diameter / 2, centerY + centerY / 2, p);

        // 设置渐变色,第四个参数为null 颜色将自动隔开
        SweepGradient sweepGradient = new SweepGradient(centerX, centerY,
                colors, null);
        // 矩阵,模型
        Matrix matrix = new Matrix();
        // 第一个参数表一颜色修改的程度
        matrix.setRotate(130, centerX, centerY);
        sweepGradient.setLocalMatrix(matrix);
        // 设置着色器
        // progressPaint.setShader(sweepGradient);

        // 当前进度
        canvas.drawArc(bgRect, startAngle, currentAngle, false, progressPaint);

        if (isNeedContent) {
            canvas.drawText(String.format("%.0f", curValues), centerX, centerY
                    + textSize / 3, vTextPaint);
        }
        if (isNeedUnit) {
            canvas.drawText(hintString, centerX, centerY + 2 * textSize / 3,
                    hintPaint);
        }
        if (isNeedTitle) {
            canvas.drawText(titleString, centerX, centerY - 2 * textSize / 3,
                    curSpeedPaint);
        }

        invalidate();

    }

    /**
     * 设置最大值
     *
     * @param maxValues
     */
    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        // 扫过的角度/单位最大角度
        k = sweepAngle / maxValues;
    }

    /**
     * 设置当前值
     *
     * @param currentValues
     */
    public void setCurrentValues(float currentValues) {
        // Log.i("ColorArcProgressBar", currentValues+"----"+maxValues);
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }
        if (currentValues < 0) {
            currentValues = 0;
        }
        this.curValues = currentValues;
        lastAngle = currentAngle;
        setAnimation(lastAngle, currentValues * k, aniSpeed);
    }

    /**
     * 设置整个圆弧宽度
     *
     * @param bgArcWidth
     */
    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = bgArcWidth;
    }

    /**
     * 设置进度宽度
     *
     * @param progressWidth
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * 设置速度文字大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置单位文字大小
     *
     * @param hintSize
     */
    public void setHintSize(int hintSize) {
        this.hintSize = hintSize;
    }

    /**
     * 设置单位文字
     *
     * @param hintString
     */
    public void setUnit(String hintString) {
        this.hintString = hintString;
        invalidate();
    }

    /**
     * 设置直径大小
     *
     * @param diameter
     */
    public void setDiameter(int diameter) {
        this.diameter = dipToPx(diameter);
    }

    /**
     * 为进度设置动画
     *
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngle);
        progressAnimator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        // 获取当前动画的角度
                        currentAngle = (Float) animation.getAnimatedValue();
                        // Log.i("ColorArcProgressBar", currentAngle+"----"+k);
                        // 除不除k都一样
                        curValues = currentAngle / k;
                        // curValues = currentAngle ;
                        // Log.i("curValues", curValues+"----"+k);
                    }
                });
        progressAnimator.start();
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        // 获得屏幕的像素密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 得到屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 设置是否显示当前速度
     *
     * @param isShowCurrentSpeed
     */
    public void setIsShowCurrentSpeed(boolean isShowCurrentSpeed) {
        this.isShowCurrentSpeed = isShowCurrentSpeed;
    }

    @Override
    public void setBackgroundColor(int color) {
        // TODO Auto-generated method stub
        super.setBackgroundColor(color);
    }

}
