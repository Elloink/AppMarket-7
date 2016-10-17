package dle.appmarket.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
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

import dle.appmarket.R;
import dle.appmarket.Bean.AboutAppinfo;

/**
 * Created by dle on 2016/9/23.
 */
public class AboutApp extends AppCompatActivity implements View.OnClickListener
{
    //初始化控件
    private ImageView icon;
    private TextView appname;
    private RatingBar ratingBar;
    private TextView downloadNum, time, version, size;
    private ImageView hide1, hide2;
    private TextView aboutapp;
    private ViewPager myviewpager;
    private LinearLayout ly;

    private Toolbar toolbar;
    private ActionBar mactionbar;

    //存储 ViewPager的Img地址
    private String[] imgList;

    //下载管理
    private Boolean isDown = false;
    private HttpHandler<File> hand;

    //装载 Img到Adapter
    private ImageView[] imageViews;

    //记录总大小、下载地址
    private String downloadurl;
    private int totlesize;

    //下载、分享、收藏
    private Button share, collection;
    private Button Down;

    //获取包名、地址。
    private String packgeName;
    private String Ip;
    private BitmapUtils bit;
    private SharedPreferences sharedPreferences;


    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.arg1)
            {
                case 1:
                    pd.cancel();
                    AboutAppinfo app = (AboutAppinfo) msg.getData().getSerializable("info");
                    bit.display(icon, "Http://"
                            + Ip
                            + ":8080/GooglePlayServer/image?name="
                            + app.getIconUrl());

                    appname.setText(app.getAppname());
                    ratingBar.setRating(Float.parseFloat(app.getStars()));
                    downloadNum.setText("下载量：" + app.getDownloadNum());
                    size.setText("大小:" + new java.text.DecimalFormat("#.00").
                            format((Double.parseDouble(app.getSize()) / (1024 * 1024))) + "MB");
                    time.setText("时间:" + app.getDate());
                    version.setText("版本:" + app.getVersion());
                    aboutapp.setText(app.getDes());

                    downloadurl = app.getDownloadurl();

                    imageViews = new ImageView[imgList.length];
                    for (int i = 0; i < imageViews.length; i++)
                    {
                        ImageView imageView = new ImageView(AboutApp.this);
                        imageViews[i] = imageView;

                        bit.display(imageView, "Http://"
                                + Ip
                                + ":8080/GooglePlayServer/image?name="
                                + imgList[i]);
                    }


                    myviewpager.setAdapter(new PagerAdapter()
                    {
                        @Override
                        public int getCount()
                        {
                            return imgList.length;
                        }

                        @Override
                        public boolean isViewFromObject(View view, Object object)
                        {
                            return view == object;
                        }

                        @Override
                        public void destroyItem(View container, int position, Object object)
                        {
                        }

                        @Override
                        public Object instantiateItem(ViewGroup container, int position)
                        {
                            try
                            {
                                ImageView views = imageViews[position % imageViews.length];
                                ((ViewPager) container).addView(views, 0);
                            } catch (Exception e)
                            {
                            }
                            return imageViews[position % imageViews.length];
                        }
                    });

                    myviewpager.setCurrentItem((imageViews.length) * 100);
                    myviewpager.setVisibility(View.VISIBLE);


                    break;
                default:
                    break;
            }
        }
    };


    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about_app);

        BindsView();


        BindsDates();


    }

    private void BindsDates()
    {


        String url = "http://" + Ip + ":8080/GooglePlayServer/detail";


        final RequestParams re = new RequestParams();
        re.addBodyParameter("packageName", packgeName);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, re, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String result = responseInfo.result;
                Log.i("haha", "13");
                try
                {
                    JSONObject root = new JSONObject(result);
                    AboutAppinfo app = new AboutAppinfo();
                    app.setAppname(root.getString("name"));
                    app.setIconUrl(root.getString("iconUrl"));
                    app.setStars(root.getString("stars"));
                    app.setDownloadNum(root.getString("downloadNum"));
                    app.setVersion(root.getString("version"));
                    app.setDate(root.getString("date"));
                    app.setSize(root.getString("size"));
                    totlesize = Integer.parseInt(root.getString("size"));
                    app.setDownloadurl(root.getString("downloadUrl"));
                    app.setDes(root.getString("des"));
                    app.setAuthor(root.getString("author"));

                    JSONArray screen = root.optJSONArray("screen");

                    imgList = new String[screen.length()];

                    for (int i = 0; i < screen.length(); i++)
                    {
                        imgList[i] = screen.optString(i);
                    }

                    Log.i("haha", "解析完成！！！");

                    Message msg = new Message();
                    Bundle bd = new Bundle();
                    bd.putSerializable("info", app);
                    msg.setData(bd);
                    msg.arg1 = 1;
                    handler.sendMessage(msg);


                } catch (JSONException e)
                {
                    e.printStackTrace();
                    Log.i("haha", "解析失败！！！");
                }


            }

            @Override
            public void onFailure(HttpException e, String s)
            {
                Toast.makeText(AboutApp.this, "网络错误", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void BindsView()
    {
        icon = (ImageView) findViewById(R.id.appinfo_icon);
        appname = (TextView) findViewById(R.id.appname);
        ratingBar = (RatingBar) findViewById(R.id.appinfo_rank);
        downloadNum = (TextView) findViewById(R.id.appinfo_downloadNum);
        time = (TextView) findViewById(R.id.appinfo_date);
        version = (TextView) findViewById(R.id.appinfo_version);
        size = (TextView) findViewById(R.id.appinfo_size);
        hide1 = (ImageView) findViewById(R.id.appinfo_hind1);
        hide2 = (ImageView) findViewById(R.id.appinfo_hind2);
        aboutapp = (TextView) findViewById(R.id.appinfo_about);
        myviewpager = (ViewPager) findViewById(R.id.appinfo_viewpager);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        mactionbar = getSupportActionBar();
        mactionbar.setTitle("");
        mactionbar.setDisplayHomeAsUpEnabled(true);

        ly = (LinearLayout) findViewById(R.id.ishid);


        share = (Button) findViewById(R.id.appinfo_share);
        collection = (Button) findViewById(R.id.appinfo_collection);

        Down = (Button) findViewById(R.id.appinfo_down);


        share.setOnClickListener(this);
        collection.setOnClickListener(this);
        Down.setOnClickListener(this);


        bit = new BitmapUtils(AboutApp.this);


        pd = new ProgressDialog(AboutApp.this);
        pd.setMessage("加载中...");
        pd.show();
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);

        Intent intent = getIntent();
        Bundle bd = intent.getBundleExtra("resouce");
        packgeName = bd.getString("packgename");

        sharedPreferences = getSharedPreferences("dle", MODE_PRIVATE);
        Ip = sharedPreferences.getString("Ip", "");


    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.appinfo_hind1:

                Log.i("haha", "hind.....asd");
                if (ly.getVisibility() == View.GONE)
                {
                    hide1.setImageResource(R.drawable.arrow_up);
                    ly.setVisibility(View.VISIBLE);
                } else
                {
                    hide1.setImageResource(R.drawable.arrow_down);
                    ly.setVisibility(View.GONE);
                }
                break;
            case R.id.appinfo_collection:
                Toast.makeText(AboutApp.this, "收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.appinfo_share:
                Toast.makeText(AboutApp.this, "分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.appinfo_down:
                String ip =
                        "http://"
                                + Ip
                                + ":8080/GooglePlayServer/download?name="
                                + downloadurl;
                File file = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/APPDownLoad/app");
                if (!file.exists())
                    file.mkdirs();
                File EndFile = new File(file, appname.getText().toString().trim() + ".apk");

                DownLoadApp(ip, EndFile + "");

                break;
            default:
                break;

        }

    }

    private void DownLoadApp(String ip, String path)
    {
        HttpUtils http = new HttpUtils();

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
                    Down.setText("下载完成");
                }

                @Override
                public void onFailure(HttpException e, String s)
                {
                    Down.setText("下载失败");
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading)
                {
                    super.onLoading(total, current, isUploading);
                    String flow = new java.text.DecimalFormat("#.00").
                            format(((float) current / (float) total) * 100) + "%";
                    Down.setText(flow);
                }
            });
        } else
        {
            hand.cancel();
            isDown = false;
        }


    }
}
