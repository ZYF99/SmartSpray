package com.zyf.smartspray;

import com.zyf.factory.model.SmartEvent;

public interface Imain {

    interface View{

        //隐藏连接按钮
        void hideBtn();

        //显示loading
        void showLoading();

        //隐藏Loading
        void hideLoading();

        //显示连接超时
        void showTimeOut();

        //显示socket错误
        void showSocketError();

        //显示服务器消息
        void showRecvMsg(String msg);

        //获取设置的参数
        SmartEvent getSmartEvent();

    }
    interface Presenter{
        void initSocket(String ip,int port);
        void sendMessage();
    }

}
