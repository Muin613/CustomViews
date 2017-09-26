package com.mh.customviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.mh.customviews.Banner.IMHBannerImageLoaderListener;
import com.mh.customviews.Banner.IMHBannerVH;
import com.mh.customviews.Banner.MHBanner;
import com.mh.customviews.Banner.MHBannerData;
import com.mh.customviews.Banner.Transform.CoverModeTransformer;
import com.mh.customviews.Banner.Transform.X3DTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class BannerTestAct extends AppCompatActivity implements IMHBannerImageLoaderListener, IMHBannerVH {
    MHBanner banner;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_banner);
        banner = (MHBanner) findViewById(R.id.banner);
        List<MHBannerData> data = new ArrayList<>();
        MHBannerData D1 = new MHBannerData().setImg("1").setResId(R.drawable.sm1);
        MHBannerData D2 = new MHBannerData().setImg("2").setResId(R.color.colorPrimary);
        MHBannerData D3 = new MHBannerData().setImg("3").setResId(R.color.colorAccent);
        data.add(D1);
        data.add(D2);
        data.add(D3);
        data.add(D3);
        banner.setData(data).setLoaderListener(this).setTransform(true, new X3DTransformer()).setBannerItem(this).build();

    }

    @Override
    public void onLoadImage(MHBannerData data, ImageView imgView, View parentView) {
        System.out.println("shuju" + data.getImgStr());
        imgView.setBackgroundResource(data.getResId());
    }

    @Override
    public void onBannerClick(int position, MHBannerData data, ImageView imgView, View parentView) {
        System.out.println("dianji" + position+" "+data.getImgStr());
    }



    @Override
    public View getBannerItemView() {
        view = LayoutInflater.from(this).inflate(R.layout.item_banner, null);
        return view;
    }

    @Override
    public ImageView getImageView() {
        return (ImageView) view.findViewById(R.id.image);
    }
}

