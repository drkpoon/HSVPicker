package com.user.derekpoon.hsvcolorselector;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IntRange;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import static com.user.derekpoon.hsvcolorselector.BitmapGenerator.getHueBitmap;


/**
 * Created by user on 28/12/2017.
 * ==
 */

public class HSVPickerDialog {
    private final int MAX_ALPHA = 255;
    private final AlertDialog dialog;
    private final int maxHue;
    @IntRange(from = 1, to = MAX_ALPHA)
    private int alpha = MAX_ALPHA;
    private float[] hsv = new float[]{120, 0.5f, 0.5f};
    private ImageView mImageViewColorSelected;
    private MyImageView mImageViewHue, mImageViewSV, mImageViewAlpha;
    private OnHSVPickerDialogListener mOnHSVPickerDialogListener;
    private TextView mTextViewHue, mTextViewSaturation, mTextViewValue, mTextViewAlpha, mTextViewRed, mTextViewGreen, mTextViewBlue;
    private float svViewSizeX, svViewSizeY, hueViewSizeY, alphaViewSizeY;

    HSVPickerDialog(final Context context, OnHSVPickerDialogListener onHSVPickerDialogListener, int[] hsvList) {
        if (hsvList.length > 0) {
            Color.colorToHSV(hsvList[0], hsv);
            alpha = Color.alpha(hsvList[0]);
        }
        maxHue = Integer.parseInt(context.getResources().getString(R.string.hueMax));
        mOnHSVPickerDialogListener = onHSVPickerDialogListener;

        final View view = LayoutInflater.from(context).inflate(R.layout.hsvdialog, null);
        mImageViewColorSelected = view.findViewById(R.id.colorSelected);
        mTextViewHue = view.findViewById(R.id.hueText);
        mTextViewSaturation = view.findViewById(R.id.saturationText);
        mTextViewValue = view.findViewById(R.id.valueText);
        mTextViewAlpha = view.findViewById(R.id.alphaText);
        mTextViewRed = view.findViewById(R.id.redText);
        mTextViewGreen = view.findViewById(R.id.greenText);
        mTextViewBlue = view.findViewById(R.id.blueText);
        mImageViewSV = view.findViewById(R.id.svView);
        mImageViewHue = view.findViewById(R.id.hueView);
        mImageViewAlpha = view.findViewById(R.id.alphaView);

        ViewTreeObserver viewTreeObserver = mImageViewSV.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                svViewSizeX = mImageViewSV.getMeasuredWidth();
                svViewSizeY = mImageViewSV.getMeasuredHeight();
                updateSVViewInput();
                mImageViewHue.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mImageViewSV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE ||
                        event.getAction() == MotionEvent.ACTION_UP) {
                    float x = Math.min(Math.max(0, event.getX()), svViewSizeX);
                    float y = Math.min(Math.max(0, event.getY()), svViewSizeY);
                    hsv[1] = x / svViewSizeX;
                    hsv[2] = 1 - y / svViewSizeY;
                    updateSVViewInput();
                    updateAlphaViewInput();
                    updateInput();
                }
                v.performClick();
                return true;
            }
        });

        ViewTreeObserver viewTreeObserver1 = mImageViewAlpha.getViewTreeObserver();
        viewTreeObserver1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                alphaViewSizeY = mImageViewAlpha.getMeasuredHeight();
                updateAlphaViewInput();
                mImageViewAlpha.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mImageViewAlpha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE ||
                        event.getAction() == MotionEvent.ACTION_UP) {
                    float h = Math.min(Math.max(0, event.getY()), alphaViewSizeY);
                    alpha = (int) ((1.f - h / alphaViewSizeY) * MAX_ALPHA);
                    updateAlphaViewInput();
                    updateInput();
                }
                v.performClick();
                return true;
            }
        });

        ViewTreeObserver viewTreeObserver2 = mImageViewHue.getViewTreeObserver();
        viewTreeObserver2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                hueViewSizeY = mImageViewHue.getMeasuredHeight();
                mImageViewHue.setImageBitmap(getHueBitmap(mImageViewHue.getMeasuredWidth(), mImageViewHue.getMeasuredHeight(),
                        maxHue, BitmapGenerator.Orientation.VERTICAL));
                updateHueViewInput();
                mImageViewHue.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mImageViewHue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE ||
                        event.getAction() == MotionEvent.ACTION_UP) {
                    float h = Math.min(Math.max(0, event.getY()), hueViewSizeY);
                    hsv[0] = (1 - h / hueViewSizeY) * maxHue;
                    updateHueViewInput();
                    updateSVViewInput();
                    updateAlphaViewInput();
                    updateInput();
                }
                v.performClick();
                return true;
            }
        });

        View.OnClickListener presetViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int color = ((ColorDrawable) v.getBackground()).getColor();
                Color.colorToHSV(color, hsv);
                alpha = Color.alpha(color);
                updateHueViewInput();
                updateSVViewInput();
                updateAlphaViewInput();
                updateInput();
            }
        };

        final int[] presetViewList = {R.id.presetView1, R.id.presetView2, R.id.presetView3, R.id.presetView4, R.id.presetView5};
        int MAX_COLOR = 5;
        for (int i = 0; i < Math.min(MAX_COLOR, hsvList.length); i++) {
            view.findViewById(presetViewList[i]).setBackgroundColor(hsvList[i]);
            view.findViewById(presetViewList[i]).setOnClickListener(presetViewOnClickListener);
        }

        updateInput();

        dialog = new AlertDialog.Builder(context)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mOnHSVPickerDialogListener != null) {
                            mOnHSVPickerDialogListener.onOk(HSVPickerDialog.this, Color.HSVToColor(alpha, hsv));
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mOnHSVPickerDialogListener != null) {
                            mOnHSVPickerDialogListener.onCancel(HSVPickerDialog.this);
                        }
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (mOnHSVPickerDialogListener != null) {
                            mOnHSVPickerDialogListener.onCancel(HSVPickerDialog.this);
                        }
                    }
                })
                .create();
        dialog.setView(view);
    }

    private void updateSVViewInput() {
        mImageViewSV.setHue(hsv[0]);
        mImageViewSV.setDrawCross(hsv[1] * svViewSizeX, (1.f - hsv[2]) * svViewSizeY);
        mImageViewSV.invalidate();
    }

    @SuppressLint("DefaultLocale")
    private void updateInput() {
        final int color = Color.HSVToColor(alpha, hsv);
        mImageViewColorSelected.setBackgroundColor(color);
        mTextViewHue.setText(String.format("H: %d", (int) hsv[0]));
        mTextViewSaturation.setText(String.format("S: %.2f", hsv[1]));
        mTextViewValue.setText(String.format("V: %.2f", hsv[2]));
        mTextViewAlpha.setText(String.format("A: %d", alpha));
        mTextViewRed.setText(String.format("R: %d", Color.red(color)));
        mTextViewGreen.setText(String.format("G: %d", Color.green(color)));
        mTextViewBlue.setText(String.format("B: %d", Color.blue(color)));
    }

    private void updateAlphaViewInput() {
        mImageViewAlpha.setAlphaValue(hsv);
        mImageViewAlpha.setDrawCross(0, alphaViewSizeY * (1.f - (float) alpha / MAX_ALPHA));
        mImageViewAlpha.invalidate();
    }

    private void updateHueViewInput() {
        mImageViewHue.setDrawCross(0, hueViewSizeY * (1.f - hsv[0] / maxHue));
        mImageViewHue.invalidate();
    }

    public void show() {
        dialog.show();
    }

    public interface OnHSVPickerDialogListener {
        void onCancel(HSVPickerDialog dialog);

        void onOk(HSVPickerDialog dialog, int color);
    }

}
