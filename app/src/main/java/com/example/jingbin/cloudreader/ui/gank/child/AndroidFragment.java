package com.example.jingbin.cloudreader.ui.gank.child;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.example.jingbin.cloudreader.MainActivity;
import com.example.jingbin.cloudreader.R;
import com.example.jingbin.cloudreader.adapter.AndroidAdapter;
import com.example.jingbin.cloudreader.base.BaseFragment;
import com.example.jingbin.cloudreader.bean.GankIoDataBean;
import com.example.jingbin.cloudreader.databinding.FragmentAndroidBinding;
import com.example.jingbin.cloudreader.viewmodel.gank.BigAndroidNavigator;
import com.example.jingbin.cloudreader.viewmodel.gank.BigAndroidViewModel;
import com.example.xrecyclerview.XRecyclerView;

/**
 * 大安卓 fragment
 */
public class AndroidFragment extends BaseFragment<FragmentAndroidBinding> implements BigAndroidNavigator {

    private static final String TAG = "AndroidFragment";
    private static final String TYPE = "mType";
    private String mType = "Android";
    //    private int mPage = 1;
    private boolean mIsPrepared;
    private boolean mIsFirst = true;
    private AndroidAdapter mAndroidAdapter;
    private MainActivity activity;
    private BigAndroidViewModel everydayViewModel;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static AndroidFragment newInstance(String type) {
        AndroidFragment fragment = new AndroidFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
    }

    @Override
    public int setContent() {
        return R.layout.fragment_android;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        everydayViewModel = new BigAndroidViewModel(this, mType);
        everydayViewModel.setBigAndroidNavigator(this);
        initRecyclerView();
        // 准备就绪
        mIsPrepared = true;
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }
        everydayViewModel.loadAndroidData();
    }

    /**
     * 设置adapter
     */
    private void setAdapter(GankIoDataBean mAndroidBean) {
        mAndroidAdapter.clear();
        mAndroidAdapter.addAll(mAndroidBean.getResults());
        bindingView.xrvAndroid.setAdapter(mAndroidAdapter);
        mAndroidAdapter.notifyDataSetChanged();
        bindingView.xrvAndroid.refreshComplete();

        mIsFirst = false;
    }

    private void initRecyclerView() {
        mAndroidAdapter = new AndroidAdapter(activity);
        bindingView.xrvAndroid.setLayoutManager(new LinearLayoutManager(getActivity()));
        bindingView.xrvAndroid.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
//                mPage = 1;
                everydayViewModel.setPage(1);
                everydayViewModel.loadAndroidData();
//                loadAndroidData();
            }

            @Override
            public void onLoadMore() {
                int page = everydayViewModel.getPage();
                page++;
                everydayViewModel.setPage(page);
                everydayViewModel.loadAndroidData();
//                loadAndroidData();
            }
        });
    }

    /**
     * 加载失败后点击后的操作
     */
    @Override
    protected void onRefresh() {
        everydayViewModel.loadAndroidData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showContentView() {
        showContentView();
    }

    @Override
    public void showAdapterView(GankIoDataBean gankIoDataBean) {
        setAdapter(gankIoDataBean);
    }

    @Override
    public void refreshAdapter(GankIoDataBean gankIoDataBean) {
        bindingView.xrvAndroid.refreshComplete();
        mAndroidAdapter.addAll(gankIoDataBean.getResults());
        mAndroidAdapter.notifyDataSetChanged();
    }

    @Override
    public void showListNoMoreLoading() {
        bindingView.xrvAndroid.noMoreLoading();
    }

    @Override
    public void showLoadFailedView() {
        bindingView.xrvAndroid.refreshComplete();
        // 注意：这里不能写成 mPage == 1，否则会一直显示错误页面
        if (mAndroidAdapter.getItemCount() == 0) {
            showError();
        }
    }
}
