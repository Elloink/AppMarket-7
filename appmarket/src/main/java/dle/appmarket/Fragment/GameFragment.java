package dle.appmarket.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import dle.appmarket.R;
import dle.appmarket.entity.HomeEntity;
import dle.appmarket.View.DownloadView;
import dle.appmarket.mulitipe.InputStream2String;
import dle.appmarket.mulitipe.InstallUtils;

/**
 * Created by zsl on 2016/9/22.
 */
public class GameFragment extends Fragment
{
    private static final String TAG = GameFragment.class.getSimpleName();
    private ListView lv_content;
    private ArrayList<HomeEntity> al;
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private String url;

    //用来保存 下载状态的数组
    private int[] stateList;
    private int count;
    private MyAdapter adapter;
    private  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter=new MyAdapter();
                    lv_content.setAdapter(adapter);
                    break;
                case 0:
                    ((DownloadView)(lv_content.getChildAt(msg.arg2).findViewById(R.id.dv_state))).setProgress(msg.arg1);
                    ((TextView)(lv_content.getChildAt(msg.arg2).findViewById(R.id.tv_progress))).setText(msg.arg1+"%");
                    break;
                case -1:
                    Toast.makeText(getActivity(), "哎呀,网络挂掉了!请稍后再试.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_game,null);
        al=new ArrayList<>();
        mContext = getActivity();
        sharedPreferences = mContext.getSharedPreferences("dle",mContext.MODE_PRIVATE);
        url = "http://"+sharedPreferences.getString("Ip","")+":8080/GooglePlayServer/";
        lv_content= (ListView) view.findViewById(R.id.lv_game_content);
        initData2(url+"game?index=1");
        return view;
    }
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return al.size();
        }
        @Override
        public Object getItem(int position) {
            return al.get(position);
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            BitmapUtils utils = new BitmapUtils(mContext);
            final HttpHandler<File> handler=null;
            View view ;
            if (convertView != null) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(mContext, R.layout.main_list_item, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                holder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
                holder.tv_size = (TextView) view.findViewById(R.id.tv_size);
                holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                holder.ll_star = (LinearLayout) view.findViewById(R.id.ll_star);
                holder.dv_state = (DownloadView) view.findViewById(R.id.dv_state);
                holder.tv_progress= (TextView) view.findViewById(R.id.tv_progress);
                holder.btn_open= (Button) view.findViewById(R.id.btn_open);
                view.setTag(holder);
            }

            final dle.appmarket.entity.HomeEntity homeEntity = al.get(position);

            holder.tv_name.setText(homeEntity.getName());
            holder.tv_desc.setText(homeEntity.getDes());
            holder.tv_size.setText(Formatter.formatFileSize(getActivity(), homeEntity.getSize()));
            utils.display(holder.iv_icon, url+"image?name=" + homeEntity.getIconUrl());
            ImageView iv ;
            holder.ll_star.removeAllViews();
            for (int i = 0; i < 5; i++) {
                iv = new ImageView(mContext);
                if (i < homeEntity.getStars() - 1) {
                    iv.setImageResource(R.drawable.rate_star_small_on);
                } else {
                    iv.setImageResource(R.drawable.rate_star_small_off);
                }

                holder.ll_star.addView(iv);
            }
            //如果已经安装

            if(InstallUtils.isInstalled(mContext,homeEntity.getPackageName())){
                holder.btn_open.setVisibility(View.VISIBLE);
                holder.dv_state.setVisibility(View.INVISIBLE);
                holder.tv_progress.setVisibility(View.INVISIBLE);
                holder.btn_open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InstallUtils.openApp(mContext,homeEntity.getPackageName());
                    }
                });
            }else {
                //未安装
                holder.btn_open.setVisibility(View.INVISIBLE);
                holder.dv_state.setVisibility(View.VISIBLE);
                holder.tv_progress.setVisibility(View.VISIBLE);
                holder.dv_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Todo  开启下载

                        if (stateList[position] != 1) {
                            String url1 = url + "download?name=" + homeEntity.getDownloadUrl();
                            download(position, url1, homeEntity.getPackageName(), handler, holder);
                        }
                        stateList[position] = 1;
                    }
                });
            }

            return view;
        }
    }

    private void download(final int pos, final String url, final String packName, HttpHandler<File> httpHandler, final ViewHolder holder) {

        final String target= Environment.getExternalStorageDirectory().toString()+"/"+packName;
        //boolean isDownloading=true;
        HttpUtils utils=new HttpUtils();
        utils.configRequestThreadPoolSize(3);//设置由几条线程进行下载
        if(true){
            httpHandler = utils.download(url, target, true, false, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Toast.makeText(mContext,"下载成功!",Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    builder.setTitle("是否安装?").setMessage(holder.tv_name.getText()).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            InstallUtils.install(Environment.getExternalStorageDirectory()+"/"+packName,getActivity());
                            holder.btn_open.setVisibility(View.VISIBLE);
                            holder.tv_progress.setVisibility(View.INVISIBLE);
                            holder.dv_state.setVisibility(View.INVISIBLE);

                        }
                    });
                    builder.show();

                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    Toast.makeText(getActivity(),"下载失败!",Toast.LENGTH_LONG).show();
                    holder.dv_state.invalidate();

                }

                @Override
                public void onStart() {
                    super.onStart();
                    Toast.makeText(mContext,"开始下载!",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    Log.i(TAG, "onLoading: "+total+"current:  "+current);
                    Message msg = Message.obtain();
                    msg.what =0;
                    msg.arg1=(int)(current*100/total);
                    msg.arg2=pos;
                    handler.sendMessage(msg);

                }
            });

        }else {

       /*     httpHandler.cancel();
            isDownloading=false;*/
        }


    }

    private static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_size;
        TextView tv_desc;
        LinearLayout ll_star;
        DownloadView dv_state;
        TextView tv_progress;
        Button btn_open;
    }

    private void initData2(final String urlStr) {
        al.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                int code = 0;
                try {
                    URL url = new URL(urlStr);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    conn.connect();
                    code = conn.getResponseCode();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (code == 200) {

                    InputStream in = null;
                    try {
                        in = conn.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String message = null;
                    try {
                        message = InputStream2String.read(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    conn.disconnect();
                    Gson gson = new Gson();
                    try {
                        JSONArray list = new JSONArray(message);

                        int length = list.length();
                        count = length;
                        stateList = new int[count];

                        for (int i = 0; i < length; i++) {
                            JSONObject jsonObject = list.getJSONObject(i);
                            HomeEntity homeEntity = gson.fromJson(jsonObject.toString(), HomeEntity.class);
                            al.add(homeEntity);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.sendMessage(msg);


                } else {
                    //联网失败
                    Message msg = Message.obtain();
                    msg.what =-1;
                    handler.sendMessage(msg);
                }
            }
        }).start();

    }

}
