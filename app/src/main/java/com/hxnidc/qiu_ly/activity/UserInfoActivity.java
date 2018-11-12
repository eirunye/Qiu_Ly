package com.hxnidc.qiu_ly.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.CardBean;
import com.hxnidc.qiu_ly.bean.JsonBean;
import com.hxnidc.qiu_ly.bean.bmob.UserInfo;
import com.hxnidc.qiu_ly.config.Configurations;
import com.hxnidc.qiu_ly.utils.FileUtils;
import com.hxnidc.qiu_ly.utils.GetJsonDataUtil;
import com.hxnidc.qiu_ly.utils.PhotoUtils;
import com.hxnidc.qiu_ly.utils.TimeUtils;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.hxnidc.qiu_ly.view.FireworkView;
import com.hxnidc.qiu_ly.widget.PicturePopupwindow;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.bmob.v3.BmobUser.getCurrentUser;


public class UserInfoActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_info_save)
    TextView tvInfoSave;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_info_photo)
    CircleImageView imgInfoPhoto;
    @BindView(R.id.et_info_nickname)
    EditText etInfoNickname;
    @BindView(R.id.et_info_Signature)
    EditText etInfoSignature;
    @BindView(R.id.tv_info_sex)
    TextView tvInfoSex;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.tv_info_age)
    TextView tvInfoAge;
    @BindView(R.id.tv_info_birthday)
    TextView tvInfoBirthday;
    @BindView(R.id.et_info_mail)
    EditText etInfoMail;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.rl_info_phone)
    RelativeLayout rlInfoPhone;
    @BindView(R.id.tv_info_address)
    TextView tvInfoAddress;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.fire_info_sign)
    FireworkView fireInfoSign;
    @BindView(R.id.fire_info_ninkname)
    FireworkView fireInfoNinkname;
    @BindView(R.id.loadingIndicatorView)
    AVLoadingIndicatorView loadingIndicatorView;

    //private TimeSelector timeSelector;

    PicturePopupwindow bottomPopupOption;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/" + TimeUtils.date2String(new Date(), "yyyyMMdd") + ".jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/" + TimeUtils.date2String(new Date(), "yyyyMMdd") + ".jpg");//crop_photo
    private File filePicture = null;
    private Uri imageUri;
    private Uri cropImageUri;


    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private OptionsPickerView pvCustomOptions;

    private TimePickerView pvCustomTime;
    private ArrayList<CardBean> cardItem = new ArrayList<>();
    private boolean isLoaded = false;
    private int infoAge;
    private String path;
    //private UserInfo newuser;
    private String imgPath;
    private static final int MINE_INFO_FINISH_FIND_USER = 0x11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_user_info);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> finish());
        //mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        if (!Configurations.Instance().isLogin()) {
            fireInfoNinkname.bindEditText(etInfoNickname);
            fireInfoSign.bindEditText(etInfoSignature);
        }
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {


    }

    @Override
    protected void initData() {
        getCardData();
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        try {
            if (Configurations.Instance().isLogin() && getCurrentUser(UserInfo.class) != null) {
                Message msg = new Message();
                msg.what = MINE_INFO_FINISH_FIND_USER;
                mHandler.sendMessage(msg);
            }
        } catch (Exception e) {

        }

    }

    @OnClick({R.id.tv_info_sex, R.id.img_info_photo, R.id.tv_info_age, R.id.tv_info_birthday, R.id.rl_info_phone, R.id.tv_info_address, R.id.tv_info_save})
    public void onViewClicked(View view) {
        if (!Configurations.Instance().isLogin()) {
            ToastUtils.showToast(UserInfoActivity.this, "请登录");
            startOpenActivity(LoginActivity.class);
            return;
        }
        switch (view.getId()) {
            case R.id.img_info_photo:
                showPopupWindow();
                break;
            case R.id.tv_info_age:
                break;
            case R.id.tv_info_sex:
                showInfoSex();
                break;
            case R.id.tv_info_birthday:
                showDataBirthday();
                break;
            case R.id.rl_info_phone:
                break;
            case R.id.tv_info_save:
                passInfoSave();
                break;
            case R.id.tv_info_address:
                if (isLoaded) {
                    ShowPickerView();
                } else {
                    Toast.makeText(this, "数据暂未解析成功，请等待", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void getCardData() {

        cardItem.add(new CardBean(0, "男"));
        cardItem.add(new CardBean(1, "女"));
        for (int i = 0; i < cardItem.size(); i++) {
            if (cardItem.get(i).getCardNo().length() > 2) {
                String str_item = cardItem.get(i).getCardNo().substring(0, 2);
                cardItem.get(i).setCardNo(str_item);
            }
        }
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1990, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 0, 1);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tvInfoBirthday.setText(TimeUtils.getTime(date));
                ToastUtils.showToast(UserInfoActivity.this, TimeUtils.getTime(date));
                infoAge = Integer.parseInt(TimeUtils.getCurrentYear()) - Integer.parseInt(tvInfoBirthday.getText().toString().split("-")[0]);
                if (infoAge < 0) {
                    ToastUtils.showToast(UserInfoActivity.this, getString(R.string.check_string));
                    return;
                }
                tvInfoAge.setText(infoAge + "");
            }
        })
                /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
               /*.gravity(Gravity.RIGHT)// default is center*/
                .setTextColorCenter(getResources().getColor(R.color.colorAccent))
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                //.setDividerColor(Color.BLACK)
                .build();
    }

    private void showInfoSex() {
        try {
            Logger.e("进入----");
            pvCustomOptions = new OptionsPickerView.Builder(this, (options1, option2, options3, v) -> {
                //返回的分别是三个级别的选中位置
                String tx = cardItem.get(options1).getPickerViewText();
                tvInfoSex.setText(tx);
            })
                    .setTextColorCenter(getResources().getColor(R.color.colorAccent))
                    .setLayoutRes(R.layout.pickerview_custom_options, v -> {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(view -> {
                            pvCustomOptions.returnData();
                            pvCustomOptions.dismiss();
                        });
                        ivCancel.setOnClickListener(view ->
                                pvCustomOptions.dismiss()
                        );
                    }).setOutSideCancelable(false).build();
            pvCustomOptions.setPicker(cardItem);//添加数据
            pvCustomOptions.show();
        } catch (Exception e) {
        }
    }

    /**
     * 保存信息
     */
    private void passInfoSave() {
        try {
            // TODO: 2017/8/3 保存用户信息
            final UserInfo bmobUser = getCurrentUser(UserInfo.class);
            if (bmobUser == null) return;
            if (cropImageUri == null) {
                ToastUtils.showToast(UserInfoActivity.this, "设置头像");
                return;
            }
            final UserInfo newuser = new UserInfo();
            path = PhotoUtils.getPath(UserInfoActivity.this, cropImageUri);
            Logger.e("path:" + path);
            if (TextUtils.isEmpty(path)) return;
            File imgFile = new File(path);
            BmobFile file = new BmobFile(FileUtils.getFileName(imgPath), null, imgFile.toString());
            if (file != null)
                newuser.setImgFile(file);
            if (loadingIndicatorView != null) {
                loadingIndicatorView.setVisibility(View.VISIBLE);
                loadingIndicatorView.show();
            }
            //newuser.setPhone(tvUserPhone.getText().toString());
            newuser.setUsername(tvUserPhone.getText().toString());
            newuser.setAddress(tvInfoAddress.getText().toString());
            newuser.setAge(tvInfoAge.getText().toString());
            newuser.setEmail(etInfoMail.getText().toString());
            newuser.setSex(tvInfoSex.getText().toString());
            newuser.setSignature(etInfoSignature.getText().toString());
            newuser.setNickname(etInfoNickname.getText().toString());
            newuser.setBirthday(tvInfoBirthday.getText().toString());
            Logger.e("newuser：" + newuser.toString());
            addSubscription(newuser.update(bmobUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    UIUtils.post(() -> {
                        if (loadingIndicatorView != null) {
                            loadingIndicatorView.setVisibility(View.GONE);
                            loadingIndicatorView.hide();
                        }
                    });
                    if (e == null) {
                        ToastUtils.showToast(UserInfoActivity.this, "保存成功！");
                        EventBus.getDefault().post(newuser);
                        UserInfoActivity.this.finish();
                    } else {
                        Logger.e("失败：" + e.getMessage());
                        ToastUtils.showToast(UserInfoActivity.this, "保存失败！");
                    }
                }
            }));
        } catch (Exception e) {
        }
    }

    private void showDataBirthday() {
        try {

            if (pvCustomTime != null) {
                pvCustomTime.show();
            }
        } catch (Exception e) {
        }
    }

    private void showPopupWindow() {
        bottomPopupOption = new PicturePopupwindow(UserInfoActivity.this);
        bottomPopupOption.setItemText("拍照", "相册");
        bottomPopupOption.showPopupWindow();
        bottomPopupOption.setItemClickListener(position -> {
            bottomPopupOption.dismiss();
            switch (position) {
                case 0:
                    passPhotograph();
                    break;
                case 1:
                    passPhotoAlbum();
                    break;
            }
        });
    }

    /**
     * 相册
     */
    private void passPhotoAlbum() {
        if (bottomPopupOption != null) bottomPopupOption.dismiss();
        autoObtainStoragePermission();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        filePicture = fileUri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(UserInfoActivity.this, "com.hxnidc.qiu_ly.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtils.showToast(this, "设备没有SD卡！");
                    }
                } else {
                    ToastUtils.showToast(this, "请允许打开相机！！");
                }
                break;


            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    ToastUtils.showToast(this, "请允许打操作SDCard！！");
                }
                break;
        }
    }

    /**
     * 拍照
     */
    private void passPhotograph() {
        if (bottomPopupOption != null) bottomPopupOption.dismiss();
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                ToastUtils.showToast(this, "您已经拒绝过一次");
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(UserInfoActivity.this, "com.hxnidc.qiu_ly.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtils.showToast(this, "设备没有SD卡！");
            }
        }
    }

    private static final int output_X = 380;
    private static final int output_Y = 380;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.hxnidc.qiu_ly.fileProvider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.showToast(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    imgPath = FileUtils.getRealFilePath(this, cropImageUri);
                    Logger.e("imgPath:" + imgPath);
                    Configurations.Instance().setBitmap(bitmap);
                    if (bitmap != null) {
                        showImages(bitmap);
                    }
                    break;
            }
        }
    }

    private void showImages(Bitmap bitmap) {
        imgInfoPhoto.setImageBitmap(bitmap);
    }


    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                tvInfoAddress.setText(tx);
                Toast.makeText(UserInfoActivity.this, tx, Toast.LENGTH_SHORT).show();

            }
        })
                .setTitleText(getString(R.string.city_string))
                .setTitleColor(getColor(R.color.colorAccent))
                //.setDividerColor(Color.BLACK)
                .setSubmitColor(getColor(R.color.colorAccent))//确定按钮文字颜色
                .setCancelColor(getColor(R.color.colorAccent))//取消按钮文字颜色
                .setTextColorCenter(getColor(R.color.colorAccent)) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        //Toast.makeText(UserInfoActivity.this, "开始解析数据", Toast.LENGTH_SHORT).show();
                        thread = new Thread(() ->
                                // 写子线程中的操作,解析省市区数据
                                initJsonData());
                        thread.start();
                    }
                    break;
                case MSG_LOAD_SUCCESS:
                    //Toast.makeText(UserInfoActivity.this, "解析数据成功", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(UserInfoActivity.this, "解析数据失败", Toast.LENGTH_SHORT).show();
                    break;
                case MINE_INFO_FINISH_FIND_USER:
                    getCurrentUserInfo();
                    break;

            }
        }
    };

    private void getCurrentUserInfo() {
        String username = (String) BmobUser.getObjectByKey("username");
        String age = (String) BmobUser.getObjectByKey("age");
        String sex = (String) BmobUser.getObjectByKey("sex");
        String birthday = (String) BmobUser.getObjectByKey("birthday");
        String address = (String) BmobUser.getObjectByKey("address");
        String signature = (String) BmobUser.getObjectByKey("signature");
        String nickname = (String) BmobUser.getObjectByKey("nickname");
        String email = (String) BmobUser.getObjectByKey("email");
        //Logger.e("username:" + username + "\n" + "age:" + age + "\n" + "sex:" + sex + "\n" + "birthday:" + birthday + "\n" + "address:" + address + "\n" + signature + "\n" + nickname + "\n" + "imgFile:");
        if (!TextUtils.isEmpty(username))
            tvUserPhone.setText(username);
        if (!TextUtils.isEmpty(nickname))
            etInfoNickname.setText(nickname);
        if (!TextUtils.isEmpty(signature))
            etInfoSignature.setText(signature);
        if (!TextUtils.isEmpty(sex))
            tvInfoSex.setText(sex);
        if (!TextUtils.isEmpty(age))
            tvInfoAge.setText(age);
        if (!TextUtils.isEmpty(birthday))
            tvInfoBirthday.setText(birthday);
        if (!TextUtils.isEmpty(address))
            tvInfoAddress.setText(address);
        if (!TextUtils.isEmpty(email))
            etInfoMail.setText(email);
        if (Configurations.Instance().getBitmap() != null)
            imgInfoPhoto.setImageBitmap(Configurations.Instance().getBitmap());
    }

}
