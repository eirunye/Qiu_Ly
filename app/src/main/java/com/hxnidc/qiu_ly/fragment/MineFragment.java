package com.hxnidc.qiu_ly.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.activity.AboutActivity;
import com.hxnidc.qiu_ly.activity.CommentActivity;
import com.hxnidc.qiu_ly.activity.DecalsActivity;
import com.hxnidc.qiu_ly.activity.FavoritesActivity;
import com.hxnidc.qiu_ly.activity.LabelActivity;
import com.hxnidc.qiu_ly.activity.LoginActivity;
import com.hxnidc.qiu_ly.activity.SettingActivity;
import com.hxnidc.qiu_ly.activity.UserInfoActivity;
import com.hxnidc.qiu_ly.bean.bmob.UserInfo;
import com.hxnidc.qiu_ly.config.Configurations;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.SecureRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
//import cn.waps.AppConnect;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

import static cn.bmob.v3.BmobUser.getObjectByKey;

/**
 * Created by on 2017/7/3 11:23
 * Author：yrg
 * Describe:
 */


public class MineFragment extends Fragment {

    @BindView(R.id.img_mine_Blur)
    ImageView imgMineBlur;
    @BindView(R.id.img_mine_photo)
    CircleImageView imgMinePhoto;
    @BindView(R.id.tv_mine_login_tag)
    TextView tvMineLoginTag;
    @BindView(R.id.tv_mine_username)
    TextView tvMineUsername;
    @BindView(R.id.tv_mine_location)
    TextView tvMineLocation;
    @BindView(R.id.ll_mine_nameAndLocation)
    LinearLayout llMineNameAndLocation;
    @BindView(R.id.ll_mine_setting)
    LinearLayout llMineSetting;
    @BindView(R.id.ll_mine_about)
    LinearLayout llMineAbout;
    @BindView(R.id.po_image0)
    ShineButton poImage0;
    @BindView(R.id.img_mine_backdrop)
    ImageView imgMineBackdrop;
    @BindView(R.id.img_mine_concern)
    ImageView imgMineConcern;
    @BindView(R.id.textView2)
    TextView textView2;
    Unbinder unbinder;
    @BindView(R.id.ll_mine_decals)
    LinearLayout llMineDecals;
    @BindView(R.id.ll_mine_comment)
    LinearLayout llMineComment;
    @BindView(R.id.ll_mine_label)
    LinearLayout llMineLabel;
    @BindView(R.id.ll_mine_adGame)
    LinearLayout llMineAdGame;
    @BindView(R.id.ll_mine_favorite)
    LinearLayout llMineFavorite;
    @BindView(R.id.ll_toolbar_color)
    LinearLayout llToolbarColor;

    private static final int MINE_INFO_FINISH_FIND = 0x43;
    private static final int MINE_USER_INFO_WHAT = 0x9090;

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView(view);
        initGetData();
        return view;
    }

    private void initGetData() {

//        if (Configurations.Instance().isLogin()) {
//            SPUtils spUtils = new SPUtils("userinfo");
//            String imgFile = spUtils.getString("imgFile");
//            File file = new File(imgFile);
//            imgMinePhoto.setImageBitmap(PhotoUtils.getBitmapFromUri(Uri.fromFile(file), getActivity()));
//            Blurry.with(getActivity()).from(PhotoUtils.getBitmapFromUri(Uri.fromFile(file), getActivity())).into(imgMineBlur);
//        }

    }

    private void initView(View view) {

        // TODO: 2017/8/1 判断是否有头像如果有显示模糊效果，没有就显示背景


        if (Configurations.Instance().isLogin() && BmobUser.getCurrentUser(UserInfo.class) != null) {
            Message msg = new Message();
            msg.what = MINE_USER_INFO_WHAT;
            handler.sendMessage(msg);
        } else {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.uoko_guide_background_3);
            Blurry.with(getActivity()).from(bmp).into(imgMineBlur);
        }
