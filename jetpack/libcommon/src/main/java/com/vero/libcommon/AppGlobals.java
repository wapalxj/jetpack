package com.vero.libcommon;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AppGlobals {
    public static Application sApplication;

    /**
     * 通过反射activityThread获取Application
     *
     * @return
     */
    public static Application getApplication() {
        if (sApplication == null) {
            try {
                Method method = Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication");
                sApplication = (Application) method.invoke(null, null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        }
        return sApplication;
    }
}
