package tw.sean.threadtest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private UIHandler uiHandler;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.mesg);
    }

    public void test1(View view){
        new Thread(){
            @Override
            public void run() {
                doThread1();
            }
        }.start();
    }
    public void test2(View view){
        timer.schedule(new MyTask(), 0, 1000);
    }

    //
    private class MyTask extends TimerTask{
        int i;
        @Override
        public void run() {
            Log.v("brad","do MyTask: " + i++);
            Message message = new Message();
            message.what = 2;
            Bundle data = new Bundle();
            data.putString("Key", "i =" + i);
            message.setData(data);
            uiHandler.sendMessage(message);
            try{
                Thread.sleep(500);
            }catch (InterruptedException ie){

            }
        }
    }

    //執行緒：版本5的會掛掉，版本6的可運作
    private void doThread1(){
        for (int i=0; i<100; i++){
            Log.v("brad", "i= " + i);
            //textView.setText("i = " + i); //在這掛掉
            Message message = new Message();
            Bundle data = new Bundle();
            data.putString("key","i= " + i);
            message.setData(data);//要new bundle data
            //uiHandler.sendEmptyMessage();//(in what):是狀態碼
            uiHandler.sendMessage(message);
            try{
                Thread.sleep(500);
            }catch (InterruptedException ie){

            }
        }
    }

    //避免掛掉的作法:在前景與背景中間當作中介
    private class UIHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String value = msg.getData().getString("key");
            textView.setText(value);
        }
    }
}
