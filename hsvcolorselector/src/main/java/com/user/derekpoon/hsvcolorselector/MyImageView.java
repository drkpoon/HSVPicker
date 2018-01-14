package com.user.derekpoon.hsvcolorselector;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by user on 28/12/2017.
 * ==
 */

class MyImageView extends android.support.v7.widget.AppCompatImageView {
    public static final float DEFAULT_LINE_WIDTH = 2f;
    public static final int DEFAULT_MARK_RADIUS = 15;
    private float crossX, crossY;
    private Paint mPaintSv, mPaintAlpha, mPaintDisplay;
    private Paint mPaintLine;
    private int mViewType;

    private enum ViewType {HUE, SV, ALPHA, DISPLAY}

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyImageView, 0, 0);
        mViewType = typedArray.getInt(R.styleable.MyImageView_viewType, 0);
        typedArray.recycle();

        mPaintSv = new Paint();
        mPaintAlpha = new Paint();
        mPaintDisplay = new Paint();
        mPaintDisplay.setStrokeWidth(6 * DEFAULT_LINE_WIDTH);
        mPaintDisplay.setStyle(Paint.Style.STROKE);
        mPaintLine = new Paint();
        mPaintLine.setColor(Color.WHITE);
        mPaintLine.setStrokeWidth(DEFAULT_LINE_WIDTH);
    }

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setDrawCross(float x, float y) {
        crossX = x;
        crossY = y;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mViewType == ViewType.HUE.ordinal()) {
            canvas.drawCircle(0, crossY, DEFAULT_MARK_RADIUS, mPaintLine);
            canvas.drawCircle(this.getMeasuredWidth(), crossY, DEFAULT_MARK_RADIUS, mPaintLine);
            canvas.drawLine(0, crossY, this.getMeasuredWidth(), crossY, mPaintLine);
        } else if (mViewType == ViewType.SV.ordinal()) {
            canvas.drawRect(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight(), mPaintSv);
            canvas.drawLine(crossX, 0, crossX, this.getMeasuredHeight(), mPaintLine);
            canvas.drawLine(0, crossY, this.getMeasuredWidth(), crossY, mPaintLine);
        } else if (mViewType == ViewType.ALPHA.ordinal()) {
            canvas.drawRect(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight(), mPaintAlpha);
            canvas.drawCircle(0, crossY, DEFAULT_MARK_RADIUS, mPaintLine);
            canvas.drawCircle(this.getMeasuredWidth(), crossY, DEFAULT_MARK_RADIUS, mPaintLine);
            canvas.drawLine(0, crossY, this.getMeasuredWidth(), crossY, mPaintLine);
        } else if (mViewType == ViewType.DISPLAY.ordinal()) {
//            canvas.drawLine(0, 0, getMeasuredWidth(), 0, mPaintDisplay);
//            canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), mPaintDisplay);
//            canvas.drawLine(0, 0, 0, getMeasuredHeight(), mPaintDisplay);
//            canvas.drawLine(getMeasuredWidth(), 0, getMeasuredWidth(), getMeasuredHeight(), mPaintDisplay);
            canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaintDisplay);
        }
    }

    void setHue(@FloatRange(from = 0.f, to = 360.f) float hue) {
        LinearGradient verticalShader = new LinearGradient(0, 0, 0, this.getMeasuredHeight(), Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP);
        LinearGradient horizontalShader = new LinearGradient(0, 0, this.getMeasuredWidth(), 0, Color.WHITE,
                Color.HSVToColor(new float[]{hue, 1.f, 1.f}), Shader.TileMode.CLAMP);
        ComposeShader composeShader = new ComposeShader(verticalShader, horizontalShader, PorterDuff.Mode.MULTIPLY);
        mPaintSv.setShader(composeShader);
    }

    void setAlphaValue(float[] hsv) {
        LinearGradient verticalShader = new LinearGradient(0, 0, 0, getMeasuredHeight(), Color.HSVToColor(hsv),
                Color.TRANSPARENT, Shader.TileMode.CLAMP);
        mPaintAlpha.setShader(verticalShader);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        mPaintDisplay.setColor(color | 0xff000000);
    }
}
