package dle.appmarket.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dle.appmarket.Adapter.AllThreeAdapter;
import dle.appmarket.Bean.Home;
import dle.appmarket.R;
import dle.appmarket.View.ArrowDownloadButton;
import dle.appmarket.View.XListView.XListView;

/**
 * Created by dle on 2016/9/29.
 */
public class MyHomeFragment extends Fragment implements View.OnClickListener
{

    private XListView myHomeListView;
    private List<Home> mDates;
    private AllThreeAdapter allThreeAdapter;

    private SharedPreferences sharedPreferences;
    private ProgressDialog pd;
    private String Ip;

    private Boolean isDown = false;

    private HttpUtils http;
    private HttpHandler<File> hand;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_myhome, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mDates = new ArrayList<>();
        myHomeListView = (XListView) getActivity().findViewById(R.id.my_listview);
        allThreeAdapter = new AllThreeAdapter(getActivity(), mDates);
        myHomeListView.setAdapter(allThreeAdapter);


        sharedPreferences = getActivity().getSharedPreferences("dle", getActivity().MODE_PRIVATE);
        Ip = sharedPreferences.getString("Ip", "");

        pd = new ProgressDialog(getActivity());
        pd.setMessage("加载中");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        BindsDates();

    }

    private void BindsDates()
    {
        HttpUtils htt = new HttpUtils();
        String url = "http://" + Ip + ":8080/GooglePlayServer/home";
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("index", 0 + "");

        htt.send(HttpRequest.HttpMethod.POST, url, rp, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String result = responseInfo.result;

                Log.i("haha","Home...1111");

                try
                {
                    JSONObject re = new JSONObject(result);
                    Log.i("haha","Home...113311");

                    JSONArray list = re.optJSONArray("list");
                    Log.i("haha","Home...11113333");

                    for (int i = 0; i < list.length(); i++)
                    {
                        JSONObject obj = list.optJSONObject(i);
                        Home home = new Home
                                (
                                        obj.getString("id"),
                                        obj.getString("name"),
                                        obj.getString("packageName"),
                                        "http://"+Ip+":8080/GooglePlayServer/image?name="
                                                +obj.getString("iconUrl"),
                                        obj.getString("stars"),
                                        obj.getString("size"),
                                        obj.getString("downloadUrl"),
                                        obj.getString("des"),
                                        false
                                );
                        mDates.add(home);
                    }
                    allThreeAdapter.notifyDataSetChanged();

                    if (pd.isShowing())
                        pd.cancel();

                } catch (JSONException e)
                {
                    e.printStackTrace();

                    Log.i("haha","sb");
                }
            }

            @Override
            public void onFailure(HttpException e, String s)
            {

            }
        });

    }


    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.three_item_download:

                Object tag = v.getTag();

                if (tag != null && tag instanceof Integer)
                {
                    int position = (int) tag;

                    isDown = mDates.get(position).getDown();

                    String ip = "http://"
                            + Ip
                            + ":8080/GooglePlayServer/download?name="
                            + mDates.get(position).getDownloadUrl();

                    File file = new File(Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/APPDownLoad/app");
                    if (!file.exists())
                        file.mkdirs();
                    File EndFile = new File(file, mDates.get(position).getName() + ".apk");

                    String path = EndFile + "";

                    DownLoadApp(ip, path, (ArrowDownloadButton) v);

                }


                break;
            default:
                break;
        }
    }


    /**
     * 下载方法
     *
     * @param ip   下载地址
     * @param path 存储路径
     * @param btn  progressbtn
     */
    private void DownLoadApp(String ip, String path, final ArrowDownloadButton btn)
    {
        HttpUtils http = new HttpUtils();

        btn.startAnimating();

        if (!isDown)
        {
            isDown = true;
            //第一个参数:下载地址
            //第二个参数:文件存储路径
            //第三个参数:是否断点续传
            //第四个参数:是否重命名
            //第五个参数:请求回调

            hand = http.download(ip, path, true, false, new RequestCallBack<File>()
            {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo)
                {
                    btn.reset();
                }

                @Override
                public void onFailure(HttpException e, String s)
                {
                    btn.setProgress(0.0f);
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading)
                {
                    super.onLoading(total, current, isUploading);

                    float process = (float) current / (float) total;

                    btn.setProgress(process);
                }
            });
        } else
        {
            hand.cancel();
            isDown = false;
        }


    }
}
