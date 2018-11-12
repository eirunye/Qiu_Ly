package com.hxnidc.qiu_ly.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.utils.ImageUtils;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.wang.avi.AVLoadingIndicatorView;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;

public class HomeDetailsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.detail_img)
    ImageView detailImg;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.detail_content)
    CoordinatorLayout detailContent;
    @BindView(R.id.webview)
    WebView mWebView;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.fab_share)
    FloatingActionButton fabShare;
    @BindView(R.id.fab_setting)
    FloatingActionButton fabSetting;
    @BindView(R.id.fab_comment)
    FloatingActionButton fabComment;
    @BindView(R.id.menu_fab)
    FloatingActionMenu menuFab;

    private RichText richText;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_details;
    }

    @Override
    protected void initView() {

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        getSupportActionBar().setTitle("");
        toolbar.setOnMenuItemClickListener((item) -> {
            switch (item.getItemId()) {
                case R.id.action_share:
                    ToastUtils.showToast(HomeDetailsActivity.this, "分享");
                    break;
            }
            return true;
        });
        fabShare.setOnClickListener(this);
        fabSetting.setOnClickListener(this);
        fabComment.setOnClickListener(this);
        menuFab.setClosedOnTouchOutside(true);
        //toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
    }

    @Override
    protected void initData() {
        avi.show();
        getDataDetail();

    }

    private void getDataDetail() {
        try {
            Intent intent = getIntent();
            String homeHtml = intent.getStringExtra("homeHtml");
            String imgUrl = intent.getStringExtra("homeImgUrl");
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(url);
//                    return true;
//                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    //super.onPageFinished(view, url);
                    avi.hide();
                }
            });

            mWebView.loadUrl(homeHtml);
            if (TextUtils.isEmpty(homeHtml) || TextUtils.isEmpty(imgUrl)) return;
            ImageUtils.loadImgAdUrl(this, imgUrl, detailImg);

//            assert textview != null;
//            if (TextUtils.isEmpty(homeHtml) || TextUtils.isEmpty(imgUrl)) return;
//            ImageUtils.loadImgAdUrl(this, imgUrl, detailImg);
//            //avi.hide();
//            new Thread(() -> {
//                String element = JsoupUtil.HomeNewDetails(activity, homeHtml);
//                if (!TextUtils.isEmpty(element))
//                    UIUtils.post(() -> {
//                        avi.hide();
//                        richText = RichText.from(element).into(textview);
//                        //html_text.setHtml(element, new HtmlResImageGetter(html_text));
//                    });
//            }).start();

        } catch (Exception e) {
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            //结束当前页
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blogs_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (richText != null) {
//            richText.clear();
//            richText = null;
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_comment:
                break;
            case R.id.fab_share:
                break;
            case R.id.fab_setting:
                break;
        }
        menuFab.close(true);
    }
//    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            String msg = "";
//            switch (menuItem.getItemId()) {
////                case R.id.action_edit:
////                    msg += "Click edit";
////                    break;
////                case R.id.action_share:
////                    msg += "Click share";
////                    break;
//                case R.id.action_settings:
//                    msg += "Click setting";
//                    break;
//            }
//
//            if (!msg.equals("")) {
//            }
//            return true;
//        }
//    };
}
