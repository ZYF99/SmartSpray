package com.zyf.smartspray;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.os.Handler;
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
import com.zyf.factory.model.SmartEvent;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

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

    @BindView(R.id.edit_param1)
    EditText edit_param1;

    @BindView(R.id.edit_param2)
    EditText edit_param2;

    @BindView(R.id.edit_param3)
    EditText edit_param3;

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
        objectAnimator = ObjectAnimator.ofFloat(btn_setting,"rotation",360f);
        objectAnimator.setDuration(2000);

        rockerView.setOnShakeListener(DIRECTION_8, new RockerView.OnShakeListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void direction(RockerView.Direction direction) {
                if (direction == RockerView.Direction.DIRECTION_CENTER){
                    btn_connect.setText("当前方向：中心");
                }else if (direction == RockerView.Direction.DIRECTION_DOWN){
                    btn_connect.setText("当前方向：下");
                }else if (direction == RockerView.Direction.DIRECTION_LEFT){
                    btn_connect.setText("当前方向：左");
                }else if (direction == RockerView.Direction.DIRECTION_UP){
                    btn_connect.setText("当前方向：上");
                }else if (direction == RockerView.Direction.DIRECTION_RIGHT){
                    btn_connect.setText("当前方向：右");
                }else if (direction == RockerView.Direction.DIRECTION_DOWN_LEFT){
                    btn_connect.setText("当前方向：左下");
                }else if (direction == RockerView.Direction.DIRECTION_DOWN_RIGHT){
                    btn_connect.setText("当前方向：右下");
                }else if (direction == RockerView.Direction.DIRECTION_UP_LEFT){
                    btn_connect.setText("当前方向：左上");
                }else if (direction == RockerView.Direction.DIRECTION_UP_RIGHT){
                    btn_connect.setText("当前方向：右上");
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
        /*
        String ip = Common.HOST;
        String port = Common.PORT+"";
        */
        if (ip.length() > 5 && port.length() > 1) {

            presenter.initSocket(ip, Integer.parseInt(port));

        } else {
            Application.showToast("请输入正确ip和端口号");
        }

    }

    //点击了发送按钮
    @OnClick(R.id.btn_sendmsg)
    void onSendMsgClick() {

        presenter.sendMessage();
    }

    @OnClick(R.id.btn_setting)
    void onSettingClick() {
        objectAnimator.start();
        if (!isSettingShow) {
            lin_setting.setVisibility(View.VISIBLE);
            isSettingShow = true;
            objectAnimator = ObjectAnimator.ofFloat(btn_setting,"rotation",0f);
            objectAnimator.setDuration(1000);
        } else {
            lin_setting.setVisibility(View.INVISIBLE);
            isSettingShow = false;
            objectAnimator = ObjectAnimator.ofFloat(btn_setting,"rotation",360f);
            objectAnimator.setDuration(1000);
        }
    }


    /*纯view操作*****************************************************/
    //获取设置的参数
    @Override
    public SmartEvent getSmartEvent() {
        int param1 = Integer.parseInt(edit_param1.getText().toString());
        int param2 = Integer.parseInt(edit_param2.getText().toString());
        int param3 = Integer.parseInt(edit_param3.getText().toString());
        SmartEvent smartEvent = new SmartEvent(param1,param2,param3);
        return smartEvent;
    }

    @Override
    public void hideBtn() {
        btn_connect.setVisibility(View.INVISIBLE);
    }

    //显示loading的具体实现
    @Override
    public void showLoading() {

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

    //显示服务器消息
    @Override
    public void showRecvMsg(String msg) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                Application.showToast(msg);
            }
        });
    }

    //显示连接超时
    @Override
    public void showTimeOut() {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                titanicTextView.setText("SmartSpray");
                titanic.cancel();
                Application.showToast("连接超时");
            }
        });
    }

    //显示socket错误
    @Override
    public void showSocketError() {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                btn_connect.setVisibility(View.VISIBLE);
                titanicTextView.setVisibility(View.INVISIBLE);
            }
        });

    }
}
