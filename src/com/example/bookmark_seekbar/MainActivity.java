package com.example.bookmark_seekbar;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

import com.example.androidlaboratory.R;

public class MainActivity extends Activity implements 
OnSeekBarChangeListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, SurfaceHolder.Callback {

	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private MediaPlayer mp;
	private BookmarkSeekbar seekbar;
	private Handler handler = new Handler();
	private Utilities utils = new Utilities();

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_seekbar);

		seekbar = (BookmarkSeekbar)findViewById(R.id.timeline);
		seekbar.setOnSeekBarChangeListener(this);

		mPreview = (SurfaceView) findViewById(R.id.surface);
		holder = mPreview.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// add bookmarks
		seekbar.addBookmark(50 * 1000);
		seekbar.addBookmark(120 * 1000);
		seekbar.addBookmark(170 * 1000);

		// event handlers
		((Button)findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				seekbar.addBookmark(mp.getCurrentPosition());
			}
		});
		
		((ToggleButton)findViewById(R.id.toggleButton1)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					mp.start();
				}
				else {
					mp.pause();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		synchronized (mp) {
			handler.removeCallbacks(mUpdateTimeTask);
			mp.release();
		}
	}

	private void setMeidaPlayer() {
		Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.leesa);
		mp = new MediaPlayer();
		try {
			mp.setDataSource(getApplicationContext(), uri);
			mp.setDisplay(holder);
			mp.setOnPreparedListener(this);
			mp.setOnSeekCompleteListener(this);
			mp.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateProgressBar() {
		handler.postDelayed(mUpdateTimeTask, 100);
	}   

	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			try {
				synchronized (mp) {
					long totalDuration = mp.getDuration();
					long currentDuration = mp.getCurrentPosition();

					// Updating progress bar
					int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
					seekbar.setProgress(progress);

					// Running this thread after 100 milliseconds
					handler.postDelayed(this, 100);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		setMeidaPlayer();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if(fromUser) {
			mp.seekTo(utils.progressToTimer(progress, mp.getDuration()));
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
		updateProgressBar();
		seekbar.setVideoDuration(mp.getDuration());
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
	}
}
