package com.zyf.smartspray.mainactivity;

import android.content.Context;

import com.zyf.common.app.Application;
import com.zyf.factory.helper.SocketHelper;
import com.zyf.factory.model.SmartData;
import com.zyf.factory.model.SmartEvent;
import com.zyf.smartspray.listactivity.ListActivity;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

public class Pmain implements Imain.Presenter,SocketHelper.Listener.main {

    static Context context;
    static Imain.View mainView;
    static Pmain presenter;
    SocketHelper socketHelper;

    private Pmain(Imain.View view){
        mainView = view;
        initSocketHelper();
    }

    //初始化socketHelper对象
    void initSocketHelper(){
        socketHelper = SocketHelper.getInstance();
    }

    //向外的初始化socket的方法
    public void initSocket(String ip, int port){
        mainView.showLoading();
        //初始化Socket(用输入的ip和端口号去连接)
        socketHelper.isFirstInit = true;
        socketHelper.initSocket(this, ip,port);
    }


    //提供给View层的获取单例对象的方法
    public static Imain.Presenter getInstance(Imain.View view){
        if (presenter==null){
            presenter = new Pmain(view);
        }
        context = (Context) view;
        return presenter;
    }


    //当socket连接成功后的回调
    @Override
    public void onConnectSuccess() {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                mainView.hideBtn();
                mainView.hideLoading();
                ListActivity.show(context);
            }
        });
    }


    //真实的发命令的方法
    @Override
    public void sendMessage(SmartEvent smartEvent) {
        if (socketHelper.socket != null) {
            //构建命令
            socketHelper.writeMessage(smartEvent);
        } else {
            Application.showToast("请先连接socket服务");
        }
    }

}
