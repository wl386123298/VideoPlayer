package com.player.tool;

import java.io.IOException;
import java.io.InputStream;

import android.R;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

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
	
}
