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
 * 接力型
 */
public class Sample02RelayTypeView extends View {
  private Bitmap mBitmap;
  private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  //图片宽度
  private static final float IMAGE_WIDTH = Utils.dpToPixel(300);
  //记录第一次按下 x，y
  private float downX, downY;
  //记录偏移的x，y
  private float offsetX, offsetY;
  //记录上一次偏移的x，y
  private float originalOffsetX, originalOffsetY;
  //跟踪指针Id
  private int trackingPointerId;

  public Sample02RelayTypeView(Context context,
      AttributeSet attrs) {
    super(context, attrs);
    mBitmap = Utils.getAvatar(getResources(), (int) IMAGE_WIDTH);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    switch (event.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
        trackingPointerId = event.getPointerId(0);
        downX = event.getX();
        downY = event.getY();
        originalOffsetX = offsetX;
        originalOffsetY = offsetY;
        break;
      case MotionEvent.ACTION_MOVE:
        int index = event.findPointerIndex(trackingPointerId);
        offsetX = originalOffsetX + event.getX(index) - downX;
        offsetY = originalOffsetY + event.getY(index) - downY;
        invalidate();
        break;
      case MotionEvent.ACTION_POINTER_DOWN:
        int actionIndex = event.getActionIndex();
        trackingPointerId = event.getPointerId(actionIndex);
        downX = event.getX(actionIndex);
        downY = event.getY(actionIndex);
        originalOffsetX = offsetX;
        originalOffsetY = offsetY;
        break;
      case MotionEvent.ACTION_POINTER_UP:
        actionIndex = event.getActionIndex();
        int pointerId = event.getPointerId(actionIndex);
        if (trackingPointerId == pointerId) {
          int newIndex;
          if (actionIndex == event.getPointerCount() - 1) {
            newIndex = event.getPointerCount() - 2;
          } else {
            newIndex = event.getPointerCount() - 1;
          }
          trackingPointerId = event.getPointerId(newIndex);
          downX = event.getX(newIndex);
          downY = event.getY(newIndex);
          originalOffsetX = offsetX;
          originalOffsetY = offsetY;
        }
        break;
    }
    return true;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawBitmap(mBitmap, offsetX, offsetY, mPaint);
  }
}
