package com.buildhawk;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class SwipeDetector implements View.OnTouchListener {
	Button delete;

	public static enum Action {
		LR, // Left to Right
		RL, // Right to Left
		TB, // Top to bottom
		BT, // Bottom to Top
		None
		// when no action was detected
	}

	private static final String logTag = "SwipeDetector";
	private static final int MIN_DISTANCE = 40;
	private float downX, downY, upX, upY;
	private Action mSwipeDetected = Action.None;
	float HORIZONTAL_MIN_DISTANCE = 40;
	float VERTICAL_MIN_DISTANCE = 40;

	public boolean swipeDetected() {
		return mSwipeDetected != Action.None;
	}

	public Action getAction() {
		return mSwipeDetected;
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			downX = event.getX();
			downY = event.getY();
			mSwipeDetected = Action.None;
			return false; // allow other events like Click to be processed
		}
		case MotionEvent.ACTION_MOVE: {
			upX = event.getX();
			upY = event.getY();

			float deltaX = downX - upX;
			float deltaY = downY - upY;

			// horizontal swipe detection
			if (Math.abs(deltaX) > HORIZONTAL_MIN_DISTANCE) {
				// left or right
				if (deltaX < 0) {

					mSwipeDetected = Action.LR;
					return true;
				}
				if (deltaX > 0) {

					mSwipeDetected = Action.RL;
					return true;
				}
			} else

			// vertical swipe detection
			if (Math.abs(deltaY) > VERTICAL_MIN_DISTANCE) {
				// top or down
				if (deltaY < 0) {
					Log.i(logTag, "Swipe Top to Bottom");
					mSwipeDetected = Action.TB;
					return false;
				}
				if (deltaY > 0) {
					Log.i(logTag, "Swipe Bottom to Top");
					mSwipeDetected = Action.BT;
					return false;
				}
			}
			return true;
		}
		}
		return false;

	}

}