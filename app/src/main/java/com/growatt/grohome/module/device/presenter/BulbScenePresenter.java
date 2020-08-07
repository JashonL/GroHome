package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.BulbSceneBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.view.IBulbSceneView;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.mylhyl.circledialog.view.listener.OnLvItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BulbScenePresenter extends BasePresenter<IBulbSceneView> {


    private float[] defaultHsv1;
    private float[] defaultHsv2;
    private int defaultColor1;//默认颜色1
    private int defaultColor2;//默认颜色2
    private int defaultMode;//默认跳变颜色
    private int defaultSpeed;//默认变化速率



    private BulbSceneBean sceneBean;



    public BulbScenePresenter(IBulbSceneView baseView) {
        super(baseView);
    }

    public BulbScenePresenter(Context context, IBulbSceneView baseView) {
        super(context, baseView);
    }

    /**
     * 获取并场景解析
     */
    public void getSceneBean() throws Exception{
        String scene = ((Activity) context).getIntent().getStringExtra(GlobalConstant.BULB_SCENE_BEAN);
        sceneBean = new Gson().fromJson(scene, BulbSceneBean.class);

        //场景名称
        String name = sceneBean.getName();
        if (!TextUtils.isEmpty(name)){
            baseView.setSceneName(name);
        }

        //解析场景颜色、模式、速率
        String sceneValue = sceneBean.getSceneValue();

        if (!TextUtils.isEmpty(sceneValue)) {
            //序号
            String number = sceneValue.substring(0, 2);
            int id = Integer.parseInt(number);
            setDefault(id);
            //速率



            baseView.setViewById(id);
        }



    }


    /**
     * 根据id设置默认值
     * @param id
     */
    private void setDefault(int id){
        switch (id) {
            case 0:
                defaultHsv1=new float[]{42.3f,0,1};
                defaultHsv2=new float[]{15,0,1};
                defaultMode=0;
                defaultSpeed=2828;
                break;
            case 1:
                defaultHsv1=new float[]{30,0,1};
                defaultHsv2=new float[]{45,0,1};
                defaultMode=0;
                defaultSpeed=2828;
                break;
            case 2:
                defaultHsv1=new float[]{60,0,1};
                defaultHsv2=new float[]{75,0,1};
                defaultMode=0;
                defaultSpeed=2828;
                break;
            case 3:
                defaultHsv1=new float[]{90,0,1};
                defaultHsv2=new float[]{105,0,1};
                defaultMode=0;
                defaultSpeed=2828;
                break;
            case 4:
                defaultHsv1=new float[]{110,0,1};
                defaultHsv2=new float[]{125,0,1};
                defaultMode=0;
                defaultSpeed=2828;
                break;
            case 5:
                defaultHsv1=new float[]{140,0,1};
                defaultHsv2=new float[]{155,0,1};
                defaultMode=0;
                defaultSpeed=2828;
                break;
            case 6:
                defaultHsv1=new float[]{170,0,1};
                defaultHsv2=new float[]{185,0,1};
                defaultMode=0;
                defaultSpeed=2828;
                break;
            case 7:
                defaultHsv1=new float[]{200,0,1};
                defaultHsv2=new float[]{215,0,1};
                defaultMode=0;
                defaultSpeed=2828;
                break;
        }
    }



    public void setSceneMode() {
        String[] modes = new String[]{context.getString(R.string.m163_static), context.getString(R.string.m164_flash), context.getString(R.string.m165_breath), context.getString(R.string.m89_cancel)};
        CircleDialogUtils.showSceneFlashMode((FragmentActivity) context, Arrays.asList(modes), new OnLvItemClickListener() {
            @Override
            public boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
                return false;
            }
        });
    }
}
