package com.zyf.smartspray.mainactivity;

import com.zyf.factory.model.SmartEvent;

public interface Imain {

    interface View{

        //隐藏连接按钮
        void hideBtn();

        //显示loading
        void showLoading();

        //隐藏Loading
        void hideLoading();

    }
    interface Presenter{
        void initSocket(String ip,int port);
        void sendMessage(SmartEvent smartEvent);
    }

}
