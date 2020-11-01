package com.vero.navigation.utils;

import android.content.res.AssetManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.vero.libcommon.AppGlobals;
import com.vero.navigation.model.BottomBar;
import com.vero.navigation.model.Destination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppConfig {

    private static HashMap<String, Destination> sDestConfig;

    public static HashMap<String, Destination> getsDestConfig() {
        if (sDestConfig == null) {
            String content = parseFile("destination.json");
            sDestConfig =JSON.parseObject(content, new TypeReference<HashMap<String, Destination>>() {}.getType());
        }
        return sDestConfig;
    }

    /**
     * 解析destination.json
     * @param fileName
     * @return
     */
    public static String parseFile(String fileName) {
        InputStream stream = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            AssetManager assetManager = AppGlobals.getApplication().getResources().getAssets();
            stream = assetManager.open(fileName);
            reader = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (stream != null) {
                    stream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return builder.toString();
    }



    /**
     * 解析bottombar
     */
    private static BottomBar sBottomBar;

    public static BottomBar getBottomBar(){
        if (sBottomBar == null) {
            String content=parseFile("main_tabs_config.json");
            sBottomBar=JSON.parseObject(content,BottomBar.class);
        }

        return sBottomBar;
    }

}
