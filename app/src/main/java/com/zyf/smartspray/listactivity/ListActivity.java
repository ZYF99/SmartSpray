package com.zyf.smartspray.listactivity;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zyf.common.app.Activity;
import com.zyf.common.app.Application;
import com.zyf.factory.model.SmartData;
import com.zyf.smartspray.R;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ListActivity extends Activity implements Ilist.View {
    private static Ilist.Presenter presenter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    RecAdapter adapter;

    List<SmartData> mainList = new ArrayList<>();


    @Override
    protected void initWidget() {
        super.initWidget();
        adapter = new RecAdapter(R.layout.cell_list,mainList);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this));
        adapter.bindToRecyclerView(recyclerView);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = Plist.getInstance(this);
        //将本活动与前一活动开启的socket绑定起来（设置监听接收消息）
        presenter.bindSocket();
    }

    public static void show(Context context){
        Intent intent = new Intent(context, ListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_list;
    }


    //列表得到刷新
    @Override
    public void showRecvMsg(List<SmartData> list) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                adapter.replaceData(list);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
