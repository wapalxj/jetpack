package com.vero.navigation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Map;

@Navigator.Name("fixfragment")
public class FixFragmentNavigator extends FragmentNavigator {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private int mContainerId;
    private static final String TAG="FixFragmentNavigator";

    public FixFragmentNavigator(@NonNull Context context, @NonNull FragmentManager manager, int containerId) {
        super(context, manager, containerId);
        mContext=context;
        mFragmentManager=manager;
        mContainerId=containerId;
    }


    @Nullable
    @Override
    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {

        if (mFragmentManager.isStateSaved()) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already"
                    + " saved its state");
            return null;
        }
        String className = destination.getClassName();
        if (className.charAt(0) == '.') {
            className = mContext.getPackageName() + className;
        }

        //rewrite:frag不需要每次都初始化了
//        final Fragment frag = instantiateFragment(mContext, mFragmentManager,
//                className, args);
//        frag.setArguments(args);
        final FragmentTransaction ft = mFragmentManager.beginTransaction();

        int enterAnim = navOptions != null ? navOptions.getEnterAnim() : -1;
        int exitAnim = navOptions != null ? navOptions.getExitAnim() : -1;
        int popEnterAnim = navOptions != null ? navOptions.getPopEnterAnim() : -1;
        int popExitAnim = navOptions != null ? navOptions.getPopExitAnim() : -1;
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = enterAnim != -1 ? enterAnim : 0;
            exitAnim = exitAnim != -1 ? exitAnim : 0;
            popEnterAnim = popEnterAnim != -1 ? popEnterAnim : 0;
            popExitAnim = popExitAnim != -1 ? popExitAnim : 0;
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
        }

//        ft.replace(mContainerId, frag);

        //rewrite:replace更改为show/hide
        Fragment fragment= mFragmentManager.getPrimaryNavigationFragment();
        if (fragment != null) {
            ft.hide(fragment);
        }

        Fragment frag=null;
        String tag=String.valueOf(destination.getId());
        frag=mFragmentManager.findFragmentByTag(tag);
        if (frag!=null) {
            ft.show(frag);
        }else {
            frag=instantiateFragment(mContext,mFragmentManager,className,args);
            frag.setArguments(args);
            ft.add(mContainerId,frag,tag);

        }

        ft.setPrimaryNavigationFragment(frag);

        //rewrite:mBackStack是父类的对象，拿不到，需要反射去拿
        ArrayDeque<Integer> mBackStack=null;
        try {
            Field field=FragmentNavigator.class.getDeclaredField("mBackStack");
            field.setAccessible(true);
            mBackStack= (ArrayDeque<Integer>) field.get(this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }



        final @IdRes int destId = destination.getId();
        final boolean initialNavigation = mBackStack.isEmpty();
        // TODO Build first class singleTop behavior for fragments
        final boolean isSingleTopReplacement = navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast() == destId;

        boolean isAdded;
        if (initialNavigation) {
            isAdded = true;
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack.size() > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                mFragmentManager.popBackStack(
                        generateBackStackName(mBackStack.size(), mBackStack.peekLast()),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.addToBackStack(generateBackStackName(mBackStack.size(), destId));
//                mIsPendingBackStackOperation = true;
                try {
                    //rewrite:mIsPendingBackStackOperation是父类的对象，拿不到，需要反射去拿
                    Field field=FragmentNavigator.class.getDeclaredField("mIsPendingBackStackOperation");
                    field.setAccessible(true);
                    field.set(this,true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            isAdded = false;
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size() + 1, destId));
//            mIsPendingBackStackOperation = true;
            try {
                //rewrite:mIsPendingBackStackOperation是父类的对象，拿不到，需要反射去拿
                Field field=FragmentNavigator.class.getDeclaredField("mIsPendingBackStackOperation");
                field.setAccessible(true);
                field.set(this,true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            isAdded = true;
        }
        if (navigatorExtras instanceof Extras) {
            Extras extras = (Extras) navigatorExtras;
            for (Map.Entry<View, String> sharedElement : extras.getSharedElements().entrySet()) {
                ft.addSharedElement(sharedElement.getKey(), sharedElement.getValue());
            }
        }
        ft.setReorderingAllowed(true);
        ft.commit();
        // The commit succeeded, update our view of the world
        if (isAdded) {
            mBackStack.add(destId);
            return destination;
        } else {
            return null;
        }
    }

    /**
     * rewrite 抄一遍父类的这个方法实现
     */
    private String generateBackStackName(int backStackIndex, int destId) {
        return backStackIndex + "-" + destId;
    }
}
