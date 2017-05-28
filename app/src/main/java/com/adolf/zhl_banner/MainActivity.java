package com.adolf.zhl_banner;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.adolf.zhl_banner.transformer.SqueezeTransformer;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<String> mImageList = new ArrayList<>();
    private ZhlBanner mBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();
        setBannerData();
    }

    private void initList(){
        mImageList.add("http://img4.imgtn.bdimg.com/it/u=2430963138,1300578556&fm=23&gp=0.jpg");
        mImageList.add("http://img1.imgtn.bdimg.com/it/u=2755648979,3568014048&fm=23&gp=0.jpg");
        mImageList.add("http://img0.imgtn.bdimg.com/it/u=2272739960,4287902102&fm=23&gp=0.jpg");
        mImageList.add("http://img3.imgtn.bdimg.com/it/u=1078051055,1310741362&fm=23&gp=0.jpg");
    }

    private void setBannerData(){
        mBanner = (ZhlBanner) findViewById(R.id.banner);
        mBanner.setImageUrls(mImageList);
        mBanner.setIndicatorVisible(true);
        mBanner.setPlaySpeed(3000);
        mBanner.setScrollSpeed(1000);
        mBanner.setPageTransformType(new SqueezeTransformer());
        mBanner.setOnBannerItemClickListener(new OnBannerItemClickListener() {
            @Override
            public void onBannerItemClick(int position, String url) {
                Toast.makeText(MainActivity.this,"you have clicked position " +position,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
