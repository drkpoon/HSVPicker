package com.user.derekpoon.hsvcolorselector;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by user on 3/1/2018.
 * ==
 */

public class HSVPickerPreference extends Preference {
    private int mColor;
//    public HSVPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);System.out.println("trace 1");
//        init();
//    }

    public HSVPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

//    public HSVPickerPreference(Context context) {
//        super(context);
//        System.out.println("trace 3");
//        init();
//    }

    private void init() {
        setWidgetLayoutResource(R.layout.preference_widget);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        mColor = a.getInt(index, 0xffffffff);
        return super.onGetDefaultValue(a, index);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        ImageView imageView = view.findViewById(R.id.colorPref);
        imageView.setBackgroundColor(mColor);
    }

    @Override
    protected void onClick() {
        super.onClick();
        HSVPickerDialog hsvPickerDialog = new HSVPickerDialog(getContext(), new HSVPickerDialog.OnHSVPickerDialogListener() {
            @Override
            public void onCancel(HSVPickerDialog dialog) {

            }

            @Override
            public void onOk(HSVPickerDialog dialog, int color) {
                if (callChangeListener(color)) {
//                    System.out.println(String.format(Locale.getDefault(), "Color is A- %d R- %d G- %d B- %d", Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color)));
                    mColor = color;
                    persistInt(color);
                    notifyChanged();
                }
            }
        }, new int[]{mColor});
        hsvPickerDialog.show();
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
//        System.out.println("onSetInitialValue " + restorePersistedValue);
//        System.out.println((int) defaultValue);
        if (restorePersistedValue) {
            mColor = getPersistedInt(mColor);
        } else {
            mColor = (int) defaultValue;
            persistInt(mColor);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();

        if (!isPersistent()) {
            final SavedState myState = new SavedState(superState);
            myState.value = mColor;
            return myState;
        } else {
            return superState;
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }
        // Restore the instance state
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        mColor = myState.value;
        notifyChanged();
    }

    private static class SavedState extends BaseSavedState {
        public static Creator<SavedState> creator = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int value;

        SavedState(Parcel source) {
            super(source);
            value = source.readInt();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(value);
        }
    }
}
