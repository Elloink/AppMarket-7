package dle.appmarket.entity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by dle on 2016/9/26.
 */
public class pageadapter extends PagerAdapter
{
    private String[] img;
    private Context mContext;

    public pageadapter(String[] img, Context mContext)
    {
        this.img = img;
        this.mContext = mContext;
    }

    @Override
    public int getCount()
    {
        return img.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return false;
    }
}
