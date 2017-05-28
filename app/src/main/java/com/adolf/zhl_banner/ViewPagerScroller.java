package com.adolf.zhl_banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by adolf on 2017/5/20.
 */

public class ViewPagerScroller extends Scroller {
    private int mDuration;
    public ViewPagerScroller(Context context) {
        super(context);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration <= 0 ? 1000 : mDuration);
    }

    @Override
    public boolean computeScrollOffset() {
        return super.computeScrollOffset();
    }

    public void setDuration(int duration){
        mDuration = duration;
    }
}
