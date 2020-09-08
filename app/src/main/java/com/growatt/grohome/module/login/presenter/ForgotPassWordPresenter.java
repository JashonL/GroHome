package com.growatt.grohome.module.login.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.http.API;
import com.growatt.grohome.module.login.view.IForgotPasswordView;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.growatt.grohome.utils.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassWordPresenter extends BasePresenter<IForgotPasswordView> {

    public ForgotPassWordPresenter(IForgotPasswordView baseView) {
        super(baseView);
    }

    public ForgotPassWordPresenter(Context context, IForgotPasswordView baseView) {
        super(context, baseView);
    }

    public void findPwdByUsername(int count) {
        String email = baseView.userEmail();
        if (TextUtils.isEmpty(email)){
            MyToastUtils.toast(R.string.m176_enter_email);
            return;
        }
        String url = "";
        if ((count == 1 && CommentUtils.getLanguage() == 0) || (count == 2 && CommentUtils.getLanguage() != 0)) {
            url = GlobalConstant.HTTP_PREFIX + API.URL_CN_HOST + API.GET_SERVER_URLBY_PARAM;
        } else {
            url = GlobalConstant.HTTP_PREFIX + API.URL_HOST + API.GET_SERVER_URLBY_PARAM;
        }
        addDisposable(apiServer.resetPassWord(url, "1", email, "1"), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String respon) {
                try {
                    JSONObject jsonObject = new JSONObject(respon);
                    String success = jsonObject.optString("success","");
                    if ("true".equals(success)) {
                        String url = jsonObject.optString("msg","");
                        if (TextUtils.isEmpty(url)) {
                            if (count == 1) {
                                findPwdByUsername(2);
                                return;
                            } else {
                                url = API.URL_HOST;
                            }
                        }
                        sendEmailByUser(url);
                    } else {
                        String str = jsonObject.optString("msg","").toString();
                        if (str.equals("501")) {
                            MyToastUtils.toast(R.string.m293_email_sending_failed);
                        }
                        if (str.equals("502")) {
                            MyToastUtils.toast(R.string.m293_user_does_not_exist);
                        }
                        if (str.equals("503")) {
                            MyToastUtils.toast(R.string.m286_fail_connect_server);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                baseView.onError(msg);
            }
        });

    }


    private void sendEmailByUser(String url) {
        String email = baseView.userEmail();
        if (TextUtils.isEmpty(email)){
            MyToastUtils.toast(R.string.m176_enter_email);
            return;
        }
        String userUrl = GlobalConstant.HTTP_PREFIX + UrlUtil.replaceUrl(url) + API.SEND_RESET_EMAIL_BY_ACCOUNT;
        addDisposable(apiServer.sendResetEmailByAccount(userUrl, email), new BaseObserver<String>(baseView, true) {
            @Override
            public void onSuccess(String respon) {
                try {
                    JSONObject jsonObject = new JSONObject(respon).getJSONObject("back");
                    String success = jsonObject.getString("success");
                    if ("true".equals(success)) {
                        CircleDialogUtils.showCommentDialog((FragmentActivity) context, context.getString(R.string.m208_note), context.getString(R.string.m294_new_password_registered), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

                    } else {
                        String str = jsonObject.getString("msg").toString();
                        if (str.equals("501")) {
                            MyToastUtils.toast(R.string.m293_email_sending_failed);
                        }
                        if (str.equals("502")) {
                            MyToastUtils.toast(R.string.m293_user_does_not_exist);
                        }
                        if (str.equals("503")) {
                            MyToastUtils.toast(R.string.m286_fail_connect_server);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                baseView.onError(msg);
            }
        });

    }

}
