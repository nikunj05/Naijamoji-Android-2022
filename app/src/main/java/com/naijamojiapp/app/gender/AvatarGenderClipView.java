package com.naijamojiapp.app.gender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AvatarGenderClipView extends FrameLayout {
    public AvatarGenderClipView(@NonNull Context context) {
        super(context);
    }

    public AvatarGenderClipView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    protected void dispatchDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        float f = (float) width;
        float f2 = f / 2.0f;
        int save = canvas.save();
        Path path = new Path();
        float f3 = (float) height;
        RectF rectF = new RectF(0.0f, 0.0f, f, f3 - f2);
        RectF rectF2 = new RectF(0.0f, (float) (height - width), f, f3);
        path.addRect(rectF, Direction.CW);
        path.addRoundRect(rectF2, f2, f2, Direction.CW);
        canvas.clipPath(path);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }
}
