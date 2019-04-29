package com.zyf.smartspray.listactivity;

import com.zyf.factory.model.SmartData;
import com.zyf.factory.model.SmartEvent;

import java.util.List;

public interface Ilist {
    interface View{
        void showRecvMsg(List<SmartData>list);
    }
    interface Presenter{
        void sendMessage(SmartEvent smartEvent);
        void bindSocket();
    }
}
