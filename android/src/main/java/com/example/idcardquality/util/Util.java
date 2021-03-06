package com.example.idcardquality.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.Gravity;
import android.widget.Toast;


import com.example.idcardquality.R;
import com.megvii.idcardquality.IDCardQualityResult;
import com.megvii.idcardquality.bean.IDCardAttr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by binghezhouke on 15-8-12.
 */
public class Util {

    public static Toast toast;

    /**
     * 输出toast
     */
    public static void showToast(Context context, String str) {
        if (context != null) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
            // 可以控制toast显示的位�?
            toast.setGravity(Gravity.TOP, 0, 30);
            toast.show();
        }
    }

    /**
     * 取消弹出toast
     */
    public static void cancleToast(Context context) {
        if (context != null) {
            if (toast != null) {
                toast.cancel();
            }
        }
    }

    public static String getUUIDString(Context mContext) {
        String KEY_UUID = "key_uuid";
        SharedUtil sharedUtil = new SharedUtil(mContext);
        String uuid = sharedUtil.getStringValueByKey(KEY_UUID);
        if (uuid != null && uuid.trim().length() != 0)
            return uuid;

        uuid = UUID.randomUUID().toString();
        uuid = Base64.encodeToString(uuid.getBytes(), Base64.DEFAULT);
        sharedUtil.saveStringValue(KEY_UUID, uuid);
        return uuid;
    }


    public static String getPhoneNumber(Context mContext) {
        TelephonyManager phoneMgr = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        return phoneMgr.getLine1Number();
    }

    public static String getDeviceID(Context mContext) {
        TelephonyManager tm = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static String getMacAddress(Context mContext) {
        WifiManager wifi = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String address = info.getMacAddress();
        if (address != null && address.length() > 0) {
            address = address.replace(":", "");
        }
        return address;
    }

    public static Camera.Size getNearestRatioSize(Camera.Parameters para,
                                                  final int screenWidth, final int screenHeight) {
        List<Camera.Size> supportedSize = para.getSupportedPreviewSizes();
        for (Camera.Size tmp : supportedSize) {
            if (tmp.width == 1280 && tmp.height == 720) {
                return tmp;
            }
        }
        Collections.sort(supportedSize, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                int diff1 = (((int) ((1000 * (Math.abs(lhs.width
                        / (float) lhs.height - screenWidth
                        / (float) screenHeight))))) << 16)
                        - lhs.width;
                int diff2 = (((int) (1000 * (Math.abs(rhs.width
                        / (float) rhs.height - screenWidth
                        / (float) screenHeight)))) << 16)
                        - rhs.width;

                return diff1 - diff2;
            }
        });

        return supportedSize.get(0);
    }


    public static String getTimeStr() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        return simpleDateFormat.format(new Date());
    }

    public static void closeStreamSilently(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {

            }
        }
    }

    public static byte[] bmp2byteArr(Bitmap bmp) {
        if (bmp == null || bmp.isRecycled())
            return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        Util.closeStreamSilently(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static String errorType2HumanStr(IDCardQualityResult.IDCardFailedType type, IDCardAttr.IDCardSide side) {
        String result = null;
        switch (type) {
            case QUALITY_FAILED_TYPE_NOIDCARD:
                result = "请将身份证置于提示框内";
                break;
            case QUALITY_FAILED_TYPE_BLUR:
                result = "请点击屏幕对焦";
                break;
            case QUALITY_FAILED_TYPE_BRIGHTNESSTOOHIGH:
                result = "太亮";
                break;
            case QUALITY_FAILED_TYPE_BRIGHTNESSTOOLOW:
                result = "太暗";
                break;
            case QUALITY_FAILED_TYPE_OUTSIDETHEROI:
                result = "请将身份证与提示框对齐";
                break;
            case QUALITY_FAILED_TYPE_SIZETOOLARGE:
                result = "请将身份证与提示框对齐";
                break;
            case QUALITY_FAILED_TYPE_SIZETOOSMALL:
                result = "请将身份证与提示框对齐";
                break;
            case QUALITY_FAILED_TYPE_SPECULARHIGHLIGHT:
                result = "请调整拍摄位置，以去除光斑";
                break;
            case QUALITY_FAILED_TYPE_TILT:
                result = "请将身份证摆正";
                break;
            case QUALITY_FAILED_TYPE_SHADOW:
                result = "请调整拍摄位置，以去除阴影";
                break;
            case QUALITY_FAILED_TYPE_WRONGSIDE:
                if (side == IDCardAttr.IDCardSide.IDCARD_SIDE_BACK)
                    result = "请翻到国徽面";
                else {
                    result = "请翻到人像面";
                }
                break;
        }
        return result;
    }

    public static byte[] readModel(Context context) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count = -1;
        try {
            inputStream = context.getResources().openRawResource(R.raw.idcardmodel);
            while ((count = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, count);
            }
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}
