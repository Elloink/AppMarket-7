package dle.appmarket.Adapter;

/**
 * Created by dle on 2016/9/21.
 */

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import dle.appmarket.R;
import dle.appmarket.Bean.AppInfo;

public class GridViewAdapter extends BaseAdapter
{
    private Context mContext;
    private List<AppInfo> mList;
    private BitmapUtils bitu ;
    private OnClickListener imgclick;

    //设置回调接口
    public void setImgclick(OnClickListener imgclick)
    {
        this.imgclick = imgclick;
    }

    public GridViewAdapter(Context mContext, List<AppInfo> mList)
    {
        super();
        this.mContext = mContext;
        this.mList = mList;
        bitu = new BitmapUtils(mContext);

    }

    @Override
    public int getCount()
    {
        if (mList == null)
        {
            return 0;
        } else
        {
            return this.mList.size();
        }
    }

    @Override
    public Object getItem(int position)
    {
        if (mList == null)
        {
            return null;
        } else
        {
            return this.mList.get(position);
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        View v = null;

        if (convertView != null)
            v = convertView;
        else
            v = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.type_gridview_item, parent, false);

        MyGridViewViewHolder viewHolder = (MyGridViewViewHolder) v.getTag();
        if (viewHolder == null)
            viewHolder = new MyGridViewViewHolder();

        viewHolder.text = (TextView) v.findViewById(R.id.type_grid_text);
        viewHolder.img = (ImageView) v.findViewById(R.id.type_grid_img);

        viewHolder.text.setText(mList.get(position).getName());

        try
        {
            bitu.display(viewHolder.img, mList.get(position).getUrl());
        } catch (Exception e)
        {
            viewHolder.img.setImageResource(R.drawable.xxgg);
            Log.i("haha", "网络获取图片出错  显示默认图片");
        }

        //区分点击的是哪一张图
        viewHolder.img.setTag(position);
        viewHolder.text.setTag(position);


        v.setLayoutParams(new AbsListView.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)));

        v.setBackgroundColor(Color.WHITE); //设置背景颜色




        //保存ViewHolder
        v.setTag(viewHolder);

        return v;
    }

    private class MyGridViewViewHolder
    {
        ImageView img;
        TextView text;
    }
}

