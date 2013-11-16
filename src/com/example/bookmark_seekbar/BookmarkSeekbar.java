package com.example.bookmark_seekbar;

import java.util.ArrayList;

import com.example.androidlaboratory.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class BookmarkSeekbar extends SeekBar {

	private ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();
	private ArrayList<Rect> bookmarksRect = new ArrayList<Rect>(); 
	
	private float positionPerSecond = 0;
	private Paint bookmarkPaint;
	private int videoDuration;
	
	private int bookmarkColor;
	private int bookmarkWidth;

	public BookmarkSeekbar(Context context) {
		super(context);
		init();
	}

	public BookmarkSeekbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(
		        attrs,
		        R.styleable.BookmarkSeekbar,
		        0, 0);
		
		bookmarkColor = a.getColor(R.styleable.BookmarkSeekbar_bookmark_color, 0x00);
		bookmarkWidth = a.getInt(R.styleable.BookmarkSeekbar_bookmark_width, 2);
		
		init();
	}

	private void init() {
		bookmarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bookmarkPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		bookmarkPaint.setColor(bookmarkColor);
		bookmarkPaint.setStrokeWidth(bookmarkWidth);
	}

	public void addBookmark(int millisec) {
		bookmarks.add(new Bookmark(millisec));
		bookmarksRect.add(new Rect(0, 0, 0, 0));
		invalidate();
	}
	
	public void setVideoDuration(int duration) {
		this.videoDuration = duration;
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(positionPerSecond == 0 && videoDuration != 0) {
			positionPerSecond = (float)getWidth() / (float)videoDuration;
		}
		
		for(int i = 0; i < bookmarks.size(); i ++) {
			Rect rect = bookmarksRect.get(i);
			if(rect.left == 0 && rect.right == 0 && positionPerSecond != 0) {
				rect.bottom = getHeight();
				rect.left = (int)(positionPerSecond * bookmarks.get(i).getTime_milsec());
				rect.right = rect.left + 10;
			}
			canvas.drawRect(rect, bookmarkPaint);
		}
	}
	
	private class Bookmark {
		private int time_milsec;
		
		public Bookmark(int time_milsec) {
			this.time_milsec = time_milsec;
		}

		public int getTime_milsec() {
			return time_milsec;
		}
	}
}
