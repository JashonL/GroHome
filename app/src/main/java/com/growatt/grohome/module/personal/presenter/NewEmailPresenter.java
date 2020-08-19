package com.growatt.grohome.module.personal.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.growatt.grohome.R;
import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.personal.view.INewEmailView;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class NewEmailPresenter extends BasePresenter<INewEmailView> {

    private String userEmail;

    private String vCode;

    private final int TOTAL_TIME = 180;
    private int TIME_COUNT = TOTAL_TIME;

    public NewEmailPresenter(INewEmailView baseView) {
        super(baseView);
    }

    public NewEmailPresenter(Context context, INewEmailView baseView) {
        super(context, baseView);
        initHandler(context);
    }


    public void sendSms(View v) {
        String email = baseView.getEmail();
        if (TextUtils.isEmpty(email)){
            MyToastUtils.toast(R.string.m176_enter_email);
            return;
        }
        //点击之后获取验证码按钮置为灰色
        baseView.getVerificationCode();
        //隐藏输入法
        CommentUtils.hideKeyboard(v);
        vCode = "";
        String userUrl = App.getUserBean().getUrl();
        String type = "0";//0代表邮箱
        addDisposable(apiServer.validate(userUrl, type ,email), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String respon) {
                try {
                    JSONObject jsonObject = new JSONObject(respon);
                    int result = jsonObject.getInt("result");
                    if (result == 1) {
                        vCode = jsonObject.getJSONObject("obj").getString("validate");
                        MyToastUtils.toast(R.string.m200_success);
                    } else {
                        handler.sendEmptyMessage(101);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }



    public void updateUserEmail() {
        String userUrl = App.getUserBean().getUrl();
        String accountName = App.getUserBean().getAccountName();
        String email = baseView.getEmail();
        if (TextUtils.isEmpty(email)){
            MyToastUtils.toast(R.string.m176_enter_email);
            return;
        }
        addDisposable(apiServer.updateValidate(userUrl, accountName, email), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String respon) {
                try {
                    JSONObject jsonObject = new JSONObject(respon);
                    int result = jsonObject.getInt("result");
                    if (result == 1) {
                        App.getUserBean().setEmail(email);
                        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m200_success), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateUser();
                            }
                        });

                    } else {
                        handler.sendEmptyMessage(101);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }



    public void updateUser() {
        String userUrl = App.getUserBean().getUrl();
        String accountName = App.getUserBean().getAccountName();
        String email = baseView.getEmail();
        if (TextUtils.isEmpty(email)){
            MyToastUtils.toast(R.string.m176_enter_email);
            return;
        }
        addDisposable(apiServer.updateUser(userUrl, accountName, email), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String respon) {
                try {
                    JSONObject jsonObject = new JSONObject(respon);
                    if (jsonObject.get("success").toString().equals("true")) {
                        Intent intent = new Intent();
                        intent.putExtra("email", email);
                        ((Activity)context).setResult(Activity.RESULT_OK, intent);
                        ((Activity)context).finish();
                    } else if ("701".equals(jsonObject.get("msg").toString())) {
                        MyToastUtils.toast(R.string.m201_fail);
                    } else {
                        MyToastUtils.toast(R.string.m201_fail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }


    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 101://发送验证码
                CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m201_fail), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 102://发送验证码后倒计时
                TIME_COUNT--;
                if (TIME_COUNT <= 0) {
                    baseView.getVerificationCodeEnd();
                } else {
                    String countDown=TIME_COUNT+"s";
                    baseView.setCountDown(countDown);
                    //发送消息
                    handler.sendEmptyMessageDelayed(102, 1000);
                }
                break;
        }
        return super.handleMessage(msg);
    }
}
