package com.zyf.factory.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class SocketHelper {

    private static SocketHelper socketHelper = null;
    public Socket socket;
    Listener listener;
    BufferedWriter bufferedWriter = null;
    //获取输入流,并且指定统一的编码格式     
    BufferedReader bufferedReader = null;
    boolean write = false;
    String msg = "";

    //单例
    private SocketHelper() {

    }

    public static SocketHelper getInstance() {
        if (socketHelper == null) {
            socketHelper = new SocketHelper();
        }
        return socketHelper;
    }


    public void initSocket(final Listener listener, final String ip, final int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socketHelper.listener = listener;
                    socket = new Socket(ip, port);

                    //通过socket获取输入输出字符流
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    socket.setSoTimeout(5000);

                    //运行View层连接成功的回调
                    listener.onConnectSuccess();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true){
                                try {
                                    String str;
                                    //通过while循环不断读取信息，   
                                    while ((str = bufferedReader.readLine()) != null) {
                                        //输出打印       
                                        listener.onReceiveMsg(str);
                                    }
                                }catch (IOException e){
                                    e.printStackTrace();
                                }

                            }
                        }
                    }).start();

                    //死循环监听是否发送（发送按钮是否按下）
                    while (true) {
                        if (write) {
                            //bufferedWriter.write("\n");
                            bufferedWriter.write("\n"+msg);
                            bufferedWriter.write("\n");
                            bufferedWriter.flush();
                            write = false;
                        }
                    }

                }catch (SocketException e)
                {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                    //listener.onSocketError();
                }
            }
        }).start();
    }


    //给外部提供的发送消息的方法
    public void writeMessage(String msg) {
        write = true;
        socketHelper.msg = msg;
    }

    //给View层的接口
    public interface Listener {
        void onReceiveMsg(String msg);
        void onConnectSuccess();
        void onTimeOut();
        void onSocketError();
    }

}
