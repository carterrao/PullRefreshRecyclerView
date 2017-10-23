package com.rzc.pullrefreshrecyclerview.demo.data;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rzc on 17/10/18.
 */

public class MockData {
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void mockQueryMsgList(final OnMsgListListener listener) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final List list = new ArrayList();
                list.add(new ReceivedMsg("张三", "大家好，我叫张三，很高兴认识大家"));
                list.add(new ReceivedMsg("李四", "大家好，我叫李四，很高兴认识大家"));
                list.add(new MyMsg("我发出的第一条消息"));
                list.add(new ReceivedMsg("张三", "大家好，张三又来啦~"));
                list.add(new ReceivedMsg("张三", "大家好，我张三话比较多☺"));
                list.add(new MyMsg("我发出的第二条消息"));
                list.add(new ReceivedMsg("王二", "大家好，我叫王二，很高兴认识大家"));
                list.add(new ReceivedMsg("李四", "大家好，我李四也多说一句话"));
                list.add(new MyMsg("我发出的第3条消息"));
                list.add(new MyMsg("我发出的第4条消息"));
                list.add(new ReceivedMsg("小明", "大家好，我是小明"));
                list.add(new MyMsg("我发出的第5条消息"));

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onMsgList(list);
                    }
                });
            }
        }.start();
    }

    public interface OnMsgListListener {
        void onMsgList(List msgList);
    }

    public static void mockQueryStringList(final OnStringListListener listener, final boolean mockFailed) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final List<String> list = new ArrayList<String>();
                for (int i = 0; i < 16; i++) {
                    list.add("测试条目" + i);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mockFailed) {
                            listener.onFailed();
                        } else {
                            listener.onStringList(list);
                        }
                    }
                });
            }
        }.start();
    }

    public interface OnStringListListener {
        void onStringList(List<String> strList);
        void onFailed();
    }
}
