package kr.co.mplat.www;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by gdfwo on 2016-11-22.
 */

public class NTextView extends TextView implements View.OnTouchListener {
    public NTextView(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public NTextView(Context context, AttributeSet attrs) {

        super(context, attrs);
        setOnTouchListener(this);
    }

    public NTextView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            view.setPressed(true);
        } else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
            view.setPressed(false);
        }
        return true;
    }

}
