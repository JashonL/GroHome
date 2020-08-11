package com.growatt.grohome.module.device.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.growatt.grohome.R;
import com.growatt.grohome.base.BasePresenter;
import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.BulbSceneBean;
import com.growatt.grohome.bean.BulbSceneColourBean;
import com.growatt.grohome.constants.GlobalConstant;
import com.growatt.grohome.module.device.manager.DeviceBulb;
import com.growatt.grohome.module.device.view.IBulbSceneView;
import com.growatt.grohome.tuya.SendDpListener;
import com.growatt.grohome.tuya.TuyaApiUtils;
import com.growatt.grohome.utils.CircleDialogUtils;
import com.growatt.grohome.utils.CommentUtils;
import com.growatt.grohome.utils.MyToastUtils;
import com.mylhyl.circledialog.view.listener.OnLvItemClickListener;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BulbScenePresenter extends BasePresenter<IBulbSceneView> implements SendDpListener {


    private float[] defaultHsv;
    private int defaultColor;//默认颜色

    private float[] whiteHsv;
    private int whiteColor;//默认颜色

    //当前选中颜色实例
    private BulbSceneColourBean mCurrentColourBean;


    //设备Id
    private String deviceId;
    private DeviceBean deviceBean;
    private ITuyaDevice mTuyaDevice;


    //当前场景的序号
    private int id;
    //当前场景的闪烁模式
    private int mCurrentFlashMode;
    //当前闪烁的速率,只有模式不为0时有效
    private int mSpeed;


    public BulbScenePresenter(IBulbSceneView baseView) {
        super(baseView);
    }

    public BulbScenePresenter(Context context, IBulbSceneView baseView) {
        super(context, baseView);
        deviceId = ((Activity) context).getIntent().getStringExtra(GlobalConstant.DEVICE_ID);
        initDevice();
        initColor();
    }

    private void initColor() {
        whiteHsv = new float[]{42.3f, 1, 1};
        whiteColor = Color.HSVToColor(whiteHsv);
    }


    /**
     * 获取到设备操作类
     * 并获取数据，进行初始化
     */
    public void initDevice() {
        //先干掉之前的在重新获取，避免多次回调
        if (mTuyaDevice != null) {
            mTuyaDevice.unRegisterDevListener();
            mTuyaDevice.onDestroy();
        }
        if (!TextUtils.isEmpty(deviceId)) {
            mTuyaDevice = TuyaHomeSdk.newDeviceInstance(deviceId);
        }
        deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(deviceId);
        if (deviceBean == null) {
            MyToastUtils.toast(R.string.m149_device_does_not_exist);
            ((Activity) context).finish();
            return;
        }
        //设备不在线
        if (!deviceNotOnline()) {
            return;
        }
    }


    /**
     * 获取场景解析
     */
    public void getSceneBean() throws Exception {
        String scene = ((Activity) context).getIntent().getStringExtra(GlobalConstant.BULB_SCENE_BEAN);
        BulbSceneBean sceneBean = new Gson().fromJson(scene, BulbSceneBean.class);

        //场景名称
        String name = sceneBean.getName();
        if (!TextUtils.isEmpty(name)) {
            baseView.setSceneName(name);
        }

        //解析场景颜色、模式、速率
        String sceneValue = sceneBean.getSceneValue();
        if (TextUtils.isEmpty(sceneValue)) return;
        int length = sceneValue.length();
        if (length >= 26) {
            //序号
            String number = sceneValue.substring(0, 2);
            id = Integer.parseInt(number);
            setDefault(id);

            String speed = sceneValue.substring(2, 6);
            mSpeed = CommentUtils.hexStringToInter(speed);
            baseView.setSpeed(mSpeed);

            String mode = sceneValue.substring(6, 8);
            mCurrentFlashMode = CommentUtils.hexStringToInter(mode);
            baseView.setMode(mCurrentFlashMode);

            //解析颜色,场景的一个颜色长度为26
            String colours = sceneValue.substring(2);
            int count = colours.length() / 26;

            List<BulbSceneColourBean> colourBeanList = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                float[] hsv = new float[3];
                BulbSceneColourBean bulbSceneColourBean = new BulbSceneColourBean();
                String colorString = colours.substring(26 * i, (i + 1) * 26);
                String hsvValue = colorString.substring(6, 18);
                String lightness = colorString.substring(18);
                if (DeviceBulb.BULB_SCENE_WHITE_DEFAULT_SPACE.equals(hsvValue)) {//白光
                    hsv[0] = 42.3f;
                    hsv[1] = (float) CommentUtils.hexStringToInter(lightness.substring(4)) / 1000f;
                    hsv[2] = (float) CommentUtils.hexStringToInter(lightness.substring(0, 4)) / 1000f;
                    bulbSceneColourBean.setWhiteHsv(hsv);
                    int color = Color.HSVToColor(hsv);
                    bulbSceneColourBean.setWhiteColor(color);
                    bulbSceneColourBean.setIsColour(false);
                    bulbSceneColourBean.setHsv(defaultHsv);//设置一个默认的彩光
                    bulbSceneColourBean.setColour(defaultColor);
                } else {//彩光
                    hsv[0] = (float) CommentUtils.hexStringToInter(hsvValue.substring(0, 4));
                    hsv[1] = (float) CommentUtils.hexStringToInter(hsvValue.substring(4, 8)) / 1000f;
                    hsv[2] = (float) CommentUtils.hexStringToInter(hsvValue.substring(8)) / 1000f;
                    bulbSceneColourBean.setHsv(hsv);
                    int color = Color.HSVToColor(hsv);
                    bulbSceneColourBean.setColour(color);
                    bulbSceneColourBean.setIsColour(true);
                    bulbSceneColourBean.setWhiteHsv(whiteHsv);//设置一个默认的白光
                    bulbSceneColourBean.setWhiteColor(whiteColor);
                }
                bulbSceneColourBean.setSelected(false);
                bulbSceneColourBean.setItemType(GlobalConstant.STATUS_ITEM_DATA);
                colourBeanList.add(bulbSceneColourBean);
            }
            dataDeal(colourBeanList);
            baseView.setColous(colourBeanList);
        }

    }


    public void dataDeal(List<BulbSceneColourBean> colourBeanList) {
        if (mCurrentFlashMode != 0 && colourBeanList.size() < 6) {
            BulbSceneColourBean addBean = new BulbSceneColourBean();
            addBean.setItemType(GlobalConstant.STATUS_ITEM_OTHER);
            colourBeanList.add(addBean);
        }
    }


    public void addData(List<BulbSceneColourBean> colourBeanList) {
        BulbSceneColourBean addBean = new BulbSceneColourBean();
        addBean.setItemType(GlobalConstant.STATUS_ITEM_DATA);
        addBean.setHsv(defaultHsv);
        addBean.setColour(defaultColor);
        addBean.setIsColour(true);
        addBean.setWhiteHsv(whiteHsv);//设置一个默认的白光
        addBean.setWhiteColor(whiteColor);
        addBean.setSelected(false);
        addBean.setItemType(GlobalConstant.STATUS_ITEM_DATA);
        colourBeanList.add(colourBeanList.size() - 1, addBean);
        baseView.addDataDeal();
    }


    /**
     * 根据id设置默认值
     *
     * @param id
     */
    private void setDefault(int id) {
        switch (id) {
            case 0:
                defaultHsv = new float[]{230, 1, 1};
                break;
            case 1:
                defaultHsv = new float[]{30, 1, 1};
                break;
            case 2:
                defaultHsv = new float[]{60, 1, 1};
                break;
            case 3:
                defaultHsv = new float[]{90, 1, 1};
                break;
            case 4:
                defaultHsv = new float[]{110, 1, 1};
                break;
            case 5:
                defaultHsv = new float[]{140, 1, 1};
                break;
            case 6:
                defaultHsv = new float[]{170, 1, 1};
                break;
            case 7:
                defaultHsv = new float[]{200, 1, 1};
                break;
        }
        defaultColor = Color.HSVToColor(defaultHsv);
    }


    public BulbSceneColourBean getCurrentColourBean() {
        return mCurrentColourBean;
    }

    public void setCurrentColourBean(BulbSceneColourBean mCurrentColourBean) {
        this.mCurrentColourBean = mCurrentColourBean;
    }

    public int getCurrentFlashMode() {
        return mCurrentFlashMode;
    }

    public void setCurrentFlashMode(int mCurrentFlashMode) {
        this.mCurrentFlashMode = mCurrentFlashMode;
        baseView.setMode(mCurrentFlashMode);
    }


    /**
     * 白光设置冷暖值
     *
     * @param temper
     */

    public void bulbTemper(int temper) {
        float[] hsv = mCurrentColourBean.getWhiteHsv();
        hsv[1] = (float) (temper) / 100f;
        int newColor = Color.HSVToColor(new float[]{hsv[0], 1f - hsv[1], hsv[2]});
        mCurrentColourBean.setWhiteColor(newColor);
        mCurrentColourBean.setWhiteHsv(hsv);
        baseView.updataSelected();
        String whiteScene = CommentUtils.integerToHexstring(id, 2) + DeviceBulb.BULB_SCENE_WHITE_DEFAULT_SPEED +
                DeviceBulb.BULB_SCENE_WHITE_STATIC + DeviceBulb.BULB_SCENE_WHITE_DEFAULT_SPACE +
                CommentUtils.integerToHexstring((int) (hsv[2] * 1000), 4)
                + CommentUtils.integerToHexstring(temper * 10, 4);
        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbSceneData(), whiteScene, mTuyaDevice, this);
        }
    }


    /**
     * 白光设置亮度
     */

    public void bulbLightness(int light) {
        float[] hsv = mCurrentColourBean.getWhiteHsv();
        hsv[2] = (float) light / 100f;
        int newColor = Color.HSVToColor(new float[]{hsv[0], 1f - hsv[1], hsv[2]});
        mCurrentColourBean.setWhiteColor(newColor);
        mCurrentColourBean.setWhiteHsv(hsv);
        baseView.updataSelected();
        String whiteScene = CommentUtils.integerToHexstring(id, 2) + DeviceBulb.BULB_SCENE_WHITE_DEFAULT_SPEED +
                DeviceBulb.BULB_SCENE_WHITE_STATIC + DeviceBulb.BULB_SCENE_WHITE_DEFAULT_SPACE
                + CommentUtils.integerToHexstring(light * 10, 4)
                + CommentUtils.integerToHexstring((int) (hsv[1] * 1000), 4);
        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbSceneData(), whiteScene, mTuyaDevice, this);
        }
    }


    /**
     * 彩光设置冷暖值
     *
     * @param temper
     */

    public void bulbColourTemper(int temper) {
        float[] hsv = mCurrentColourBean.getHsv();
        hsv[1] = (float) (temper) / 100f;
        float mHue = hsv[0];
        float mSat = hsv[1];
        float mVal = hsv[2];
        int newColor = Color.HSVToColor(new float[]{hsv[0], hsv[1], hsv[2]});
        mCurrentColourBean.setColour(newColor);
        mCurrentColourBean.setHsv(hsv);
        Log.i(TuyaApiUtils.TUYA_TAG, "mHue：" + mHue + "mStat" + mSat + "mVal" + mVal);
        String angle = CommentUtils.integerToHexstring((int) mHue, 4);
        String s = CommentUtils.integerToHexstring((int) (mSat * 1000), 4);
        String v = CommentUtils.integerToHexstring((int) (mVal * 1000), 4);
        String colour = angle + s + v;
        baseView.updataSelected();
        Log.i(TuyaApiUtils.TUYA_TAG, colour);
        String whiteScene = CommentUtils.integerToHexstring(id, 2) + DeviceBulb.BULB_SCENE_WHITE_DEFAULT_SPEED +
                DeviceBulb.BULB_SCENE_WHITE_STATIC + colour + DeviceBulb.BULB_SCENE_COLOUR_DEFAULT_SPACE;
        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbSceneData(), whiteScene, mTuyaDevice, this);
        }
    }


    /**
     * 彩光设置亮度
     */

    public void bulbColourLightness(int light) {
        float[] hsv = mCurrentColourBean.getHsv();
        hsv[2] = (float) light / 100f;
        float mHue = hsv[0];
        float mSat = hsv[1];
        float mVal = hsv[2];
        int newColor = Color.HSVToColor(new float[]{hsv[0], hsv[1], hsv[2]});
        mCurrentColourBean.setColour(newColor);
        mCurrentColourBean.setHsv(hsv);
        Log.i(TuyaApiUtils.TUYA_TAG, "mHue：" + mHue + "mStat" + mSat + "mVal" + mVal);
        String angle = CommentUtils.integerToHexstring((int) mHue, 4);
        String s = CommentUtils.integerToHexstring((int) (mSat * 1000), 4);
        String v = CommentUtils.integerToHexstring((int) (mVal * 1000), 4);
        String colour = angle + s + v;
        baseView.updataSelected();
        Log.i(TuyaApiUtils.TUYA_TAG, colour);
        String whiteScene = CommentUtils.integerToHexstring(id, 2) + DeviceBulb.BULB_SCENE_WHITE_DEFAULT_SPEED +
                DeviceBulb.BULB_SCENE_WHITE_STATIC + colour + DeviceBulb.BULB_SCENE_COLOUR_DEFAULT_SPACE;
        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbSceneData(), whiteScene, mTuyaDevice, this);
        }
    }


    /**
     * 设置彩光颜色
     */
    public void bulbColour(int color) {
        float[] hsv = mCurrentColourBean.getHsv();
        float[] newHsv = new float[3];
        Color.colorToHSV(color, newHsv);
        float mHue = newHsv[0];
        float mSat = hsv[1];
        float mVal = hsv[2];
        hsv[0] = mHue;
        int newColor = Color.HSVToColor(new float[]{hsv[0], hsv[1], hsv[2]});
        mCurrentColourBean.setColour(newColor);
        mCurrentColourBean.setHsv(hsv);
        Log.i(TuyaApiUtils.TUYA_TAG, "mHue：" + mHue + "mStat" + mSat + "mVal" + mVal);
        String angle = CommentUtils.integerToHexstring((int) mHue, 4);
        String s = CommentUtils.integerToHexstring((int) (mSat * 1000), 4);
        String v = CommentUtils.integerToHexstring((int) (mVal * 1000), 4);
        String colour = angle + s + v;
        baseView.updataSelected();
        Log.i(TuyaApiUtils.TUYA_TAG, colour);
        String whiteScene = CommentUtils.integerToHexstring(id, 2) + DeviceBulb.BULB_SCENE_WHITE_DEFAULT_SPEED +
                DeviceBulb.BULB_SCENE_WHITE_STATIC + colour + DeviceBulb.BULB_SCENE_COLOUR_DEFAULT_SPACE;
        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbSceneData(), whiteScene, mTuyaDevice, this);
        }
    }


    /**
     * 设置场景
     */
    public void setScene() {
        List<BulbSceneColourBean> data = baseView.getData();
        StringBuilder scenes = new StringBuilder(CommentUtils.integerToHexstring(id, 2));
        if (data != null) {
            String currentSpeed;
            if (mCurrentFlashMode == 0) {//静态
                currentSpeed = DeviceBulb.BULB_SCENE_WHITE_DEFAULT_SPEED;
            } else {//动态
                currentSpeed = CommentUtils.integerToHexstring(mSpeed, 4);
            }
            for (int i = 0; i < data.size(); i++) {
                scenes.append(currentSpeed).append(CommentUtils.integerToHexstring(mCurrentFlashMode, 2));
                BulbSceneColourBean bulbSceneColourBean = data.get(i);
                float[] hsv = bulbSceneColourBean.getHsv();
                float[] whiteHsv = bulbSceneColourBean.getWhiteHsv();
                boolean colour = bulbSceneColourBean.isColour();
                if (colour) {
                    String angle = CommentUtils.integerToHexstring((int) hsv[0],4);
                    String s = CommentUtils.integerToHexstring((int) (hsv[1]*1000),4);
                    String v = CommentUtils.integerToHexstring((int) (hsv[2]*1000),4);
                    scenes.append(angle).append(s).append(v).append(DeviceBulb.BULB_SCENE_COLOUR_DEFAULT_SPACE);
                } else {
                    String s = CommentUtils.integerToHexstring((int) (whiteHsv[1]*1000),4);
                    String v = CommentUtils.integerToHexstring((int) (whiteHsv[2]*1000),4);
                    scenes.append(DeviceBulb.BULB_SCENE_WHITE_DEFAULT_SPACE).append(s).append(v);
                }
            }
        }
        Log.d(TuyaApiUtils.TUYA_TAG,"场景设置"+scenes.toString());
        if (deviceNotOnline()) {
            TuyaApiUtils.sendCommand(DeviceBulb.getBulbSceneData(), scenes.toString(), mTuyaDevice, this);
        }
    }


    /**
     * 设置速率
     */

    public void setFlashSpeed(int progress) {
        int max = DeviceBulb.getSpeedMax();
        int min = DeviceBulb.getSpeedMin();
        mSpeed = (int) ((float) progress / 100 * (max - min) + min);
        Log.d(TuyaApiUtils.TUYA_TAG, "速度" + mSpeed);
    }


    /**
     * 判断是否在线
     *
     * @return
     */
    private boolean deviceNotOnline() {
        if (!deviceBean.getIsOnline()) {
            MyToastUtils.toast(R.string.m150_device_is_offline);
            return false;
        }
        return true;
    }

    public void setSceneMode() {
        String[] modes = new String[]{context.getString(R.string.m163_static), context.getString(R.string.m164_flash), context.getString(R.string.m165_breath), context.getString(R.string.m89_cancel)};
        CircleDialogUtils.showSceneFlashMode((FragmentActivity) context, Arrays.asList(modes), new OnLvItemClickListener() {
            @Override
            public boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        setCurrentFlashMode(0);
                        break;
                    case 1:
                        setCurrentFlashMode(1);
                        break;
                    case 2:
                        setCurrentFlashMode(2);
                        break;
                    case 3:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void sendCommandSucces() {

    }

    @Override
    public void sendCommandError(String code, String error) {

    }
}
