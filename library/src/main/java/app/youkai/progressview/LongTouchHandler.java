package app.youkai.progressview;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.Timer;
import java.util.TimerTask;

class LongTouchHandler implements View.OnTouchListener {

    private IncrementListener incrementListener;
    private int incrementBy;
    private Timer timer;

    LongTouchHandler (IncrementListener incrementListener, int incrementBy) {
        this.incrementListener = incrementListener;
        this.incrementBy = incrementBy;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.isClickable()) {
            // handle press states
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setPressed(true);
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        incrementListener.incrementBy(incrementBy);
                    }
                }, ViewConfiguration.getLongPressTimeout(), ViewConfiguration.getLongPressTimeout());
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                timer.cancel();
                v.setPressed(false);
            }
            long lengthOfPress = event.getEventTime() - event.getDownTime();
            // If the button has been "tapped" then handle normally
            if (lengthOfPress < ViewConfiguration.getLongPressTimeout()
                && event.getAction() == MotionEvent.ACTION_UP) {
                    incrementListener.increment();
            }
            return true;
        } else {
            /* If the view isn't clickable, let the touch be handled by others. */
            return false;
        }
    }

    void cancelPress() {
        try {
            timer.cancel();
        } catch (Exception e) {
            // Swallow.
        }
    }

}
