package dle.appmarket.Fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Random;

import dle.appmarket.R;
import dle.appmarket.View.tagview.OnTagClickListener;
import dle.appmarket.View.tagview.Tag;
import dle.appmarket.View.tagview.TagView;

/**
 * Created by dle on 2016/9/19.
 */
public class PaihangFragment extends Fragment
{
    private SharedPreferences sh;
    private TagView tag;
    private String Ip;

    private ArrayList<String> colors = new ArrayList<>();
    private String[] mDates;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_paihang, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        BindsViews();

        BindsDatas();
    }

    private void BindsDatas()
    {

        String url = "http://" + Ip + ":8080/GooglePlayServer/hot";
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("index", "0");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, rp, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                try
                {
                    JSONArray js = new JSONArray(responseInfo.result);
                    mDates = new String[js.length()];

                    for (int i = 0; i < js.length(); i++)
                    {
                        mDates[i] = js.optString(i);
                        Tag ta = new Tag(js.optString(i));
                        ta.radius = 19f;
                        ta.layoutColor =Color.parseColor( colors.get(new Random().nextInt(colors.size())));
                        tag.addTag(ta);


                    }


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
        sh = getActivity().getSharedPreferences("dle", getActivity().MODE_PRIVATE);
        Ip = sh.getString("Ip", "");
        tag = (TagView) getActivity().findViewById(R.id.paihang_tagview);

        tag.setOnTagClickListener(new OnTagClickListener()
        {
            @Override
            public void onTagClick(int position, Tag tag)
            {
                Toast.makeText(getActivity(),mDates[position]+"",Toast.LENGTH_SHORT ).show();
            }
        });


        colors.add("#ED7D31");
        colors.add("#00B0F0");
        colors.add("#FF0000");
        colors.add("#D0CECE");
        colors.add("#00B050");
        colors.add("#9999FF");
        colors.add("#FF5FC6");
        colors.add("#FFC000");
        colors.add("#7F7F7F");
        colors.add("#4800FF");


    }
}
