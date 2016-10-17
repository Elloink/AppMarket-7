package dle.appmarket.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import dle.appmarket.View.XListView.XListView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dle.appmarket.R;
import dle.appmarket.Bean.AppInfo;
import dle.appmarket.Adapter.MyListViewAdapter;

/**
 * Created by dle on 2016/9/19.
 */
public class TypeFragment extends Fragment implements XListView.IXListViewListener
{
    private XListView listview;
    private MyListViewAdapter adapter;

    private List<Map<String,List<AppInfo>>> mDates;

    private String TT = "LoadTimes";

    //数据获取 ---Ip获取
    private SharedPreferences sh;
    private String Ip;
    private SharedPreferences.Editor ed;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_type,container,false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        BindsViews();

        BindsDates();


    }


    private void LoadDatas(final int index)
    {

        ed.putInt(TT,GetLoadTimes()+1);
        ed.commit();

        String url = "http://"+Ip+":8080/GooglePlayServer/category";
        RequestParams rps = new RequestParams();
        rps.addBodyParameter("index",index+"");

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,url, rps, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String result = responseInfo.result;
                Log.i("haha","result:"+responseInfo.result);

                try
                {
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i=0;i<jsonArray.length();i++)
                    {

                        final Map<String,List<AppInfo>> map = new HashMap<>();
                        final List<AppInfo> list  = new ArrayList<>();

                        JSONObject jsonObject = jsonArray.optJSONObject(i);


                        String title = jsonObject.getString("title");
                        Log.i("haha","title"+title);

                        JSONArray infosArray = jsonObject.optJSONArray("infos");


                        for (int j = 0;j<infosArray.length();j++)
                        {

                            Log.i("haha","length"+infosArray.length());

                            JSONObject root = infosArray.optJSONObject(j);
                            for (int k=1;k<4;k++)
                            {
                                String name = root.getString("name"+k);
                                String url = "http://"+Ip+":8080/GooglePlayServer/image?name="+root.getString("url"+k);
                                AppInfo info = new AppInfo(title,url,name);
                                list.add(info);
                                Log.i("haha","Url:++"+url);
                            }
                            map.put(title,list);
                            Log.i("haha","Map size"+map.size());
                        }
                        mDates.add(map);

                        adapter.notifyDataSetChanged();


                        onLoad();

                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                    Log.i("haha","解析失败");
                }
            }


            @Override
            public void onFailure(HttpException e, String s)
            {
                Log.i("haha","连接失败");

                Toast.makeText(getActivity(),"检查网络。。。",Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void BindsDates()
    {

        LoadDatas(GetLoadTimes());


        mDates = new ArrayList<>();


        adapter = new MyListViewAdapter(getActivity(),mDates);

        listview.setAdapter(adapter);

    }

    private int GetLoadTimes()
    {
        sh = getActivity().getSharedPreferences("dle",getActivity().MODE_PRIVATE);

        return sh.getInt(TT,0);
    }

    private void BindsViews()
    {
        sh = getActivity().getSharedPreferences("dle",getActivity().MODE_PRIVATE);

        Ip = sh.getString("Ip","");

        ed = sh.edit();

        ed.putInt(TT,0);

        ed.commit();



        Log.i("haha",Ip.toString());



        listview = (XListView) getActivity().findViewById(R.id.type_listview);

        listview.setPullLoadEnable(true);
        listview.setPullRefreshEnable(true);

        listview.setXListViewListener(this);



    }

    @Override
    public void onRefresh()
    {
        LoadDatas(0);
        Toast.makeText(getActivity(),"刷新..",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore()
    {
        LoadDatas(GetLoadTimes());
        Toast.makeText(getActivity(),"加载.."+GetLoadTimes(),Toast.LENGTH_SHORT).show();
    }

    private void onLoad() {
        listview.stopRefresh();
        listview.stopLoadMore();
        listview.setRefreshTime("刚刚");
    }

}
