package dle.appmarket.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dle.appmarket.Fragment.AppFragment;
import dle.appmarket.Fragment.GameFragment;
import dle.appmarket.Fragment.GroomFragment;
import dle.appmarket.Fragment.HomeFragment;
import dle.appmarket.Fragment.MyHomeFragment;
import dle.appmarket.Fragment.PaihangFragment;
import dle.appmarket.Fragment.ThemFragment;
import dle.appmarket.Fragment.TypeFragment;
import dle.appmarket.R;
import dle.appmarket.View.ObServerScrollView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View
        .OnClickListener
{

    // DrawerLayout
    private NavigationView myMenu;
    private DrawerLayout myDrawerlayout;
    private ActionBarDrawerToggle mDrawertoggle;
    private ActionBar mActionbar;

    //ViewPager
    private ViewPager myViewpager;
    private FragmentPagerAdapter mFpagementPagerAdapter;
    private List<Fragment> mDates;

    private int mScreenImgLength;
    private int mCurrentPageIndex;
    private ImageView mTabLine;

    private LinearLayout
            llt_home,
            llt_app,
            llt_game,
            llt_them,
            llt_groom,
            llt_type,
            llt_paihang;

    private TextView
            text_home,
            text_app,
            text_game,
            text_them,
            text_groom,
            text_type,
            text_paihang;

    //ScrollView
    private ObServerScrollView myView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BindsView();


        InitViewpager();

        initTabLine();


    }

    private void InitViewpager()
    {
        myViewpager.setOffscreenPageLimit(7);
        mDates = new ArrayList<>();

        MyHomeFragment myHome = new MyHomeFragment();
        HomeFragment home = new HomeFragment();
        AppFragment  app  = new AppFragment();
        GameFragment game = new GameFragment();
        ThemFragment them = new ThemFragment();
        GroomFragment groom = new GroomFragment();
        TypeFragment type = new TypeFragment();
        PaihangFragment paihang = new PaihangFragment();
        mDates.add(home);
        mDates.add(app);
        mDates.add(game);
        mDates.add(them);
        mDates.add(groom);
        mDates.add(type);
        mDates.add(paihang);

        mFpagementPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public Fragment getItem(int position)
            {
                return mDates.get(position);
            }

            @Override
            public int getCount()
            {
                return mDates.size();
            }
        };

        myViewpager.setAdapter(mFpagementPagerAdapter);

        myViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLine.getLayoutParams();

                lp.leftMargin= (int) (mCurrentPageIndex*mScreenImgLength+
                        (positionOffset+position-mCurrentPageIndex)*mScreenImgLength);
                mTabLine.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position)
            {
                restTextview();

                switch (position)
                {
                    case 0:
                        text_home.setTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case 1:
                        text_app.setTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case 2:
                        text_game.setTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case 3:
                        text_them.setTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case 4:
                        text_groom.setTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case 5:
                        text_type.setTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case 6:
                        text_paihang.setTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                    default:
                        break;
                }
                mCurrentPageIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });


    }

    private void restTextview()
    {
        text_home.setTextColor(Color.BLACK);
        text_app.setTextColor(Color.BLACK);
        text_game.setTextColor(Color.BLACK);
        text_them.setTextColor(Color.BLACK);
        text_groom.setTextColor(Color.BLACK);
        text_type.setTextColor(Color.BLACK);
        text_paihang.setTextColor(Color.BLACK);
    }

    /**
     * 初始化tabline的长度
     */
    private void initTabLine()
    {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics dismet = new DisplayMetrics();
        display.getMetrics(dismet);
        mScreenImgLength = dismet.widthPixels/7;
        ViewGroup.LayoutParams lp = mTabLine.getLayoutParams();
        lp.width = mScreenImgLength;
        mTabLine.setLayoutParams(lp);

    }




    private void BindsView()
    {

        //绑定 下滑框、点击事件。字体设置，滑动处理
        mTabLine = (ImageView) findViewById(R.id.img_tab_line);

        myView = (ObServerScrollView) findViewById(R.id.tab_scrollview);



        myViewpager = (ViewPager) findViewById(R.id.main_viewpager);

        llt_home = (LinearLayout) findViewById(R.id.layout_home);
        llt_app = (LinearLayout) findViewById(R.id.layout_app);
        llt_game = (LinearLayout) findViewById(R.id.layout_game);
        llt_them = (LinearLayout) findViewById(R.id.layout_them);
        llt_groom = (LinearLayout) findViewById(R.id.layout_groom);
        llt_type = (LinearLayout) findViewById(R.id.layout_type);
        llt_paihang = (LinearLayout) findViewById(R.id.layout_paihang);


        llt_home.setOnClickListener(this);
        llt_app.setOnClickListener(this);
        llt_game.setOnClickListener(this);
        llt_them.setOnClickListener(this);
        llt_groom.setOnClickListener(this);
        llt_type.setOnClickListener(this);
        llt_paihang.setOnClickListener(this);

        text_home = (TextView) findViewById(R.id.text_home);
        text_app = (TextView) findViewById(R.id.text_app);
        text_game = (TextView) findViewById(R.id.text_game);
        text_them = (TextView) findViewById(R.id.text_them);
        text_groom = (TextView) findViewById(R.id.text_groom);
        text_type = (TextView) findViewById(R.id.text_type);
        text_paihang = (TextView) findViewById(R.id.text_paihang);



        myMenu = (NavigationView) findViewById(R.id.my_menu);
        myMenu.setNavigationItemSelectedListener(this);
        myDrawerlayout = (DrawerLayout) findViewById(R.id.my_drawerlayout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawertoggle = new ActionBarDrawerToggle(this,
                myDrawerlayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        myDrawerlayout.setDrawerListener(mDrawertoggle);
        mActionbar = getSupportActionBar();
        mActionbar.setDisplayUseLogoEnabled(true);
        mActionbar.setDisplayShowHomeEnabled(true);
        mActionbar.setLogo(R.mipmap.lanch);

        mActionbar.setTitle("应用市场");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        mDrawertoggle.syncState();// 三横杠
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawertoggle.onOptionsItemSelected(item)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawertoggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.menu_main:
                Toast.makeText(MainActivity.this,"首页",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_setting:
                Toast.makeText(MainActivity.this,"设置",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_theme:
                Toast.makeText(MainActivity.this,"主题",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_appmanager:
                Toast.makeText(MainActivity.this,"安装包管理",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_feedback:
                Toast.makeText(MainActivity.this,"反馈",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_checkupdate:
                Toast.makeText(MainActivity.this,"检查更新",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_about:
                Toast.makeText(MainActivity.this,"关于",Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_exit:
                Toast.makeText(MainActivity.this,"退出",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        myDrawerlayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v)
    {
       switch (v.getId())
       {
           case R.id.layout_home:
               myViewpager.setCurrentItem(0);
               break;
           case R.id.layout_app:
               myViewpager.setCurrentItem(1);
               break;
           case R.id.layout_game:
               myViewpager.setCurrentItem(2);
               break;
           case R.id.layout_them:
               myViewpager.setCurrentItem(3);
               break;
           case R.id.layout_groom:
               myViewpager.setCurrentItem(4);
               break;
           case R.id.layout_type:
               myViewpager.setCurrentItem(5);
               break;
           case R.id.layout_paihang:
               myViewpager.setCurrentItem(6);
               break;
           default:
               break;
       }
    }
}
