package com.example.idcardquality;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.megvii.idcardlib.IDCardScanActivity;

import java.util.Objects;

import androidx.annotation.Nullable;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;


public class TransparentActivity extends Activity {
    static MethodCall call;
    static MethodChannel.Result result;
    private static final int INTO_IDCARDSCAN_PAGE = 100;
    byte[] portraitImgs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, IDCardScanActivity.class);
        intent.putExtra("side", Integer.parseInt(Objects.requireNonNull(call.argument("side")).toString()));
        intent.putExtra("isvertical", false);
        startActivityForResult(intent, INTO_IDCARDSCAN_PAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTO_IDCARDSCAN_PAGE && resultCode == RESULT_OK) {
//            Intent intent = new Intent(this, ResultActivity.class);
//            intent.putExtra("side", data.getIntExtra("side", 0));
//            intent.putExtra("idcardImg", data.getByteArrayExtra("idcardImg"));
//            if (data.getIntExtra("side", 0) == 0) {
//                intent.putExtra("portraitImg", data.getByteArrayExtra("portraitImg"));
//            }
//            startActivity(intent);
//            result.success(String.valueOf(data.getIntExtra("side", 0)));
            portraitImgs = data.getByteArrayExtra("portraitImg");
        } else {
            portraitImgs = null;
        }
        onBackPressed();

    }

    @Override
    protected void onDestroy() {
        if (portraitImgs != null) {
            result.success(portraitImgs);
        } else {
            result.error("没有选择成功", "", "");
        }
        destroyFlutter();
        super.onDestroy();
    }

    void destroyFlutter() {
        TransparentActivity.call = null;
        TransparentActivity.result = null;
    }

    public static void start(Activity activity, MethodCall call, MethodChannel.Result result) {
        TransparentActivity.call = call;
        TransparentActivity.result = result;
        Intent intent = new Intent(activity, TransparentActivity.class);
        activity.startActivity(intent);
    }
}
