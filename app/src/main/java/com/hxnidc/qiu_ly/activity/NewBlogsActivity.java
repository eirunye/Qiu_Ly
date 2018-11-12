package com.hxnidc.qiu_ly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.utils.ImageUtils;
import com.hxnidc.qiu_ly.utils.JsoupUtil;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.hxnidc.qiu_ly.widget.CommentDialog;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;
import butterknife.OnClick;

public class NewBlogsActivity extends BaseActivity implements CommentDialog.OnSendListener, View.OnClickListener {

    @BindView(R.id.detail_img)
    ImageView detailImg;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.textview)
    TextView textview;
    @BindView(R.id.detail_content)
    CoordinatorLayout detailContent;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.tv_more_comment)
    TextView tvMoreComment;
    @BindView(R.id.img_share)
    ImageView imgShare;
    @BindView(R.id.fab_share)
    FloatingActionButton fabShare;
    @BindView(R.id.fab_setting)
    FloatingActionButton fabSetting;
    @BindView(R.id.fab_comment)
    FloatingActionButton fabComment;
    @BindView(R.id.menu_fab)
    FloatingActionMenu menuFab;
    private RichText richText;
    private CommentDialog commentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_new_blogs);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_blogs;
    }

    @Override
    protected void initView() {

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        getSupportActionBar().setTitle("");
        avi.show();
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_share:
                    ToastUtils.showToast(NewBlogsActivity.this, "newblogs 分享");
                    break;
            }
            return true;
        });
        fabShare.setOnClickListener(this);
        fabSetting.setOnClickListener(this);
        fabComment.setOnClickListener(this);
        menuFab.setClosedOnTouchOutside(true);
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {
    }


    @Override
    protected void initData() {

        getNewBlogData();
        commentDialog = new CommentDialog(this);
        commentDialog.setOnSendListener(this);

    }

    private void getNewBlogData() {

        try {
            Intent intent = getIntent();
            String blogsUrl = intent.getStringExtra("blogs");
            String imgUrl = intent.getStringExtra("imgUrl");
            if (intent == null || TextUtils.isEmpty(blogsUrl)) return;
            assert textview != null;
            new Thread(() -> {
                try {
                    String element = JsoupUtil.getNewBlogDetails(activity, blogsUrl);
                    if (TextUtils.isEmpty(element)) {
                        UIUtils.post(() -> avi.hide());
                        return;
                    }
                    UIUtils.post(() -> {
                        avi.hide();
                        ImageUtils.loadImgUrl(NewBlogsActivity.this, imgUrl, detailImg);
                        richText = RichText.from(element).into(textview);
                        //html_text.setHtml(element, new HtmlResImageGetter(html_text));
                    });

                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }

            }).start();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        richText.clear();
        richText = null;
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

    @OnClick({R.id.tv_more_comment, R.id.img_share, R.id.tv_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_more_comment:

                break;
            case R.id.img_share:
                break;
            case R.id.tv_comment:
                if (commentDialog != null)
                    commentDialog.show();
                break;
        }
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

    @Override
    public void sendComment(String content) {

    }
}
