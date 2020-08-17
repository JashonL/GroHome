package com.growatt.grohome.module.personal.presenter;

import android.content.Context;

import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.module.personal.view.IUpdatePwdView;

public class UpdatePwdPresenter extends BasePresenter<IUpdatePwdView> {
    public UpdatePwdPresenter(IUpdatePwdView baseView) {
        super(baseView);
    }

    public UpdatePwdPresenter(Context context, IUpdatePwdView baseView) {
        super(context, baseView);
    }


    	public void changePassword(){



/*
			if(Cons.isflag){
				toast(R.string.all_experience);
				return;
			}
			if (isEmpty(et1,et2,et3)) return;
//			if(s1.equals("")||s2.equals("")||s3.equals("")){
//				toast(R.string.all_blank);
//				return;
//			}
			String s1=et1.getText().toString().trim();
			String s2=et2.getText().toString().trim();
			String s3=et3.getText().toString().trim();
			if (s2.length() < 6 || s3.length() < 6) {
				toast(R.string.login_no_pwdlength);
				return;
			}
			if(!s2.equals(s3)){
				toast(R.string.register_password_no_same);
				return;
			}
			Mydialog.Show(UpdatepwdActivity.this,"");
			PostUtil.post(new Urlsutil().updateUserPassword, new postListener() {

				@Override
				public void success(String json) {
					try {
						Mydialog.Dismiss();
						JSONObject jsonObject=new JSONObject(json);
						if(jsonObject.get("msg").toString().equals("200")){
							toast(R.string.all_success);
							jumpTo(LoginActivity.class, true);
						}else if(jsonObject.get("msg").toString().equals("502")){
							toast(R.string.updatepwd_oldpwd_failed);
						}else if("701".equals(jsonObject.get("msg").toString())){
							toast(R.string.m7);
						}else{
							toast(R.string.serviceerror);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void Params(Map<String, String> params) {
					Map<String, Object> map = SqliteUtil.inquirylogin();
					params.put("accountName",map.get("name").toString());
					params.put("passwordOld", s1);
					params.put("passwordNew", s2);
				}

				@Override
				public void LoginError(String str) {
					// TODO Auto-generated method stub

				}
			});*/
	}
}
