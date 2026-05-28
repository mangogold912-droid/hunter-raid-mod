package com.android.support;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/* loaded from: /tmp/dex_extract/classes6.dex */
public class Main {
    private static native void CheckOverlayPermission(Context context);

    static {
        System.loadLibrary("DeathLantern");
    }

    public static void StartWithoutPermission(Context context) {
        CrashHandler.init(context, true);
        if (!(context instanceof Activity)) {
            Toast.makeText(context, "Failed to launch the mod menu\n", 1).show();
            return;
        }
        Menu menu = new Menu(context);
        menu.SetWindowManagerActivity();
        menu.ShowMenu();
    }

    public static void Start(Context context) {
        CrashHandler.init(context, false);
        CheckOverlayPermission(context);
    }
}
