package dle.appmarket.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;

import dle.appmarket.R;
import dle.appmarket.View.KeywordsFlow;
import dle.appmarket.View.KeywordsFlowView;

/**
 * Created by dle on 2016/9/19.
 */
public class GroomFragment extends Fragment
{
    private KeywordsFlowView mykeywords;
    private String[] keywords ;

    private SharedPreferences sh;
    private SharedPreferences.Editor ed;
    private String Ip;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_groom,container,false);
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
        String url = "http://"+Ip+":8080/GooglePlayServer/recommend";
        HttpUtils https = new HttpUtils();
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("index","0");

        https.send(HttpRequest.HttpMethod.POST, url, rp, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                try
                {
                    JSONArray js = new JSONArray(responseInfo.result);
                    keywords = new String[js.length()];

                    for (int i=0;i<js.length();i++)
                    {
                        keywords[i] = js.optString(i);
                    }
                    Log.i("haha","推荐 。。。数据解析成功");

                    //开始展示
                    mykeywords.show(keywords, KeywordsFlow.ANIMATION_IN);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s)
            {

            }
        });

    }

    private void BindsViews()
    {
        mykeywords = (KeywordsFlowView) getActivity().findViewById(R.id.keywordsflowview);

        //设置每次随机飞入文字的个数
        mykeywords.setTextShowSize(15);
        //设置是否允许滑动屏幕切换文字
        mykeywords.shouldScroolFlow(true);


        mykeywords.setOnItemClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(),((TextView)v).getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });


        //文字随机飞入
        //文字随机飞出
//        mykeywords.show(keywords,KeywordsFlow.ANIMATION_IN);
//        mykeywords.show(keywords,KeywordsFlow.ANIMATION_OUT);

        sh = getActivity().getSharedPreferences("dle",getActivity().MODE_PRIVATE);

        Ip = sh.getString("Ip","");

    }
}
