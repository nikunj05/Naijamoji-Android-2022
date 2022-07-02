package com.naijamojiapp.app.gender;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AvatarGenderRatioView extends FrameLayout {
    public AvatarGenderRatioView(@NonNull Context context) {
        super(context);
    }

    public AvatarGenderRatioView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    protected void onMeasure(int i, int i2) {
        i = MeasureSpec.getSize(i2);
        super.onMeasure(MeasureSpec.makeMeasureSpec((int) (((double) i) * 0.75d), 1073741824), MeasureSpec.makeMeasureSpec(i, 1073741824));
    }
}
