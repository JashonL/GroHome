package com.growatt.grohome.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.growatt.grohome.R;
import com.growatt.grohome.bean.SceneTaskBean;
import com.growatt.grohome.bean.ScenesBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.manager.DevicePanel;
import com.growatt.grohome.module.device.manager.DevicePlug;
import com.growatt.grohome.module.device.manager.DeviceStripLights;
import com.growatt.grohome.module.device.manager.DeviceThermostat;
import com.growatt.grohome.module.device.manager.DeviceTypeConstant;

import java.util.List;

public class SceneGridAdapter extends BaseMultiItemQuickAdapter<ScenesBean.DataBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SceneGridAdapter(List<ScenesBean.DataBean> data) {
        super(data);
        addItemType(GlobalConstant.SCENE_TYPE_LUANCH, R.layout.item_launch_tap_to_run);
        addItemType(GlobalConstant.SCENE_TYPE_SMART, R.layout.item_scene_smart_grid);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ScenesBean.DataBean item) {

        helper.setText(R.id.tv_name, item.getName());
        helper.addOnClickListener(R.id.iv_right);
        LinearLayout llTaskGroup = helper.getView(R.id.ll_task);
        llTaskGroup.removeAllViews();
        List<SceneTaskBean> conf = item.getConf();
        if (conf != null && conf.size() > 0){
            int res=0;
            for (int i = 0; i < conf.size(); i++) {
                SceneTaskBean sceneTaskBean = conf.get(i);
                String devType = sceneTaskBean.getDevType();
                switch (devType) {
                    case DeviceTypeConstant.TYPE_BULB:
                        res= DeviceBulb.getCloseIcon(4);
                        break;
                    case DeviceTypeConstant.TYPE_PADDLE:
                        res= DevicePlug.getCloseIcon(2);
                        break;
                    case DeviceTypeConstant.TYPE_PANELSWITCH:
                        res= DevicePanel.getCloseIcon(2);
                        break;
                    case DeviceTypeConstant.TYPE_STRIP_LIGHTS:
                        res= DeviceStripLights.getCloseIcon(4);
                        break;
                    case DeviceTypeConstant.TYPE_THERMOSTAT:
                        res= DeviceThermostat.getCloseIcon(2);
                        break;
                }
                if (i==5){
                    setTextview(mContext,llTaskGroup);
                    break;
                }else {
                    setImageType(mContext, llTaskGroup, res);
                }
            }
        }
    }


    private void setImageType(Context context, LinearLayout llType, int res) {
        if (res==0)return;
        ImageView ivType = new ImageView(context);
        int width = context.getResources().getDimensionPixelSize(R.dimen.dp_24);
        int height = context.getResources().getDimensionPixelSize(R.dimen.dp_24);
        int marginLeft = context.getResources().getDimensionPixelSize(R.dimen.dp_10);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        if (llType.getChildCount() != 0)
            layoutParams.setMargins(marginLeft, 0, 0, 0);
        ivType.setLayoutParams(layoutParams);
        ivType.setImageResource(res);
        ivType.setScaleType(ImageView.ScaleType.FIT_XY);
        llType.addView(ivType);
    }

    private void setTextview(Context context, LinearLayout llType) {
        TextView tvMore = new TextView(context);
        int width = context.getResources().getDimensionPixelSize(R.dimen.dp_24);
        int height = context.getResources().getDimensionPixelSize(R.dimen.dp_24);
        int marginLeft = context.getResources().getDimensionPixelSize(R.dimen.dp_10);
        tvMore.setText("…");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        if (llType.getChildCount() != 0)
            layoutParams.setMargins(marginLeft, 0, 0, 0);
        tvMore.setLayoutParams(layoutParams);
        tvMore.setBackgroundResource(R.drawable.shape_circle_solid_gray);
        tvMore.setTextColor(ContextCompat.getColor(mContext,R.color.white));
        llType.addView(tvMore);
    }
}
