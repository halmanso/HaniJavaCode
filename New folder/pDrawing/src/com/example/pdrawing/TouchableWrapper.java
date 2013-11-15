package com.example.pdrawing;

import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchableWrapper extends FrameLayout {
	private long lastTouched = 0;
	private static final long SCROLL_TIME = 200L; // 200 Milliseconds, but you can adjust that to your liking
	private UpdateMapAfterUserInterection updateMapAfterUserInterection;

	public TouchableWrapper(Context context) {
		super(context);
		// Force the host activity to implement the UpdateMapAfterUserInterection Interface
		try {
			updateMapAfterUserInterection = (MainActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement UpdateMapAfterUserInterection");
        }
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		  case MotionEvent.ACTION_DOWN:
	    	  lastTouched = SystemClock.uptimeMillis();
	    	  updateMapAfterUserInterection.init(ev);
	  	 	  break;
		  case MotionEvent.ACTION_UP:
			  final long now = SystemClock.uptimeMillis();
			  if (now - lastTouched > SCROLL_TIME) {
				// Update the map
				//updateMapAfterUserInterection.onUpdateMapAfterUserInterection();
				updateMapAfterUserInterection.show();
		 	  }
			  break;
		  case MotionEvent.ACTION_MOVE:
			  updateMapAfterUserInterection.onUpdateMapAfterUserInterection(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	// Map Activity must implement this interface
    public interface UpdateMapAfterUserInterection {
        public void onUpdateMapAfterUserInterection(MotionEvent ev);
        public void init(MotionEvent ev);
        public void show();
    }
}


