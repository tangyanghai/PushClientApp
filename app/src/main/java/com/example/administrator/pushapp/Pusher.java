package com.example.administrator.pushapp;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.example.administrator.pushapp.bean.PushRecordBean;
import com.example.administrator.pushapp.cache.CacheUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>@author : tangyanghai</p>
 * <p>@time : 2020/4/16</p>
 * <p>@for : </p>
 * <p></p>
 */
public class Pusher {
    private static final Pusher ourInstance = new Pusher();
    private SimpleDateFormat simpleDateFormat;

    public static Pusher getInstance() {
        return ourInstance;
    }

    private Pusher() {

    }

    public void pushRegIds(List<String> regIds, String title) {
        if (regIds == null || regIds.isEmpty()) {
            ToastUtils.show("regids不能为空");
            return;
        }

        if (TextUtils.isEmpty(title)) {
            title = "推送app";
        }
        PushEntity pushEntity = new PushEntity("android", new Audit(regIds), new Message(getTime(), title));
        String s = JSON.toJSONString(pushEntity);
        push(s);
    }

    private void push(final String json) {
        RequestParams params = new RequestParams();
        params.addHeader("Authorization", "Basic YjFiZjY3Zjc0MTMxMGU5ZDdmMjM2YmNiOjQ2ZjYxMjkzMWM2ODk5NzJkYWUxMzJkYQ==");
        params.addHeader("Accept", "application/json");
        params.addHeader("Content-Type", "application/json");
        params.setUri("https://api.jpush.cn/v3/push");
        params.setAsJsonContent(true);
        params.setBodyContent(json);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ToastUtils.show("推送发送成功!");
                PushRecordBean bean = new PushRecordBean(true, json, getTime());
                CacheUtils.getInstance().add(bean);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.show("推送发送失败!");
                PushRecordBean bean = new PushRecordBean(false, json, getTime());
                CacheUtils.getInstance().add(bean);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private String getTime() {
        // HH:mm:ss
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        }
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    static class PushEntity {
        private String platform;
        private Audit audience;
        private Message message;

        public PushEntity(String platform, Audit audience, Message message) {
            this.platform = platform;
            this.audience = audience;
            this.message = message;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public Audit getAudience() {
            return audience;
        }

        public void setAudience(Audit audience) {
            this.audience = audience;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }
    }

    static class Audit {
        private List<String> registration_id;

        public Audit(List<String> registration_id) {
            this.registration_id = registration_id;
        }

        public List<String> getRegistration_id() {
            return registration_id;
        }

        public void setRegistration_id(List<String> registration_id) {
            this.registration_id = registration_id;
        }
    }

    static class Message {
        private String msg_content;
        private String title;

        public String getMsg_content() {
            return msg_content;
        }

        public void setMsg_content(String msg_content) {
            this.msg_content = msg_content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Message(String msg_content, String title) {
            this.msg_content = msg_content;
            this.title = title;
        }
    }

}
