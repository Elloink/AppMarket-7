package dle.appmarket.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dle.appmarket.R;
import dle.appmarket.Bean.AppInfo;

/**
 * Created by dle on 2016/9/21.
 */
public class MyListViewAdapter extends BaseAdapter
{
    private Context context;
    private List<Map<String, List<AppInfo>>> maps;

    private List<String> list;

    public MyListViewAdapter(Context context, List<Map<String, List<AppInfo>>> map)
    {
        this.context = context;
        this.maps = map;
    }

    @Override
    public int getCount()
    {
        return maps.size();
    }

    @Override
    public Object getItem(int position)
    {
        return maps.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = null;
        if (convertView != null)
        {
            v = convertView;
        } else
        {
            v = LayoutInflater.from(context)
                    .inflate
                            (
                                    R.layout.type_listview_item,
                                    parent,
                                    false
                            );
        }

        MyViewHolder viewHolder = (MyViewHolder) v.getTag();
        if (viewHolder==null)
            viewHolder = new MyViewHolder();

        viewHolder.textView = (TextView) v.findViewById(R.id.type_notice);
        viewHolder.gridView = (GridView) v.findViewById(R.id.type_grid);



        //
        viewHolder.textView.setTag(position);
        viewHolder.gridView.setTag(position);

        final int po =(Integer)viewHolder.gridView.getTag();
        viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.i("haha",po+"...."+position);
                Toast.makeText(context,"当前点击的是第"+po+"组，第"+position+"列",Toast.LENGTH_SHORT).show();

            }
        });
        /*
         保存holder;
        */
        v.setTag(viewHolder);

        list = new ArrayList<>();
        Set keyset = maps.get(position).keySet();
        Iterator it = keyset.iterator();
        while (it.hasNext())
        {
            String key = (String) it.next();
            list.add(key);
        }

        //按道理应该是 2.
        Log.i("haha","键名获取完成 大小是："+list.size());


        for (int i=0;i<list.size();i++)
        {
            viewHolder.textView.setText(list.get(i));
            viewHolder.gridView.setAdapter(new GridViewAdapter(context,maps.get(position).get(list.get(i))));
        }






        return v;
    }


    private class MyViewHolder
    {
        GridView gridView;
        TextView textView;
    }
}
