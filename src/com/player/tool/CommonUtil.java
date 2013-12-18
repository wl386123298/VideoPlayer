package com.player.tool;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.player.adapter.PlayHistoryAdapter;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;

public class CommonUtil {
	private Context context;
	public CommonUtil(Context context) {
		this.context = context;
	}
	
	/**
	 * 获取当前亮度
	 * @return
	 */
	public int getBrightness(){
		int brightness = -1;
		try{
			brightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
		}catch(SettingNotFoundException ex){
			ex.printStackTrace();
		}
		
		return brightness;
	}
	
	/**
	 * 获取当前音量
	 * @return
	 */
	public int getVolume(){
		int volume = -1;
		AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		
		return volume;
	}
	
	/**
	 * 获得最大音量
	 * @return
	 */
	public int getMaxVolume() {
		int maxVolume = -1;
		
		AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		return maxVolume;
	}
	
	/**
	 * 读取raw文件夹下的Json文件
	 * @param jsonId Json的名称
	 * @return byte[]
	 */
	public byte[] readJson(int jsonId) {
		InputStream is = context.getResources().openRawResource(jsonId);
		byte[] buffer = null;
		try {
			buffer = new byte[is.available()];
			is.read(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
		return buffer;
	}
	
	

	/**
	 * 通过反射机制获取泛型中的T的类型
	 * @param objName 要判断的对象名称
	 * @return 
	 */
	public static String getGenericType(String objName){
		Class<PlayHistoryAdapter> class1 = PlayHistoryAdapter.class;
		Field mapField = null;
		try {
			mapField = class1.getDeclaredField(objName);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return "";
		}
		
		Type mapMainType = mapField.getGenericType();
		
		if (mapMainType instanceof ParameterizedType) {
			 // 执行强制类型转换   
            ParameterizedType parameterizedType = (ParameterizedType)mapMainType;   
            // 获取基本类型信息，即Map   
            Type basicType = parameterizedType.getRawType();   
            System.out.println("基本类型为："+basicType);   
            // 获取泛型类型的泛型参数   
            Type[] types = parameterizedType.getActualTypeArguments();   
            for (int i = 0; i < types.length;i++) {   
                System.out.println("第"+(i+1)+"个泛型类型是："+types[i].toString());
                return types[i].toString();
            }   
        } 
        
		return "";
		
	}
	
	/**
	 * 获得屏幕的分辨率的宽
	 * @param activity
	 * @return 屏幕的宽度，单位像素
	 */
	public int getScreenPixels(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		
		return width;
		
	}
	
}
