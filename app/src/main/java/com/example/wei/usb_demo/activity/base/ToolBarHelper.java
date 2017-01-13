package com.example.wei.usb_demo.activity.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.wei.pl2303_test.R;

/**
 * Created by zhenqiang on 2016/12/28.
 */

public class ToolBarHelper {
    /*上下文，创建view的时候需要用到*/
    private Context mContext; /*base view*/
    private FrameLayout mContentView; /*用户定义的view*/
    private View mUserView; /*toolbar*/
    private Toolbar mToolBar; /*视图构造器*/
    private LayoutInflater mInflater; /*
    * 两个属性
    * 1、toolbar是否悬浮在窗口之上
    * 2、toolbar的高度获取
    * */
    private static int[] ATTRS = {
            R.attr.windowActionBarOverlay,
            R.attr.actionBarSize
    };


    private TextView tvRight;

    public ToolBarHelper(Context context, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext); /*初始化整个内容*/
        initContentView(); /*初始化用户定义的布局*/
        initUserView(layoutId); /*初始化toolbar*/
        initToolBar();
    }

    private void initContentView() { /*直接创建一个帧布局，作为视图容器的父容器*/
        mContentView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);

    }

    private void initToolBar() { /*通过inflater获取toolbar的布局文件*/
        View toolbar = mInflater.inflate(R.layout.common_toolbar_layout, mContentView);
        mToolBar = (Toolbar) toolbar.findViewById(R.id.toolbar);
        mToolBar.setTitleTextColor(Color.WHITE);

        tvRight = (TextView) toolbar.findViewById(R.id.tv_right);
    }

    private void initUserView(int id) {
        mUserView = mInflater.inflate(id, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(ATTRS); /*获取主题中定义的悬浮标志*/
        boolean overly = typedArray.getBoolean(0, false); /*获取主题中定义的toolbar的高度*/
        int toolBarSize = (int) typedArray.getDimension(1, (int) mContext.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
        typedArray.recycle(); /*如果是悬浮状态，则不需要设置间距*/
        params.topMargin = overly ? 0 : toolBarSize;
        mContentView.addView(mUserView, params);

    }

    public FrameLayout getContentView() {
        return mContentView;
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    public void setTvRight(String text, final onTextViewClickListener listener) {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(text);
        if (listener != null) {
            tvRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick();
                }
            });
        }
    }

    public interface onTextViewClickListener {
        void onClick();
    }
}
