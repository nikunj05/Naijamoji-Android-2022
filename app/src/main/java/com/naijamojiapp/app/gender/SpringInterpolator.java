package com.naijamojiapp.app.gender;

import android.view.animation.Interpolator;

public class SpringInterpolator implements Interpolator {
    /* renamed from: a */
    double f3527a = 1.0d;
    /* renamed from: b */
    double f3528b = 10.0d;

    public SpringInterpolator(double d, double d2) {
        this.f3527a = d;
        this.f3528b = d2;
    }

    public float getInterpolation(float f) {
        return (float) (((-1.0d * Math.pow(2.718281828459045d, ((double) (-f)) / this.f3527a)) * Math.cos(this.f3528b * ((double) f))) + 1.0d);
    }
}