//        Blurry.with(getActivity())
//                .radius(25)
//                .sampling(8)
//                .color(Color.argb(66, 255, 255, 0))
//                .async()//
//                .capture(view.findViewById(R.id.img_mine_Blur))
//                .into(imgMineBlur);
//
//        Blurry.with(getActivity())
//                .radius(10)
//                .sampling(8)
//                .async()
//                .capture(view.findViewById(R.id.img_mine_Blur))
//                .into((ImageView) view.findViewById(R.id.img_mine_Blur));
//        Blurry.with(getActivity())
//                .radius(10)
//                .sampling(8)
//                .color(Color.argb(66, 255, 255, 0))
//                .async()
//                .animate(500)
//                .onto();

    }


    @OnClick({R.id.ll_toolbar_color, R.id.ll_mine_about, R.id.img_mine_photo, R.id.ll_mine_decals, R.id.ll_mine_comment, R.id.ll_mine_label, R.id.ll_mine_adGame,
            R.id.ll_mine_nameAndLocation, R.id.po_image0, R.id.tv_mine_login_tag, R.id.ll_mine_setting, R.id.ll_mine_favorite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_mine_photo:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.ll_mine_nameAndLocation:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.po_image0:
                break;
            case R.id.ll_mine_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.tv_mine_login_tag:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.ll_mine_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.ll_mine_decals:
                if (!Configurations.Instance().isLogin()) {
                    ToastUtils.showToast(getActivity(), "请登录，才能查看贴纸");
                    return;
                }
                startActivity(new Intent(getActivity(), DecalsActivity.class));
                break;
            case R.id.ll_mine_comment:
                if (!Configurations.Instance().isLogin()) {
                    ToastUtils.showToast(getActivity(), "请登录，才能查看评论");
                    return;
                }
                startActivity(new Intent(getActivity(), CommentActivity.class));
                break;
            case R.id.ll_mine_label:
                if (!Configurations.Instance().isLogin()) {
                    ToastUtils.showToast(getActivity(), "请登录，才能查看标签");
                    return;
                }
                startActivity(new Intent(getActivity(), LabelActivity.class));
                break;
            case R.id.ll_mine_adGame:
                // TODO: 2017/8/1 广告游戏
//                AppConnect.getInstance(getActivity()).showGameOffers(getActivity());
                break;
            case R.id.ll_mine_favorite:
                if (!Configurations.Instance().isLogin()) {
                    ToastUtils.showToast(getActivity(), "请登录，才能查看收藏");
                    return;
                }
                startActivity(new Intent(getActivity(), FavoritesActivity.class));
                break;
            case R.id.ll_toolbar_color:
                setToolbarColor();
                break;
        }
    }

    private void setToolbarColor() {


//        final ColorPickerDialog dialog = new ColorPickerDialog(getActivity());
//        dialog.setOnColorSelectedListener(new ColorPickerDialog.OnColorSelectedListener() {
//            @Override
//            public void onColorSelected(Colorful.ThemeColor color) {
//                Colorful.config(getActivity())
//                        .primaryColor(pickRandomThemeColor())
//                        .apply();
//                //recreateActivity(MainActivity.this, MainActivity.class);
//                getActivity().recreate();
//            }
//        });
//        dialog.show();
    }

    private SecureRandom mRandom = new SecureRandom();


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUserInfo(UserInfo userinfo) {
        try {
            if (userinfo == null) return;
            if (!Configurations.Instance().isLogin() || BmobUser.getCurrentUser(UserInfo.class) == null) {
                tvMineUsername.setText("昵称");
                tvMineLocation.setText("个性签名>");
                imgMinePhoto.setImageResource(R.mipmap.avatar_icon);
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.uoko_guide_background_2);
                Blurry.with(getActivity()).from(bmp).into(imgMineBlur);
            } else {
                String nickename = (String) BmobUser.getObjectByKey("nickname");
                String signature = (String) BmobUser.getObjectByKey("signature");
                if (!TextUtils.isEmpty(nickename))
                    tvMineUsername.setText(nickename);
                if (!TextUtils.isEmpty(signature))
                    tvMineLocation.setText(signature);
                if (Configurations.Instance().getBitmap() != null) {
                    imgMinePhoto.setImageBitmap(Configurations.Instance().getBitmap());
                    Blurry.with(getActivity()).from(Configurations.Instance().getBitmap()).into(imgMineBlur);
                }
            }
        } catch (Exception e) {
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MINE_INFO_FINISH_FIND:
                    tvMineLoginTag.setVisibility(View.INVISIBLE);
                    llMineNameAndLocation.setVisibility(View.VISIBLE);
                    //tvMineUsername.setText(user.getUsername());
                    break;
                case MINE_USER_INFO_WHAT:
                    String nickname = (String) getObjectByKey("nickname");
                    //BmobFile imgFile = (BmobFile) BmobUser.getObjectByKey("imgFile");
                    //Logger.e("====>" + imgFile);
                    String signature = (String) BmobUser.getObjectByKey("signature");
                    if (!TextUtils.isEmpty(nickname))
                        tvMineUsername.setText(nickname);
                    if (!TextUtils.isEmpty(signature))
                        tvMineLocation.setText(signature);
//                    SPUtils sp = new SPUtils("userinfo");
//                    String imgFile = sp.getString("imgFile");
                    if (Configurations.Instance().getBitmap() != null) {
                        imgMinePhoto.setImageBitmap(Configurations.Instance().getBitmap());
                        Blurry.with(getActivity()).from(Configurations.Instance().getBitmap()).into(imgMineBlur);
                    }
                    break;

            }
        }
    };
}
