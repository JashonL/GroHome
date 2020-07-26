package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import androidx.fragment.app.FragmentActivity;

import com.growatt.grohome.R;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.view.IBulbSceneView;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.mylhyl.circledialog.view.listener.OnLvItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BulbScenePresenter extends BasePresenter<IBulbSceneView> {



    public BulbScenePresenter(IBulbSceneView baseView) {
        super(baseView);
    }

    public BulbScenePresenter(Context context, IBulbSceneView baseView) {
        super(context, baseView);
        String scene = ((Activity) context).getIntent().getStringExtra(DeviceBulb.BULB_SCENE_DATA);

    }

    public void setSceneMode(){
        String[]modes =new String[]{context.getString(R.string.m163_static),context.getString(R.string.m164_flash),context.getString(R.string.m165_breath),context.getString(R.string.m89_cancel)};
        CircleDialogUtils.showSceneFlashMode((FragmentActivity) context, Arrays.asList(modes), new OnLvItemClickListener() {
            @Override
            public boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
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
