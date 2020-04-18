package com.example.administrator.pushapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.pushapp.bean.PushRecordBean;
import com.example.administrator.pushapp.bean.RegIdBean;
import com.example.administrator.pushapp.cache.CacheUtils;
import com.example.administrator.pushapp.cache.OnFindData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class PushActivity extends AppCompatActivity implements OnFindData<RegIdBean> {

    @BindView(R.id.btn_push_manual)
    Button btnPushManual;
    @BindView(R.id.btn_start_push_auto)
    Button btnStartPushAuto;
    @BindView(R.id.btn_stop_push_auto)
    Button btnStopPushAuto;
    @BindView(R.id.btn_check_reg_id)
    Button btnCheckRegId;
    @BindView(R.id.btn_add_reg_id)
    Button btnAddRegId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        ButterKnife.bind(this);
        showUI(View.GONE);
        getData();
    }

    private void checkNotify() {
        int enabled = JPushInterface.isNotificationEnabled(this);
        if (enabled == 1) {
            startService();
        } else {
            endServiceNotificationDisable();
            new AlertDialog.Builder(this)
                    .setMessage("请先打开通知权限")
                    .setPositiveButton("去打开", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            JPushInterface.goToAppNotificationSettings(PushActivity.this);
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    private void startService() {
        if (!RunningService.isRunning) {
            Intent service = new Intent(this, RunningService.class);
            // Android 8.0使用startForegroundService在前台启动新服务
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(service);
            } else {
                startService(service);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNotify();
    }

    private void endServiceNotificationDisable() {
        if (RunningService.isRunning) {
            stopService(new Intent(this, RunningService.class));
        }
    }

    private void getData() {
        CacheUtils.getInstance().findAsyn(RegIdBean.class, this);
    }

    @OnClick({R.id.btn_push_manual, R.id.btn_start_push_auto, R.id.btn_stop_push_auto, R.id.btn_check_reg_id, R.id.btn_add_reg_id, R.id.btn_check_push_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_push_manual:
                Pusher.getInstance().pushRegIds(list, "推送App手动推送");
                break;
            case R.id.btn_start_push_auto:
                mHandler.sendMessage(mHandler.obtainMessage());
                break;
            case R.id.btn_stop_push_auto:
                mHandler.removeCallbacksAndMessages(null);
                break;
            case R.id.btn_check_reg_id:
                showRegIds();
                break;
            case R.id.btn_add_reg_id:
                addRegId();
                break;
            case R.id.btn_check_push_record:
                showPushRecord();
                break;
        }
    }

    private void addRegId() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_add_reg_id, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCustomTitle(v).create();
        final EditText et = v.findViewById(R.id.et_reg_id);
        View.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et.getText().toString();
                if (TextUtils.isEmpty(s) || TextUtils.isEmpty(s.trim())) {
                    ToastUtils.show("RegId不能为空");
                    return;
                }
                CacheUtils.getInstance().add(new RegIdBean(s.trim()));
                getData();
                dialog.dismiss();
            }
        };
        v.findViewById(R.id.btn_submit).setOnClickListener(btnListener);
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //dialog弹出软键盘
                dialog.getWindow()
                        .clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            }
        });
        dialog.show();
    }

    private void showRegIds() {
        CacheUtils.getInstance().findAsyn(RegIdBean.class, new OnFindData<RegIdBean>() {
            @Override
            public void onFind(List<RegIdBean> list) {
                showRegIds(list);
            }
        });
    }

    private void showRegIds(List<RegIdBean> list) {
        showAlertDiaot(list, new ViewBinder<RegIdBean>() {
            @Override
            public void bind(@NonNull View v, RegIdBean bean) {
                TextView tv = v.findViewById(R.id.tv_time);
                tv.setText(bean.getRegId());
            }
        });
    }

    private void showPushRecord() {
        CacheUtils.getInstance().findAsyn(PushRecordBean.class, new OnFindData<PushRecordBean>() {
            @Override
            public void onFind(List<PushRecordBean> list) {
                showPushRecord(list);
            }
        });
    }

    private void showPushRecord(final List<PushRecordBean> list) {
        if (list == null || list.size() == 0) {
            ToastUtils.show("没查到推送记录");
            return;
        }
        showAlertDiaot(list, new ViewBinder<PushRecordBean>() {
            @Override
            public void bind(@NonNull View itemView, PushRecordBean bean) {
                TextView time = itemView.findViewById(R.id.tv_time);
                TextView status = itemView.findViewById(R.id.tv_status);
                time.setText(bean.getTime());
                status.setText(bean.isSucceed() ? "成功" : "失败");
            }
        });
    }

    private <T> void showAlertDiaot(final List<T> l, final ViewBinder<T> binder) {
        if (l == null || l.size() == 0) {
            ToastUtils.show("数据为空");
            return;
        }
        final List<T> list = new ArrayList<>();
        for (int i = l.size()-1; i >= 0; i--) {
            list.add(l.get(i));
        }

        View v = LayoutInflater.from(this)
                .inflate(R.layout.dialog_push_record, null, false);
        RecyclerView rv = v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(PushActivity.this).inflate(R.layout.item, viewGroup, false);
                RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(v) {
                };
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                View itemView = viewHolder.itemView;
                T t = list.get(i);
                binder.bind(itemView, t);
            }

            @Override
            public int getItemCount() {
                return list.size();
            }
        });
        new AlertDialog.Builder(this)
                .setCustomTitle(v)
                .create()
                .show();
    }

    interface ViewBinder<T> {
        void bind(@NonNull View v, T bean);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            removeCallbacksAndMessages(null);
            if (list == null || list.isEmpty()) {
                ToastUtils.show("regId不能为空");
                return;
            }
            sendMessageDelayed(obtainMessage(), 60_000);
            Pusher.getInstance().pushRegIds(list, "推送App自动推送");
        }
    };

    @Override
    public void onFind(List<RegIdBean> beans) {
        if (beans == null || beans.size() == 0) {
            beans = new ArrayList<>();
            RegIdBean regIdBean1 = new RegIdBean("1a0018970a3f030d35f");
            beans.add(regIdBean1);
            RegIdBean regIdBean2 = new RegIdBean("13065ffa4e96ce2df60");
            beans.add(regIdBean2);
            RegIdBean regIdBean3 = new RegIdBean("1104a897923712bdbbe");
            beans.add(regIdBean3);
            CacheUtils.getInstance().add(regIdBean1, regIdBean2, regIdBean3);
        }

        list.clear();
        for (RegIdBean bean : beans) {
            list.add(bean.getRegId());
        }

        showUI(View.VISIBLE);
    }

    private List<String> list = new ArrayList<>();

    private void showUI(int visibility) {
        btnAddRegId.setVisibility(visibility);
        btnCheckRegId.setVisibility(visibility);
        btnPushManual.setVisibility(visibility);
        btnStopPushAuto.setVisibility(visibility);
        btnStartPushAuto.setVisibility(visibility);
    }

    private long lastClickBackTime;

    @Override
    public void onBackPressed() {
        long cur = System.currentTimeMillis();
        if (cur - lastClickBackTime > 1000) {
            Toast.makeText(this, "再点一次退出应用", Toast.LENGTH_LONG).show();
            lastClickBackTime = cur;
        } else {
            lastClickBackTime = cur;
            super.onBackPressed();
        }
    }
}
