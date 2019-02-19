package com.cdx.example.picasso;

import android.view.View;

import com.cdx.example.picasso.activity.LoadPictureSaveTOSDActivity;

import java.util.ArrayList;

import apis.amapv2.com.listviewlibrary.activity.BaseListActivty;
import apis.amapv2.com.listviewlibrary.bean.ItemObject;

public class MainActivity extends BaseListActivty {

    @Override
    protected void initData() {
        mTvTitle.setText("Picasso的使用");
        mTvTitle.setVisibility(View.VISIBLE);

        ArrayList<ItemObject> data = new ArrayList<>();
        data.add(new ItemObject("Picasso加载网络图片，并且将图片缓存到本地",LoadPictureSaveTOSDActivity.class));
        mMyListView.setData(data);
    }
}
