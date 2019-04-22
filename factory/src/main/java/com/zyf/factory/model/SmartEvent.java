package com.zyf.factory.model;

public class SmartEvent {

    int operation;              //操作编号
    int param;                  //操作参数
    int target;                 //目的硬件

    public SmartEvent(int operation, int param) {
        //目的硬件，默认为0号硬件
        this(operation, param, 0);
    }

    public SmartEvent(int operation, int param, int target) {
        this.operation = operation;
        this.param = param;
        this.target = target;
    }

    public String toSmartString() {

        return ""+operation+","+param+","+target;
    }

}
