package com.jkwar.code.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.jkwar.code.Utils;

/**
 * @author paihaozhan
 * 协作型
 */
public class Sample03CollaborativeView extends View {
  private Bitmap mBitmap;
  private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  //图片宽度
  private static final float IMAGE_WIDTH = Utils.dpToPixel(300);
  //记录触摸的位置
  private float downX, downY;
  //记录偏移的x，y
  private float offsetX, offsetY;
  //记录上一次偏移的x，y
  private float originalOffsetX, originalOffsetY;

  public Sample03CollaborativeView(Context context,
      AttributeSet attrs) {
    super(context, attrs);
    mBitmap = Utils.getAvatar(getResources(), (int) IMAGE_WIDTH);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    //求交集点
    //先把 X 轴的点都加起来
    //先把 Y 轴的点都加起来
    float sumX = 0;
    float sumY = 0;
    int pointerCount = event.getPointerCount();
    boolean isPointerUp = event.getActionMasked() == MotionEvent.ACTION_POINTER_UP;
    for (int i = 0; i < pointerCount; i++) {
      //判断有手指离开屏幕该坐标就不添加集合中
      if (!(isPointerUp && i == event.getActionIndex())) {
        sumX += event.getX(i);
        sumY += event.getY(i);
      }
    }
    if (isPointerUp) {
      pointerCount -= 1;
    }
    //记录交集点
    float focusX = sumX / pointerCount;
    float focusY = sumY / pointerCount;
    switch (event.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_POINTER_DOWN:
      case MotionEvent.ACTION_POINTER_UP:
        downX = focusX;
        downY = focusY;
        originalOffsetX = offsetX;
        originalOffsetY = offsetY;
        break;
      case MotionEvent.ACTION_MOVE:
        offsetX = originalOffsetX + focusX - downX;
        offsetY = originalOffsetY + focusY - downY;
        invalidate();
        break;
    }
    return true;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawBitmap(mBitmap, offsetX, offsetY, mPaint);
  }
}
