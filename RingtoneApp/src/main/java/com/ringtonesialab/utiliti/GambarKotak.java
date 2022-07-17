package com.ringtonesialab.utiliti;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class GambarKotak extends ImageView{
    public GambarKotak(Context context) {
        super(context);
    }

    public GambarKotak(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GambarKotak(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getMeasuredHeight();
        setMeasuredDimension(height, height);
    }
}
