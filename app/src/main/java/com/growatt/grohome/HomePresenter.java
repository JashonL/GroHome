package com.growatt.grohome;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.growatt.grohome.app.App;
import com.growatt.grohome.base.BaseBean;
import com.growatt.grohome.base.BaseObserver;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.eventbus.DeviceStatusMessage;
import com.growatt.grohome.jpush.TagAliasOperatorHelper;
import com.growatt.grohome.module.login.RegisterLoginActivity;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.ActivityUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.GlideUtils;
import com.growatt.grohome.utils.MD5andKL;
import com.growatt.grohome.utils.SharedPreferencesUnit;
import com.mylhyl.circledialog.res.drawable.CircleDrawable;
import com.mylhyl.circledialog.res.values.CircleColor;
import com.mylhyl.circledialog.res.values.CircleDimen;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.growatt.grohome.jpush.TagAliasOperatorHelper.sequence;


public class HomePresenter  extends BasePresenter<IMainActivityView> {

    public HomePresenter(IMainActivityView baseView) {
        super(baseView);
    }


    public HomePresenter(Context context,IMainActivityView baseView) {
        super(context, baseView);
    }

    /**
     * 第一次加载文章列表
     */
    public void getArticleList() {
        addDisposable(apiServer.getArticleList(0), new BaseObserver<String>(baseView,true) {
            @Override
            public void onSuccess(String bean) {
                baseView.setArticleData(bean);
            }

            @Override
            public void onError(String msg) {
                baseView.showArticleError(msg + "(°∀°)ﾉ");
            }
        });
    }



    /**
     * 收藏
     *
     * @param id 文章id
     */
    public void collect(int id) {
        addDisposable(apiServer.collectIn(id), new BaseObserver<BaseBean>(baseView) {
            @Override
            public void onSuccess(BaseBean bean) {
                baseView.showCollectSuccess("收藏成功（￣▽￣）");
            }

            @Override
            public void onError(String msg) {
                baseView.showCollectError(msg + "(°∀°)ﾉ");
            }
        });
    }

    /**
     * 取消收藏
     *
     * @param id 文章id
     */
    public void uncollect(int id) {
        addDisposable(apiServer.uncollect(id), new BaseObserver<BaseBean>(baseView) {
            @Override
            public void onSuccess(BaseBean bean) {
                baseView.showUncollectSuccess("取消收藏成功（￣▽￣）");
            }

            @Override
            public void onError(String msg) {
                baseView.showUncollectError(msg + "(°∀°)ﾉ");
            }
        });
    }



    /**
     * 登录涂鸦
     */
    public void loginTuya(Context context){
        String accountName = App.getUserBean().getAccountName();
        String code = App.getUserBean().getUserTuyaCode();
        TuyaApiUtils.autoLogin(context,code, App.getUserBean().getAccountName(), MD5andKL.encryptPassword(accountName),loginCallback);
    }

    private ILoginCallback loginCallback=new ILoginCallback() {
        @Override
        public void onSuccess(User user) {
            //登录成功处理
            isNeedCreateHome();
            Log.d("tuyalogin","登录成功");
        }

        @Override
        public void onError(String code, String error) {
            baseView.showTuyaLoginError();
            //登录失败处理
            Log.d("tuyalogin","登录失败");
        }
    };



    /**
     * 判断是否需要创建一个家庭
     */
    private void isNeedCreateHome() {
        //登录涂鸦成功,获取家庭，如果没有创建家庭那么就创建一个
        TuyaApiUtils.getHomeList(homelistCallBack);
    }


    private ITuyaGetHomeListCallback homelistCallBack=new ITuyaGetHomeListCallback() {

        @Override
        public void onSuccess(List<HomeBean> homeBeans) {
            if (CommentUtils.isEmpty(homeBeans)) {
                createHome();
            } else {
                initHome();
            }
        }

        @Override
        public void onError(String errorCode, String error) {
            baseView.showTuyaLoginError();
        }
    };


