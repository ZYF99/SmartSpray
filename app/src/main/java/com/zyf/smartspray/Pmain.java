package com.zyf.smartspray;

import com.zyf.common.app.Application;
import com.zyf.factory.helper.SocketHelper;
import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

public class Pmain implements Imain.Presenter,SocketHelper.Listener {

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
        socketHelper.initSocket(this, ip,port);
    }



    //提供给View层的获取单例对象的方法
    public static Imain.Presenter getInstance(Imain.View view){
        if (presenter==null){
            presenter = new Pmain(view);
        }
        return presenter;
    }


    //当socket从服务端接到数据的回调
    @Override
    public void onReceiveMsg(final String msg) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                mainView.showRecvMsg(msg);
            }
        });
    }

    //当socket连接成功后的回调
    @Override
    public void onConnectSuccess() {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                mainView.hideBtn();
                mainView.hideLoading();
            }
        });
    }

    //向外提供的发命令的方法
    @Override
    public void sendMessage(){
        if (socketHelper.socket != null) {
            //构建命令
            socketHelper.writeMessage(mainView.getSmartEvent().toSmartString());
        } else {
            Application.showToast("请先连接socket服务");
        }
    }

    //连接超时的监听
    @Override
    public void onTimeOut() {
        mainView.showTimeOut();
    }

    //socket连接发生错误
    @Override
    public void onSocketError() {
        mainView.showSocketError();
    }
}
