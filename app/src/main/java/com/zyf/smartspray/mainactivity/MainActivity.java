package com.zyf.smartspray.mainactivity;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zyf.common.app.Activity;
import com.zyf.common.app.Application;
import com.zyf.common.widget.RockerView;
import com.zyf.common.widget.Titanic;
import com.zyf.common.widget.TitanicTextView;
import com.zyf.common.SharedPreferencesUtil;
import com.zyf.factory.model.SmartData;
import com.zyf.factory.model.SmartEvent;
import com.zyf.smartspray.R;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zyf.common.widget.RockerView.DirectionMode.DIRECTION_8;

public class MainActivity extends Activity implements Imain.View {

    @BindView(R.id.btn_connect)
    Button btn_connect;

    @BindView(R.id.lin_setting)
    LinearLayout lin_setting;

    @BindView(R.id.edit_ip)
    EditText edit_ip;

    @BindView(R.id.edit_port)
    EditText edit_port;

    @BindView(R.id.btn_setting)
    ImageView btn_setting;

    @BindView(R.id.my_rocker)
    RockerView rockerView;

    @BindView(R.id.titanic_tv)
    TitanicTextView titanicTextView;
    Titanic titanic;

    boolean isSettingShow = false;
    ObjectAnimator objectAnimator;
    private static Imain.Presenter presenter;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    //初始化界面控件
    @Override
    protected void initWidget() {
        super.initWidget();
        //设置loading的字体
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Satisfy-Regular.ttf");
        titanicTextView.setTypeface(typeface);
        //设置loading的动画
        titanic = new Titanic();
        //设置setting的动画
        objectAnimator = ObjectAnimator.ofFloat(btn_setting, "rotation", 360f);
        objectAnimator.setDuration(2000);
        //读取上次输入的ip和端口
        edit_ip.setText((String) SharedPreferencesUtil.getData("ip", "0.0.0.0"));
        edit_port.setText((String) SharedPreferencesUtil.getData("port", "8080"));
        rockerView.setOnShakeListener(DIRECTION_8, new RockerView.OnShakeListener() {
            @Override
            public void onStart() {

            }

            /*
             * OA 10
             * OB 11
             * OC 12
             * OD 13
             * OE 14
             *
             * */


            @Override
            public void direction(RockerView.Direction direction) {
                switch (direction) {
                    case DIRECTION_CENTER:
                        //当前方向：中心
                        presenter.sendMessage(new SmartEvent("01", "14", "17"));
                        break;
                    case DIRECTION_UP:
                        //当前方向：上
                        break;
                    case DIRECTION_UP_RIGHT:
                        //当前方向：右上
                        presenter.sendMessage(new SmartEvent("01", "14", "0"));
                        break;
                    case DIRECTION_RIGHT:
                        //当前方向：右
                        presenter.sendMessage(new SmartEvent("01", "14", "0"));
                        break;
                    case DIRECTION_DOWN_RIGHT:
                        //当前方向：右下
                        presenter.sendMessage(new SmartEvent("01", "14", "0"));
                        break;
                    case DIRECTION_DOWN:
                        //当前方向：下
                        break;
                    case DIRECTION_DOWN_LEFT:
                        //当前方向：左下
                        presenter.sendMessage(new SmartEvent("01", "14", "1"));
                        break;
                    case DIRECTION_LEFT:
                        //当前方向：左
                        presenter.sendMessage(new SmartEvent("01", "14", "1"));
                        break;
                    case DIRECTION_UP_LEFT:
                        //当前方向：左上
                        presenter.sendMessage(new SmartEvent("0001", "14", "1"));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFinish() {

            }

        });
    }

    //界面初始化后记载数据
    @Override
    protected void initData() {
        super.initData();
        //设置一个presenter
        presenter = Pmain.getInstance(MainActivity.this);

    }

    //点击了连接按钮
    @OnClick(R.id.btn_connect)
    void onConnectClick() {
        titanicTextView.setVisibility(View.VISIBLE);
        String ip = edit_ip.getText().toString();
        String port = edit_port.getText().toString();
        if (ip.length() > 5 && port.length() > 1) {

            SharedPreferencesUtil.putData("ip", ip);
            SharedPreferencesUtil.putData("port", port);
            presenter.initSocket(ip, Integer.parseInt(port));
        } else {
            Application.showToast("请输入正确ip和端口号");
        }

    }

    //点击了发送按钮
    @OnClick(R.id.btn_sendmsg)
    void onSendMsgClick() {
/*        //查询
        presenter.sendMessage(new SmartEvent("255","01","0"));*/

    }

    @OnClick(R.id.btn_setting)
    void onSettingClick() {
        objectAnimator.start();
        if (!isSettingShow) {
            lin_setting.setVisibility(View.VISIBLE);
            isSettingShow = true;
            objectAnimator = ObjectAnimator.ofFloat(btn_setting, "rotation", 0f);
            objectAnimator.setDuration(1000);
        } else {
            lin_setting.setVisibility(View.INVISIBLE);
            isSettingShow = false;
            objectAnimator = ObjectAnimator.ofFloat(btn_setting, "rotation", 360f);
            objectAnimator.setDuration(1000);
        }
    }


    /*纯view操作*****************************************************/

    @Override
    public void hideBtn() {
        btn_connect.setVisibility(View.INVISIBLE);
    }

    //显示loading的具体实现
    @Override
    public void showLoading() {
        hideBtn();
        titanicTextView.setText("Connecting");
        titanic.start(titanicTextView);

    }

    //隐藏loading的具体实现
    @Override
    public void hideLoading() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                titanicTextView.setText("Connected");
                titanic.cancel();
            }
        }, 4000);
    }

}
