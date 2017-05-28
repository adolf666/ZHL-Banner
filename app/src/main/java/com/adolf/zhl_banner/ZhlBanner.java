package com.adolf.zhl_banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by adolf on 2017/5/21.
 */

public class ZhlBanner extends RelativeLayout {
    private static final int MESSAGE_WHAT = 0;
    private LinearLayout mPointContainerLayout;
    private BannerViewPager mViewPager;
    private BannerChangeAdapter mPagerAdapter;
    private ArrayList<String> mImageUrls;
    private boolean mIsOneImg = false;
    private boolean mIsNeedAutoPaly = true;
    private boolean mIsPlaying = false;
    private int mPlayDelayTime = 4000;
    private int mCurrentPosition;
    private boolean mIsPointVisible = true;
    private LayoutParams mPointContainerParams;
    private int mScrollSpeed = 1000;
    private enum PageIndicatorAlign{
        ALIGN_PARENT_LEFT,ALIGN_PARENT_RIGHT,CENTER_HORIZONTAL}

    private Handler mPlayHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            mPlayHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, mPlayDelayTime);
        }
    };


    public void setPlaySpeed(int playDelayTime){
        mPlayDelayTime = playDelayTime <= 0 ? 3000 : playDelayTime;
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mCurrentPosition = position % mImageUrls.size();
            switchToPoint(mCurrentPosition);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    public ZhlBanner(Context context) {
        this(context,null);
    }

    public ZhlBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    private void initViews(Context context){
        mViewPager = new BannerViewPager(context);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        addView(mViewPager, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        RelativeLayout pointContainerRl = new RelativeLayout(context);
        pointContainerRl.setPadding(0, 10, 0, 10);
        LayoutParams pointContainerLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(pointContainerRl, pointContainerLp);
        mPointContainerLayout = new LinearLayout(context);
        mPointContainerLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams mPointRealContainerParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pointContainerRl.addView(mPointContainerLayout, mPointRealContainerParams);
        if (mPointContainerLayout != null) {
            mPointContainerLayout.setVisibility(mIsPointVisible ? View.VISIBLE : View.GONE);
        }
        setPointAlign(PageIndicatorAlign.CENTER_HORIZONTAL);
        initViewPagerScroll(mScrollSpeed);
    }

    public void setPointAlign(PageIndicatorAlign align){
        mPointContainerParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mPointContainerParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,align == PageIndicatorAlign.ALIGN_PARENT_LEFT ? RelativeLayout.TRUE : 0);
        mPointContainerParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, align == PageIndicatorAlign.ALIGN_PARENT_RIGHT ? RelativeLayout.TRUE : 0);
        mPointContainerParams.addRule(RelativeLayout.CENTER_HORIZONTAL, align == PageIndicatorAlign.CENTER_HORIZONTAL ? RelativeLayout.TRUE : 0);
        mPointContainerLayout.setLayoutParams(mPointContainerParams);
    }

    public void setIndicatorVisible(boolean isVisible){
        this.mIsPointVisible = isVisible;
        if (mPointContainerLayout != null) {
            mPointContainerLayout.setVisibility(mIsPointVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setOnBannerItemClickListener(OnBannerItemClickListener listener){
        if (mPagerAdapter != null){
            mPagerAdapter.setOnBannerItemClickListener(listener);
        }
    }

    public void setPageTransformType(ViewPager.PageTransformer transformer){
        mViewPager.setPageTransformer(true,transformer);
    }

    public void setScrollSpeed(int scrollSpeed){
        this.mScrollSpeed = scrollSpeed;
        initViewPagerScroll(scrollSpeed);
    }

    public void setImageUrls(ArrayList<String> imageUrls){
        if (imageUrls == null){
            return;
        }
        if (imageUrls.size() == 0){
            mIsOneImg = true;
        }

        mImageUrls  = imageUrls;
        setViewPagerData();
    }

    private int computeStartIndex(){
        int start = mPagerAdapter.getCount()/2;
        int offset = start % mImageUrls.size();
        return start - offset;
    }

    private void setViewPagerData() {
        if(!mIsOneImg) {
            addPoints();
        }
        mPagerAdapter = new BannerChangeAdapter(getContext(),mImageUrls);
        mViewPager.setAdapter(mPagerAdapter);
        if (!mIsOneImg) {
            startAutoPlay();
        }
        mViewPager.setCurrentItem(computeStartIndex());
    }

    private void addPoints() {
        mPointContainerLayout.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        ImageView imageView;
        for (int i = 0; i < mImageUrls.size(); i++) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(lp);
            imageView.setImageResource(R.mipmap.ic_page_indicator);
            mPointContainerLayout.addView(imageView);
        }

        switchToPoint(0);
    }

    private void switchToPoint(final int currentPoint) {
        for (int i = 0; i < mPointContainerLayout.getChildCount(); i++) {
            if (mPointContainerLayout.getChildAt(i) instanceof ImageView){
                ((ImageView) mPointContainerLayout.getChildAt(i)).setImageResource(i == currentPoint?R.mipmap.ic_page_indicator_focused : R.mipmap.ic_page_indicator);
            }
        }
    }

    public void startAutoPlay(){
        if (mIsNeedAutoPaly && !mIsPlaying){
            mPlayHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, mPlayDelayTime);
            mIsPlaying = true;
        }
    }
    public void stopAutoPlay(){
        if (mIsNeedAutoPaly && mIsPlaying) {
            mIsPlaying = false;
            mPlayHandler.removeMessages(MESSAGE_WHAT);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsNeedAutoPaly && !mIsOneImg) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    startAutoPlay();
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void initViewPagerScroll(int scrollSpeed) {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller viewPagerScroller = new ViewPagerScroller(getContext());
            viewPagerScroller.setDuration(scrollSpeed);
            mScroller.set(mViewPager, viewPagerScroller);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
