package com.binarybricks.coinhood.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;

/**
 * Created by pranay airan on 1/16/18.
 */
public interface ResourceProvider {
    @NonNull
    String getString(@StringRes int resId);

    @NonNull
    String getString(@StringRes int resId, Object... formatArgs);

    @NonNull
    String getQuantityString(@PluralsRes int resId, int quantity);

    @NonNull
    String getQuantityString(@PluralsRes int resId, int quantity, Object... formatArgs);

    @NonNull
    Integer getColor(int resId);

    @Nullable
    Drawable getDrawable(@DrawableRes int resId);
}
