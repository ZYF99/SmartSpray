package com.zyf.factory.helper;


import android.util.Log;

import com.zyf.common.Common;
import com.zyf.factory.model.SmartData;
import com.zyf.factory.model.SmartEvent;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class SocketHelper {

    private static SocketHelper socketHelper = null;
    public Socket socket;
    private Listener.main mListener;
    private Listener.list lListener;
    private boolean write = false;
    public boolean isFirstInit;
    SmartEvent smartEvent;

    public void setlListener(Listener.list lListener) {
        this.lListener = lListener;
    }

    //单例
    private SocketHelper() {

    }

    public static SocketHelper getInstance() {
        if (socketHelper == null) {
            socketHelper = new SocketHelper();
        }
        return socketHelper;
    }


    public void initSocket(final Listener.main listener, final String ip, final int port) {
        this.mListener = listener;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socketHelper.mListener = listener;
                    socket = new Socket(ip, port);

                    BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                    final DataInputStream dis = new DataInputStream(bis);

                    //运行View层连接成功的回调
                    mListener.onConnectSuccess();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (true) {
                                    //通过while循环不断读取信息
                                    byte[] bytes = new byte[1]; // 一次读取一个byte
                                    String ret = "";
                                    while (dis.read(bytes) != -1) {
                                        ret += bytesToHexString(bytes) + " ";
                                        if (dis.available() == 0) { //一个请求
                                            if(lListener!=null){
                                                lListener.onReceiveMsg(parseToObj(ret));
                                            }
                                            ret = "";
                                        }
                                    }
                                }
                            } catch (SocketException e) {
                                e.printStackTrace();
                                //mListener.onSocketError();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    //死循环监听是否发送（发送按钮是否按下）
                    while (true) {
                        OutputStream os = socket.getOutputStream();
                        DataOutputStream ds = new DataOutputStream(os);

                        if (isFirstInit) {
                            //链接时发个1给服务器标识手机端
                            ds.writeByte(1);
                            //连接成功即发查询指令
                            ds.write(Common.select);
                            isFirstInit = false;
                        }
                        if (write) {
                            ds.write(smartEvent.getSmartBytes());
                            write = false;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //mListener.onSocketError();
                    //ExceptionUtil.decodeException(e);
                }
            }
        }).start();
    }


    //给外部提供的发送消息的方法
    public void writeMessage(SmartEvent smartEvent) {
        write = true;
        this.smartEvent = smartEvent;
    }

    //给View层的接口
    public interface Listener {
        interface list{
            void onReceiveMsg(List<SmartData> list);
        }
        interface main{
            void onConnectSuccess();
        }
    }

    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param bytes 要转换的字节数组
     * @return 转换后的结果
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();

    }

    private List<SmartData> parseToObj(String msg) {
        msg = msg.replaceAll(" ","");
        List<SmartData> list = new ArrayList<>();
        for (int i = 8; i < 39; i += 8) {
            String temp = "";
            for (int j = i; j < i + 8; j++) {
                temp += msg.charAt(j);
            }
            Log.d("temp", temp);
            list.add(new SmartData(temp));
        }
        return list;

    }
}

