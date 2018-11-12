package com.hxnidc.qiu_ly.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.activity.video.HotListVideoActivity;
import com.hxnidc.qiu_ly.bean.bmob.UserInfo;
import com.hxnidc.qiu_ly.config.Configurations;
import com.hxnidc.qiu_ly.constant.Constants;
import com.hxnidc.qiu_ly.fragment.HomeFragment;
import com.hxnidc.qiu_ly.fragment.HotFragment;
import com.hxnidc.qiu_ly.fragment.MineFragment;
import com.hxnidc.qiu_ly.fragment.VideoFragment;
import com.hxnidc.qiu_ly.listener.OnStateNavigationListener;
import com.hxnidc.qiu_ly.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnStateNavigationListener, View.OnClickListener {

    Button button;
    TextView mTextMessage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private TextView tvMainSignature;
    private TextView tvNickname;
    private ImageView avatarView;
    private BottomNavigationView navigation;
    private HomeFragment mHomeFragment;
    private VideoFragment mVideoFragment;
    private HotFragment mHotFragment;
    private MineFragment mMineFragment;
    private FragmentManager manager;
    FragmentTransaction transaction;
    private int itemId;
    Bundle savedInstanceState;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = manager.beginTransaction();
            itemId = item.getItemId();
            //clearChioceTab();
            hideFragment();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (mHomeFragment == null) {
                        mHomeFragment = HomeFragment.newInstance();
                        transaction.add(R.id.content, mHomeFragment);
                    } else {
                        transaction.show(mHomeFragment);
                    }
                    transaction.commit();
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_video:
                    if (mVideoFragment == null) {
                        mVideoFragment = VideoFragment.newInstance();
                        transaction.add(R.id.content, mVideoFragment);
                    } else {
                        transaction.show(mVideoFragment);
                    }
                    transaction.commit();
                    //mTextMessage.setText(R.string.title_video);
                    return true;
                case R.id.navigation_dashboard:
                    if (mHotFragment == null) {
                        mHotFragment = HotFragment.newInstance();
                        transaction.add(R.id.content, mHotFragment);
                    } else {
                        transaction.show(mHotFragment);
                    }
                    transaction.commit();
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    if (mMineFragment == null) {
                        mMineFragment = MineFragment.newInstance();
                        transaction.add(R.id.content, mMineFragment);
                    } else {
                        transaction.show(mMineFragment);
                    }
                    transaction.commit();
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    private void hideFragment() {
        if (transaction == null) return;
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mVideoFragment != null) {
            transaction.hide(mVideoFragment);
        }
        if (mHotFragment != null) {
            transaction.hide(mHotFragment);
        }
        if (mMineFragment != null) {
            transaction.hide(mMineFragment);
        }
    }

    private void clearChioceTab() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
        this.savedInstanceState = savedInstanceState;
        //setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        transaction = manager.beginTransaction();
        if (mHomeFragment == null) {
            //Logger.e("mHomeFragment");
            mHomeFragment = HomeFragment.newInstance();
            transaction.replace(R.id.content, mHomeFragment);
            transaction.commit();
        }
        //mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headview = navView.getHeaderView(0);
        avatarView = (ImageView) headview.findViewById(R.id.imageView);
        avatarView.setOnClickListener(this);
        tvNickname = (TextView) headview.findViewById(R.id.tv_main_username);
        tvNickname.setOnClickListener(this);
        tvMainSignature = (TextView) headview.findViewById(R.id.tv_main_signature);
        tvMainSignature.setOnClickListener(this);
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        setUserInfoData();
    }

    private void setUserInfoData() {
        if (Configurations.Instance().isLogin() && BmobUser.getCurrentUser(UserInfo.class) != null) {
            if (Configurations.Instance().getBitmap() != null) {
                avatarView.setImageBitmap(Configurations.Instance().getBitmap());
            }
            UserInfo userInfo = BmobUser.getCurrentUser(UserInfo.class);
            String nickname = userInfo.getNickname();
            String signature = userInfo.getSignature();

            if (!TextUtils.isEmpty(nickname)) {
                tvNickname.setText(nickname);
            }
            if (!TextUtils.isEmpty(signature)) {
                tvMainSignature.setText(signature);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            exitApp();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            if (Configurations.Instance().isLogin()) {
                startOpenActivity(DecalsActivity.class);
            } else {
                ToastUtils.showToast(MainActivity.this, "请登录,才能查看贴纸");
            }
        } else if (id == R.id.nav_gallery) {
            if (Configurations.Instance().isLogin()) {
                startOpenActivity(FavoritesActivity.class);
            } else {
                ToastUtils.showToast(MainActivity.this, "请登录,才能查看关注");
            }
        } else if (id == R.id.nav_slideshow) {
            //startOpenActivity(HotListVideoActivity.class);
            Intent intent = new Intent(MainActivity.this, HotListVideoActivity.class);
            intent.putExtra("page", "2");
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            startOpenActivity(SettingActivity.class);
        } else if (id == R.id.nav_share) {
            // TODO: 2017/8/1 分享APP
        }
//        else if (id == R.id.nav_day) {
//            App.me().setTheme(this, false);
//        } else if (id == R.id.nav_night) {
//            App.me().setTheme(this, true);
//        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Logger.d("onSaveInstanceState=" + currentIndex);
        outState.putInt(Constants.CURRENT_INDEX, itemId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //savedInstanceState.putInt(Constants.CURRENT_INDEX, itemId);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void stateNavigation(int state) {
        if (state != 0) return;
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUserInfo(UserInfo userinfo) {
        if (userinfo == null) return;
        if (Configurations.Instance().getBitmap() != null) {
            avatarView.setImageBitmap(Configurations.Instance().getBitmap());
        }
        String nickname = (String) BmobUser.getObjectByKey("nickname");
        String signature = (String) BmobUser.getObjectByKey("signature");

        if (!TextUtils.isEmpty(nickname)) {
            tvNickname.setText(nickname);
        }
        if (!TextUtils.isEmpty(signature)) {
            tvMainSignature.setText(signature);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                startOpenPage();
                break;
            case R.id.tv_main_username:
                startOpenPage();
                break;
            case R.id.tv_main_signature:
                startOpenPage();
                break;

        }
    }

//    @Override
//    protected void onResume() {
//
//        super.onResume();
//    }

    private void startOpenPage() {
        if (Configurations.Instance().isLogin() && BmobUser.getCurrentUser(UserInfo.class) != null) {
            startOpenActivity(UserInfoActivity.class);
        } else {
            startOpenActivity(LoginActivity.class);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mOnNavigationItemSelectedListener = null;
        if (mHomeFragment != null) {
            mHomeFragment = null;
        }
        if (mVideoFragment != null) {
            mVideoFragment = null;
        }
        if (mHotFragment != null) {
            mHotFragment = null;
        }
        if (mMineFragment != null) {
            mMineFragment = null;
        }
    }
}
