package com.binarybricks.coiny.utils;

/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */


import android.support.v7.widget.RecyclerView;

public abstract class OnVerticalScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (!recyclerView.canScrollVertically(-1)) {
            onScrolledToTop();
        } else if (!recyclerView.canScrollVertically(1)) {
            onScrolledToBottom();
        }
        if (dy < 0) {
            onScrolledUp(dy);
        } else if (dy > 0) {
            onScrolledDown(dy);
        }

        onScrolled(recyclerView.computeVerticalScrollOffset());
    }

    public void onScrolledUp(int dy) {
        onScrolledUp();
    }

    public void onScrolledDown(int dy) {
        onScrolledDown();
    }

    public void onScrolledUp() {
    }

    public void onScrolledDown() {
    }

    public void onScrolledToTop() {
    }

    public void onScrolledToBottom() {
    }

    public void onScrolled(int offset) {

    }
}
