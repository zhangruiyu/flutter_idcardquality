package com.example.idcardquality;

import com.example.idcardquality.util.Util;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * IdcardqualityPlugin
 */
public class IdcardqualityPlugin implements MethodCallHandler {
    private final Registrar registrar;

    public IdcardqualityPlugin(Registrar registrar) {
        this.registrar = registrar;
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "idcardquality");
        channel.setMethodCallHandler(new IdcardqualityPlugin(registrar));
    }

    @Override
    public void onMethodCall(final MethodCall call, final Result result) {
        if (call.method.equals("openIdcardQuality")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Manager manager = new Manager(registrar.context());
                    IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(
                            registrar.context());
                    manager.registerLicenseManager(idCardLicenseManager);
                    manager.takeLicenseFromNetwork(Util.getUUIDString(registrar.context()));
                    if (idCardLicenseManager.checkCachedLicense() > 0) {
                        //授权成功
                        TransparentActivity.start(registrar.activity(), call, result);
                    } else {
                        //授权失败
                        result.error("1", "2", "3");
                    }
                }
            }).start();

        } else {
            result.notImplemented();
        }
    }

}
