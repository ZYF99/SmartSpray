package com.zyf.factory.model;

import android.util.Log;

public class SmartEvent {

    String operation;              //操作编号
    String param;                  //操作参数
    String target;                 //目的硬件

    public SmartEvent(String operation, String param) {
        //目的硬件，默认为0号硬件
        this("0",operation, param);
    }

    public SmartEvent(String target,String operation, String param) {
        this.target = target;
        this.operation = operation;
        this.param = param;
    }

    public byte[] getSmartBytes(){


        byte[] before = new byte[]{(byte) 0x3A,(byte)0x00,(byte)Integer.parseInt(target), (byte) Integer.parseInt(operation)};
        byte ret = before[0];
        for (int i = 1;i<before.length;i++){
            ret = (byte) (ret^before[i]);
        }
        byte[] newBytes;
        if((byte) Integer.parseInt(operation)>0){
            newBytes = new byte[]{(byte) 0x3A,(byte)0x00,(byte)Integer.parseInt(target), (byte) Integer.parseInt(operation),ret,(byte)0x23};

        }else {
            newBytes = new byte[]{(byte) 0x3A,(byte)0x00,(byte)Integer.parseInt(target),ret,(byte)0x23};
        }
        Log.d("!!!!!!!!!!!", String.valueOf(newBytes));
        return newBytes;

    }

}
