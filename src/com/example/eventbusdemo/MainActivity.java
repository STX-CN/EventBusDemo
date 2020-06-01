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
    	MessageEvent event = new MessageEvent("主线程MSG");
    	EventBus.getDefault().post(event);
    }

    /**
     * 子线程消息通知给主线程，功能与Handler类似
     * @param v
     */
    public void onSTPostEventBtn(View v){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
		    	MessageEvent event = new MessageEvent("子线程MSG");
		    	EventBus.getDefault().post(event);
			}
    	};
    	Thread th = new Thread(r);
    	th.start();
    }
    
    /**
     * 在线程中实现UI展示
     * 根据Android本来得定义，子线程中不能展示UI, 为什么? 原因是子线程中没有消息循环，
     * 所以在子线程中将消息循环给加上就可以展示UI，但是UI展示完毕后，必须退出消息循环，
     * 否者会引起严重的内存泄露
     * @param v
     */
    public void onSTUIBtn(View v){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, "子线程UI", Toast.LENGTH_SHORT).show();
				Looper.loop();
				Looper.myLooper().quitSafely();  //必须退出Looper, 否者会引起内存泄漏
			}
    	};
    	Thread th = new Thread(r);
    	th.start();
    }
    
    
}










