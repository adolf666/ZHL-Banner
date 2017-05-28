package com.adolf.zhl_banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by adolf on 2017/5/20.
 */

public class BannerChangeAdapter extends PagerAdapter {

    private ArrayList<String> mDataList;
    private Context mContext;
    private ArrayList<ImageView> mViews;
    private OnBannerItemClickListener mBannerItemClickListener;

    public BannerChangeAdapter(Context context, ArrayList<String> dataSource) {
        this.mContext = context;
        this.mDataList = dataSource;
        initViews();
    }

    private void initViews() {
        mViews = new ArrayList<>();
        if (mDataList == null){
            return;
        }
        for (int i = 0; i < mDataList.size(); i++) {
            mViews.add(new ImageView(mContext));
        }
    }

    public void setOnBannerItemClickListener(OnBannerItemClickListener listener){
        this.mBannerItemClickListener = listener;
    }

    private ImageView getImageView(final int position) {
        ImageView imageView = mViews.get(position);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext).load(mDataList.get(position)).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBannerItemClickListener != null){
                    mBannerItemClickListener.onBannerItemClick(position,mDataList.get(position));
                }
            }
        });
        return imageView;
    }

    @Override public int getCount() {
        return mDataList == null || mDataList.size() == 0 ? 0 : Integer.MAX_VALUE;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = position % mDataList.size();
        if (realPosition<0){
            realPosition = mDataList.size()+position;
        }
        ImageView imageView = getImageView(realPosition);
        ViewGroup parentView = (ViewGroup) imageView.getParent();
        if (parentView != null){
            parentView.removeView(imageView);
        }
        container.addView(imageView);
        return imageView;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
    }

}