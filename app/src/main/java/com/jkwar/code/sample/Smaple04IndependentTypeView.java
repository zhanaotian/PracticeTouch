package com.jkwar.code.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import com.jkwar.code.Utils;

/**
 * @author paihaozhan
 * 独立型
 */
public class Smaple04IndependentTypeView extends View {
  private SparseArray<Path> mSparseArray = new SparseArray<>();

  Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

  public Smaple04IndependentTypeView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  {
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(Utils.dpToPixel(4));
    paint.setStrokeCap(Paint.Cap.ROUND);
    paint.setStrokeJoin(Paint.Join.ROUND);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    switch (event.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_POINTER_DOWN:
        //添加到路径集合中
        int actionIndex = event.getActionIndex();
        int pointerId = event.getPointerId(actionIndex);
        Path path = new Path();
        path.moveTo(event.getX(actionIndex), event.getY(actionIndex));
        mSparseArray.append(pointerId, path);
        invalidate();
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_POINTER_UP:
        //移除路径集合
        pointerId = event.getPointerId(event.getActionIndex());
        mSparseArray.remove(pointerId);
        invalidate();
        break;
      case MotionEvent.ACTION_MOVE:
        //遍历路径集合添加位移路径
        for (int i = 0; i < event.getPointerCount(); i++) {
          pointerId = event.getPointerId(i);
          path = mSparseArray.get(pointerId);
          path.lineTo(event.getX(i), event.getY(i));
        }
        invalidate();
        break;
    }
    return true;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    for (int i = 0; i < mSparseArray.size(); i++) {
      canvas.drawPath(mSparseArray.valueAt(i), paint);
    }
  }
}
