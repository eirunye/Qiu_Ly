package com.hxnidc.qiu_ly.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.orhanobut.logger.Logger;

import static com.hxnidc.qiu_ly.utils.Utils.context;

/**
 * Created by on 2017/9/3 11:28
 * Author：yrg
 * Describe:
 */


public class CommentDialog extends Dialog implements
        android.view.View.OnClickListener {

    public interface OnSendListener {
        void sendComment(String content);
    }

    private Context mContext;
    private EditText mEdittext;
    private TextView mTvCancel;
    private TextView mTvSend;
    private OnSendListener onSendListener;

    public void setOnSendListener(OnSendListener onSendListener) {
        this.onSendListener = onSendListener;
    }

    public CommentDialog(Context context) {
        super(context, R.style.inputDialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = (ViewGroup) View.inflate(mContext,
                R.layout.commend_bottom_layout, null);

        initView(v);
        setContentView(v);
        setLayout();
        setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                mEdittext.setFocusableInTouchMode(true);
                mEdittext.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) mEdittext
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mEdittext,
                        InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    private void initView(View v) {
        mEdittext = (EditText) v.findViewById(R.id.et_comment);
        mTvCancel = (TextView) v.findViewById(R.id.tv_cancel);
        mTvSend = (TextView) v.findViewById(R.id.tv_send);
        mTvCancel.setOnClickListener(this);
        mTvSend.setOnClickListener(this);

        mEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() >= 0)
                    mTvSend.setTextColor(context.getColor(R.color.colorAccent));
                Logger.e(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count,
                                      int after) {
                if (s.toString().trim().length() == 0)
                    mTvSend.setTextColor(context.getColor(R.color.colorGrayF5));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setLayout() {
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(p);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_send:
                String content = mEdittext.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showToast(UIUtils.getContext(), "请输入评论");
                    return;
                }

                if (onSendListener != null) {
                    onSendListener.sendComment(content);
                }
                break;
            default:
                break;
        }
    }
}

