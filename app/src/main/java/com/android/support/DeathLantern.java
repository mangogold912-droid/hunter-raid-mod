package com.android.support;

import android.content.Context;
import android.os.Bundle;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/* loaded from: /tmp/dex_extract/classes6.dex */
public class DeathLantern implements IXposedHookLoadPackage {
    private static String TARGET_PKG = "com.com2usholdings.starsailors.android.google.global.normal";
    private static String HOOK_ACTIVITY = "com.com2us.game.MainActivity";

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam != null && lpparam.packageName.equals(TARGET_PKG)) {
            XposedHelpers.findAndHookMethod(HOOK_ACTIVITY, lpparam.classLoader, "onCreate", new Object[]{Bundle.class, new XC_MethodHook() { // from class: com.android.support.DeathLantern.1
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    if (param == null || param.thisObject == null) {
                        return;
                    }
                    Main.StartWithoutPermission((Context) param.thisObject);
                }
            }});
        }
    }
}
