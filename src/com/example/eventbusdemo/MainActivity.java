package com.example.eventbusdemo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {

	private Context context;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = this;
        
        EventBus.getDefault().register(this);
    }
    
 // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
    }

    // This method will be called when a SomeOtherEvent is posted
    @Subscribe
    public void handleSomethingElse(SomeOtherEvent event) {
        doSomethingWith(event);
    }
    
    
    private void doSomethingWith(SomeOtherEvent event){
    }
    
    public void onDestory(){
    	EventBus.getDefault().unregister(this);
    	super.onDestroy();
    }
    
    public void onMTPostEventBtn(View v){
    	MessageEvent event = new MessageEvent("���߳�MSG");
    	EventBus.getDefault().post(event);
    }

    /**
     * ���߳���Ϣ֪ͨ�����̣߳�������Handler����
     * @param v
     */
    public void onSTPostEventBtn(View v){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
		    	MessageEvent event = new MessageEvent("���߳�MSG");
		    	EventBus.getDefault().post(event);
			}
    	};
    	Thread th = new Thread(r);
    	th.start();
    }
    
    /**
     * ���߳���ʵ��UIչʾ
     * ����Android�����ö��壬���߳��в���չʾUI, Ϊʲô? ԭ�������߳���û����Ϣѭ����
     * ���������߳��н���Ϣѭ�������ϾͿ���չʾUI������UIչʾ��Ϻ󣬱����˳���Ϣѭ����
     * ���߻��������ص��ڴ�й¶
     * @param v
     */
    public void onSTUIBtn(View v){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, "���߳�UI", Toast.LENGTH_SHORT).show();
				Looper.loop();
				Looper.myLooper().quitSafely();  //�����˳�Looper, ���߻������ڴ�й©
			}
    	};
    	Thread th = new Thread(r);
    	th.start();
    }
    
    
}










