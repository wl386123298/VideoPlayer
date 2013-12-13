package com.player.main;

import java.io.IOException;

import com.jrummy.apps.dialogs.EasyDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.player.model.TvContentModel;
import com.player.tool.CommonUtil;
import com.player.tool.DBUtil;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.net.Uri;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainPlayerActivity extends FragmentActivity implements 
		OnCompletionListener,OnInfoListener, OnBufferingUpdateListener,OnTouchListener,OnErrorListener{

	/**
	 * TODO: Set the path variable to a streaming video URL or a local media file
	 * path.
	 */
	private String path = "http://live.gslb.letv.com/gslb?stream_id=cctv5_800&tag=live&ext=m3u8&sign=live_tv";
	private Uri uri;
	private VideoView mVideoView;
	private boolean isStart;
	private ProgressBar pb;
	private TextView downloadRateView, loadRateView , endText;
	private ImageButton replay_btn;
	private MediaController mMediaController;
	private float startX ;
	private float startY ;
	private DbUtils db;
	private EasyDialog dialog;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		setContentView(R.layout.player);
		mVideoView = (VideoView) findViewById(R.id.buffer);
		pb = (ProgressBar) findViewById(R.id.probar);
		replay_btn = (ImageButton)findViewById(R.id.replayBtn);
		
		downloadRateView = (TextView) findViewById(R.id.download_rate);
		loadRateView = (TextView) findViewById(R.id.load_rate);
		endText = (TextView)findViewById(R.id.endText);
		
		Intent intent = getIntent();
		path = intent.getStringExtra("tv_url");
		
		if (path == "") {
			// Tell the user to provide a media file URL/path.
			Toast.makeText(MainPlayerActivity.this, "Please edit VideoBuffer Activity, and set path" + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
			return;
		} else {
			/*
			 * Alternatively,for streaming media you can use
			 * mVideoView.setVideoURI(Uri.parse(URLstring));
			 */
			uri = Uri.parse(path);
			mVideoView.setVideoURI(uri);
			mMediaController = new MediaController(this);
			mVideoView.setMediaController(mMediaController);
			mVideoView.requestFocus();
			mVideoView.setOnInfoListener(this);
			mVideoView.setOnBufferingUpdateListener(this);
			mVideoView.setOnCompletionListener(this);
			mVideoView.setOnTouchListener(this);
			mVideoView.setOnErrorListener(this);
			mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					// optional need Vitamio 4.0
					mediaPlayer.setPlaybackSpeed(1.0f);
				}
			});
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				isStart = true;
				pb.setVisibility(View.VISIBLE);
				downloadRateView.setVisibility(View.VISIBLE);
				loadRateView.setVisibility(View.VISIBLE);
				
			}
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			if (isStart) {
				mVideoView.start();
				pb.setVisibility(View.GONE);
				downloadRateView.setVisibility(View.GONE);
				loadRateView.setVisibility(View.GONE);
			}
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			downloadRateView.setText(extra + "kb/s");
			break;
		}
		return true;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		loadRateView.setText("正在加载    " + percent + "%");
	}

	@Override
	public void onCompletion(final MediaPlayer mp) {
		replay_btn.setVisibility(View.VISIBLE);
		endText.setVisibility(View.VISIBLE);
		mVideoView.setBackgroundColor(getResources().getColor(android.R.color.black));
		replay_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mp.reset();
				mVideoView.setVideoURI(Uri.parse(path));
				mVideoView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				try {
					mp.prepare();
					replay_btn.setVisibility(View.GONE);
					endText.setVisibility(View.GONE);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		CommonUtil util  = new CommonUtil(getApplicationContext());
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//记录开始坐标
			startX = event.getX();
			startY = event.getY();
			//Toast.makeText(this, "初始坐标为："+"初始坐标："+startX+"::"+startY, Toast.LENGTH_SHORT).show();
			
			break;
		case MotionEvent.ACTION_UP:
			if (mMediaController.isShowing()) {
			     mMediaController.hide();
			} else {
			     mMediaController.show();
			}
			
			if (startX - event.getX() > 100) {
				//mVideoView.setVolume(leftVolume, rightVolume)
				System.out.println("左滑！");
			}else if (event.getX()-startX > 100) {
				System.out.println("右滑！");
			}else if (startY - event.getY() >100 ) {
				System.out.println(util.getVolume()+":最大音量：" + util.getMaxVolume() +"上滑动的距离： "+ (startY - event.getY()/100));
			}else if (event.getY()- startY > 100 ) {
				//System.out.println("下滑！");
				System.out.println(util.getVolume()+":最大音量：" + util.getMaxVolume() +"上滑动的距离： "+ (event.getY()- startY /100));
			}
			break;
		case MotionEvent.ACTION_POINTER_UP://假如有两个触点，第二个触点离开
			System.out.println("第二个触点离开");
			break;
			
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		dialog = new EasyDialog.Builder(this,EasyDialog.THEME_HOLO_LIGHT)
		.setTitle("提示")
		.setIcon(null)
		.setMessage("亲，视频无法播放!")
		.setCancelable(false)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TvContentModel model = new TvContentModel();
				model.setEnPlay("false");
				//System.out.println("path::"+path);
				db = DbUtils.create(getApplicationContext(), DBUtil.DBNAME);
				db.configDebug(true);
				
				// "tv_url = '"+ path +"'"
				try {
					db.update(model,WhereBuilder.b("tv_url", "=", path),"enPlay");
				} catch (DbException e) {
					e.printStackTrace();
				}
				finish();		
			}
		})
		.show();

		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dialog !=null) {
			dialog.dismiss();
		}

	}

	
}
