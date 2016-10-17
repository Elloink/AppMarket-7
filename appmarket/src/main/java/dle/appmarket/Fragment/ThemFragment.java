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
import java.util.List;

import dle.appmarket.Bean.Special;
import dle.appmarket.Adapter.ThemAdapter;
import dle.appmarket.View.XListView.XListView;

import dle.appmarket.R;

/**
 * Created by dle on 2016/9/19.
 */
public class ThemFragment extends Fragment implements XListView.IXListViewListener
{
    private XListView xListView;
    private ThemAdapter adapter;

    private String Ip;
    private SharedPreferences sh;
    private SharedPreferences.Editor ed;

    private List<Special> mDates;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_them,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        BindsViews();
        
        BindsData();
    }

    private void BindsData()
    {


        LoadDates();


        adapter = new ThemAdapter(getActivity(),mDates);

        xListView.setAdapter(adapter);



    }

    private void LoadDates()
    {

        String url = "http://"+Ip+":8080/GooglePlayServer/subject";
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("index",0+"");

        HttpUtils http = new HttpUtils();

        http.send(HttpRequest.HttpMethod.POST, url, rp, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String resule = responseInfo.result;

                try
                {
                    JSONArray arr = new JSONArray(resule);
                    for (int i=0;i<arr.length();i++)
                    {
                        JSONObject obj = arr.optJSONObject(i);
                        String name = obj.getString("des");
                        String url ="http://"+Ip+":8080/GooglePlayServer/image?name="+obj.getString("url");
                        Special sp = new Special(name,url);
                        mDates.add(sp);
                    }
                    adapter.notifyDataSetChanged();

                    onLoad();


                } catch (JSONException e)
                {
                    e.printStackTrace();
                    Log.i("haha","解析失败  them");
                }


            }

            @Override
            public void onFailure(HttpException e, String s)
            {
                Toast.makeText(getActivity(),"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void BindsViews()
    {
        xListView = (XListView) getActivity().findViewById(R.id.them_listview);
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);

        sh = getActivity().getSharedPreferences("dle",getActivity().MODE_PRIVATE);

        Ip = sh.getString("Ip","");

        mDates = new ArrayList<Special>();

    }

    @Override
    public void onRefresh()
    {
        LoadDates();
        Toast.makeText(getActivity(),"刷新..",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore()
    {
        LoadDates();
        Toast.makeText(getActivity(),"加载..",Toast.LENGTH_SHORT).show();
    }

    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
        xListView.setRefreshTime("刚刚");
    }


}
