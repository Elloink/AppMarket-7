package dle.appmarket.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import dle.appmarket.R;
import dle.appmarket.Bean.Special;

/**
 * Created by dle on 2016/9/22.
 */
public class ThemAdapter extends BaseAdapter
{
    private List<Special> specials;
    private Context context;
    private BitmapUtils bit;

    public ThemAdapter(Context context, List<Special> specials)
    {
        this.context = context;
        this.specials = specials;
        bit = new BitmapUtils(context);
    }

    @Override
    public int getCount()
    {
        return specials.size();
    }

    @Override
    public Object getItem(int position)
    {
        return specials.get(position);
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
        if (convertView!=null)
            v = convertView;
        else v=LayoutInflater.from(context).inflate(R.layout.item_them,parent,false);

        MyViewHolde myViewHolde = (MyViewHolde) v.getTag();
        if (myViewHolde==null)
            myViewHolde = new MyViewHolde();

        myViewHolde.img = (ImageView) v.findViewById(R.id.them_pic);
        myViewHolde.text = (TextView) v.findViewById(R.id.them_name);

        bit.display(myViewHolde.img,specials.get(position).getUrl());

        myViewHolde.text.setText(specials.get(position).getDes());

        myViewHolde.img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(context,"当前点击的是:"+specials.get(position).getDes(),Toast.LENGTH_SHORT).show();
            }
        });


        v.setTag(myViewHolde);




        return v;
    }


    private class MyViewHolde
    {
        ImageView img;
        TextView text;
    }
}
