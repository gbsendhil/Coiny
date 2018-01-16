package com.binarybricks.coinhood.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

/**
 * Created by pranay airan on 1/16/18.
 */
public class ResourceProviderImpl implements ResourceProvider {
    private Context appContext;

    public ResourceProviderImpl(Context context) {
        appContext = context.getApplicationContext();
    }


    @NonNull
    @Override
    public String getString(@StringRes int resId) {
        return appContext.getString(resId);
    }


    @NonNull
    @Override
    public String getString(@StringRes int resId, Object... formatArgs) {
        return appContext.getString(resId, formatArgs);
    }


    @NonNull
    @Override
    public String getQuantityString(@PluralsRes int resId, int quantity) {
        return appContext.getResources().getQuantityString(resId, quantity);
    }


    @NonNull
    @Override
    public String getQuantityString(@PluralsRes int resId, int quantity, Object... formatArgs) {
        return appContext.getResources().getQuantityString(resId, quantity, formatArgs);
    }

    @NonNull
    @Override
    public Integer getColor(int resId) {
        return ContextCompat.getColor(appContext, resId);
    }

    @Nullable
    @Override
    public Drawable getDrawable(int resId) {
        return ContextCompat.getDrawable(appContext, resId);
    }
}
