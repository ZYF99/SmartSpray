package com.zyf.smartspray.listactivity;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zyf.factory.model.SmartData;
import com.zyf.smartspray.R;

import java.util.List;

public class RecAdapter extends BaseQuickAdapter<SmartData, BaseViewHolder> {
    public RecAdapter(int layoutResId, @Nullable List<SmartData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SmartData item) {
        helper.setText(R.id.tv_msg,item.toString());
    }
}
