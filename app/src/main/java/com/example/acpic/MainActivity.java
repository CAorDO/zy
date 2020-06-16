package com.example.acpic;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int INTERVALTIME=500;
    private long lastTime;
    private SensorManager sensorManager;
    private sensorListener sensor;//监听类
    public static int flag=0;
    private static LinearLayout linearLayout=null;//定义了一个布局对象

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout=(LinearLayout)findViewById(R.id.activity_main);
        linearLayout.setBackgroundResource(R.drawable.img1);
    }
    //注册传感器

    @Override
    protected void onStart() {
        super.onStart();
        if (sensorManager==null){
           sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        }
        sensor=new sensorListener();
        //注册加速度传感器
        sensorManager.registerListener(sensor,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    //接受处理消息
    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //根据晃动接收消息，更换图片
            switch(flag){
                case 0:
                    linearLayout.setBackgroundResource(R.drawable.img1);
                    break;
                case 1:
                    linearLayout.setBackgroundResource(R.drawable.img2);
                    break;
                case 2:
                    linearLayout.setBackgroundResource(R.drawable.img3);
                    break;
                case 3:
                    linearLayout.setBackgroundResource(R.drawable.img4);
                    break;



            }
        }
    };
   private class sensorListener implements SensorEventListener{

       @Override//当传感器数值发生变化时
       public void onSensorChanged(SensorEvent event) {
           long currentUpdateTime=System.currentTimeMillis();
           long timeInterval=currentUpdateTime-lastTime;
           if(timeInterval<INTERVALTIME)
               return;
               lastTime=currentUpdateTime;
                int sensorType=event.sensor.getType();
                float[] values=event.values;
                if (sensorType==Sensor.TYPE_ACCELEROMETER){
                    if((Math.abs(values[0])>12)||(Math.abs(values[1])>12)||(Math.abs(values[0])>12)){
                        flag++;
                        if(flag>3)flag=0;
                        //发送消息
                        mHandler.sendEmptyMessage(0);
                    }
                }
       }

       @Override
       public void onAccuracyChanged(Sensor sensor, int accuracy) {

       }
   }



}
