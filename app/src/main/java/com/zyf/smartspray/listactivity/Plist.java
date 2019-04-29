package com.zyf.smartspray.listactivity;
import com.zyf.common.app.Application;
import com.zyf.factory.helper.SocketHelper;
import com.zyf.factory.model.SmartData;
import com.zyf.factory.model.SmartEvent;

import java.util.List;

public class Plist implements Ilist.Presenter, SocketHelper.Listener.list {
    private static Ilist.View listView;
    private static Plist presenter;
    private SocketHelper socketHelper;
    Thread thread_send;


    private Plist(Ilist.View view){
        listView = view;

    }

    //提供给View层的获取单例对象的方法
    static Ilist.Presenter getInstance(Ilist.View view){
        if (presenter==null){
            presenter = new Plist(view);
        }
        return presenter;
    }



    @Override
    public void bindSocket(){
        socketHelper = SocketHelper.getInstance();
        socketHelper.setlListener(this);
    }

    @Override
    public void onReceiveMsg(List<SmartData> list) {
        listView.showRecvMsg(list);
        if(thread_send==null){
            thread_send = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        sendMessage(new SmartEvent("255","01","0"));
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
            thread_send.start();
        }



    }

    //真实的发命令的方法
    @Override
    public void sendMessage(SmartEvent smartEvent) {
        if (socketHelper.socket != null) {
            //构建命令
            socketHelper.writeMessage(smartEvent);
        } else {
            Application.showToast("没有socket连接");
        }
    }
}
