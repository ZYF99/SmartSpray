package com.zyf.factory.model;


public class SmartData {

    int tempetature;//温度
    int humidity;//湿度
    boolean isGasAbnormal;//气体是否异常
    boolean isLightOn;//灯是否亮起


    public SmartData(String msg) {
        tempetature = Integer.parseInt(msg.charAt(0)+""+msg.charAt(1), 16);
        humidity = Integer.parseInt(msg.charAt(2)+""+msg.charAt(3), 16);
        isGasAbnormal = Integer.parseInt(msg.charAt(4) + "" + msg.charAt(5),16) > 0;
        isLightOn = Integer.parseInt(msg.charAt(6) + "" + msg.charAt(7),16) > 0;
    }

    @Override
    public String toString() {
        return "SmartData{" +
                "tempetature=" + tempetature +
                ", humidity=" + humidity +
                ", isGasAbnormal=" + isGasAbnormal +
                ", isLightOn=" + isLightOn +
                '}';
    }
}