    /**
     * 创建一个默认的涂鸦家庭
     */
    private void createHome() {
        ArrayList<String> rooms = new ArrayList<>();
        rooms.add("bedroom");
        TuyaApiUtils.createHome("MyHome", rooms, new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean homeBean) {
                initHome();
            }

            @Override
            public void onError(String s, String s1) {
                baseView.showTuyaLoginError();
            }
        });
    }



    private void initHome() {
        TuyaApiUtils.getHomeDetail(TuyaApiUtils.getHomeId(), new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean homeBean) {
                TuyaApiUtils.setIsHomeInit(true);
                EventBus.getDefault().post(new DeviceStatusMessage());
                Log.d("initHome","家庭初始化成功");
            }

            @Override
            public void onError(String s, String s1) {
                baseView.showTuyaLoginError();
                Log.d("initHome","家庭初始化失败");
            }
        });
    }


    public void showLoginError(){
        View bodyView = LayoutInflater.from(context).inflate(R.layout.dialog_layout_login, null, false);
        CircleDialogUtils.showCostomBodyViewDialog(context, bodyView, "", ((FragmentActivity)context).getSupportFragmentManager(), new OnCreateBodyViewListener() {
            @Override
            public void onCreateBodyView(View view) {
                CircleDrawable bgCircleDrawable = new CircleDrawable(CircleColor.DIALOG_BACKGROUND
                        , CircleDimen.DIALOG_RADIUS, CircleDimen.DIALOG_RADIUS, CircleDimen.DIALOG_RADIUS, CircleDimen.DIALOG_RADIUS);
                view.setBackgroundDrawable(bgCircleDrawable);
                ImageView ivPic = view.findViewById(R.id.iv_pic);
                TextView tvTitle = view.findViewById(R.id.tv_title);
                TextView tvContent = view.findViewById(R.id.tv_content);
                Button btnNext = view.findViewById(R.id.btn_next);
                GlideUtils.showImageAct((Activity) context, R.drawable.linkage_notadd_dialog, ivPic);
                tvTitle.setText(R.string.m332_login_failed);
                tvContent.setText(R.string.m333_re_login);
                btnNext.setText(R.string.m14_login);
                btnNext.setOnClickListener(v -> {
                    applogout();
                });
            }
        }, context.getString(R.string.m127_no),null,context.getString(R.string.m90_ok),null,false);
    }





    public void showSsionExpired(){
        View bodyView = LayoutInflater.from(context).inflate(R.layout.dialog_layout_login, null, false);
        CircleDialogUtils.showCostomBodyViewDialog(context, bodyView, "", ((FragmentActivity)context).getSupportFragmentManager(), new OnCreateBodyViewListener() {
            @Override
            public void onCreateBodyView(View view) {
                CircleDrawable bgCircleDrawable = new CircleDrawable(CircleColor.DIALOG_BACKGROUND
                        , CircleDimen.DIALOG_RADIUS, CircleDimen.DIALOG_RADIUS, CircleDimen.DIALOG_RADIUS, CircleDimen.DIALOG_RADIUS);
                view.setBackgroundDrawable(bgCircleDrawable);
                ImageView ivPic = view.findViewById(R.id.iv_pic);
                TextView tvTitle = view.findViewById(R.id.tv_title);
                TextView tvContent = view.findViewById(R.id.tv_content);
                Button btnNext = view.findViewById(R.id.btn_next);
                GlideUtils.showImageAct((Activity) context, R.drawable.linkage_notadd_dialog, ivPic);
                tvTitle.setText(R.string.m334_information_expired);
                tvContent.setText(R.string.m333_re_login);
                btnNext.setText(R.string.m14_login);
                btnNext.setOnClickListener(v -> {
                    applogout();
                });
            }
        }, context.getString(R.string.m127_no),null,context.getString(R.string.m90_ok),null,false);
    }




    public void applogout() {
        setJpushAlias();
        TuyaApiUtils.setIsHomeInit(false);
        TuyaApiUtils.logoutTuya();
        List<WeakReference<Activity>> activityStack = App.getInstance().getActivityList();
        for (WeakReference<Activity> activity : activityStack) {
            if (activity != null && activity.get() != null) {
                Activity activity1 = activity.get();
                if (activity1 instanceof MainActivity) continue;//这里要忽略掉，要不然会闪屏
                activity1.finish();
            }
        }
        activityStack.clear();
        SharedPreferencesUnit.getInstance(context).putBoolean(GlobalConstant.SP_AUTO_LOGIN,false);
        Intent intent=new Intent(context, RegisterLoginActivity.class);
        ActivityUtils.startActivity((Activity) context,intent,ActivityUtils.ANIMATE_BACK,true);
    }


    public  void setJpushAlias() {
        //删除别名和Tag
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = TagAliasOperatorHelper.ACTION_DELETE;
        tagAliasBean.isAliasAction = true;
        tagAliasBean.alias = App.getUserBean().getAccountName();
        sequence++;
        TagAliasOperatorHelper.getInstance().handleAction(context, sequence, tagAliasBean);

     /*   tagAliasBean.action = TagAliasOperatorHelper.ACTION_CLEAN;
        tagAliasBean.isAliasAction = false;
        sequence++;
        TagAliasOperatorHelper.getInstance().handleAction(context.getApplicationContext(), sequence, tagAliasBean);*/
    }


}
