package dle.appmarket.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;
import java.util.zip.Inflater;

import dle.appmarket.Bean.Home;
import dle.appmarket.R;
import dle.appmarket.View.ArrowDownloadButton;

/**
 * Created by dle on 2016/9/29.
 */
public class AllThreeAdapter extends BaseAdapter
{
    private Context context;
    private List<Home> mDates;

    private BitmapUtils bit;

    private View.OnClickListener onDownload;

    public AllThreeAdapter(Context context, List<Home> mDates)
    {
        this.context = context;
        this.mDates = mDates;
    }

    public void setOnDownload(View.OnClickListener onDownload)
    {
        this.onDownload = onDownload;
    }

    @Override
    public int getCount()
    {
        return mDates.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mDates.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v;

        bit = new BitmapUtils(context);

        if (convertView==null)
        {
            v = LayoutInflater.from(context).inflate(R.layout.all_three_item,parent,false);
        }
        else
         v = convertView;

        MyHolder myholder = (MyHolder) v.getTag();
        if (myholder==null)
            myholder = new MyHolder();

        myholder.appicon = (ImageView) v.findViewById(R.id.three_item_icon);
        myholder.appname = (TextView) v.findViewById(R.id.three_item_name);
        myholder.ratingBar = (RatingBar) v.findViewById(R.id.three_item_ratting);
        myholder.appsize = (TextView) v.findViewById(R.id.three_item_size);
        myholder.summary = (TextView) v.findViewById(R.id.three_item_summary);
        myholder.down  = (ArrowDownloadButton) v.findViewById(R.id.three_item_download);


        bit.display(myholder.appicon,mDates.get(position).getIconUrl());
        myholder.appname.setText(mDates.get(position).getName());
        myholder.ratingBar.setRating(Float.parseFloat(mDates.get(position).getStars()));
        myholder.appsize.setText(new java.text.DecimalFormat("#.00").
                format((Float.parseFloat(mDates.get(position).getSize()) / (1024 * 1024))) + "MB");
        myholder.summary.setText(mDates.get(position).getDes());

        myholder.down.setOnClickListener(onDownload);

        myholder.summary.setTag(position);

        v.setTag(myholder);



        return v;
    }


    public class MyHolder
    {
        private ImageView appicon;
        private TextView appname;
        private TextView appsize;
        private RatingBar ratingBar;
        private ArrowDownloadButton down;
        private TextView summary;


    }



}
