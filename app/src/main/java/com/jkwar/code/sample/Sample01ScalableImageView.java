package com.jkwar.code.sample;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.widget.OverScroller;
import com.jkwar.code.Utils;

/**
 * @author paihaozhan
 * 缩放ImageView
 */
public class Sample01ScalableImageView extends android.support.v7.widget.AppCompatImageView {
  private static final float IMAGE_WIDTH = Utils.dpToPixel(300);
  private static final float OVER_SCALE_FACTOR = 1.5f;
  private Bitmap bitmap;
  private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  //偏移位置
  private float offsetX;
  private float offsetY;
  //最初的大小
  private float originalOffsetX;
  private float originalOffsetY;
  //最小的缩放
  private float smallScale;
  //最大的缩放
  private float bigScale;
  //是否已经放大
  private boolean isBig;
  //当前放大倍数
  private float currentScale;

  public float getCurrentScale() {
    return currentScale;
  }

  public void setCurrentScale(float currentScale) {
    this.currentScale = currentScale;
    invalidate();
  }

  //缩放进度
  //private float scaleFraction;
  //public float getScaleFraction() {
  //  return scaleFraction;
  //}
  //
  //public void setScaleFraction(float scaleFraction) {
  //  this.scaleFraction = scaleFraction;
  //  invalidate();
  //}
  //手势监听
  private GestureDetectorCompat mGestureDetector;
  //缩放动画
  private ObjectAnimator scaleAnimator;
  //滑动
  private OverScroller scroller;
  //线程
  private flingRunner mFlingRunner = new flingRunner();
  //手势缩放监听
  private ScaleGestureDetector mScaleGestureDetector;

  public Sample01ScalableImageView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    bitmap = Utils.getAvatar(getResources(), (int) IMAGE_WIDTH);
    //手势监听事件
    mGestureDetector = new GestureDetectorCompat(context, new GestureListener());
    //双指缩放监听事件
    mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureListener());
    scroller = new OverScroller(context);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    originalOffsetX = (getWidth() - bitmap.getWidth()) / 2;
    originalOffsetY = (getHeight() - bitmap.getHeight()) / 2;
    //图片缩放，如果图片宽比高大就把宽适应整个视图的宽,否则就把高适应整个视图的高
    if ((float) bitmap.getWidth() / bitmap.getHeight() > (float) getWidth() / getHeight()) {
      smallScale = (float) getWidth() / bitmap.getWidth();
      bigScale = (float) getHeight() / bitmap.getHeight() * OVER_SCALE_FACTOR;
    } else {
      smallScale = (float) getHeight() / bitmap.getHeight();
      bigScale = (float) getWidth() / bitmap.getWidth() * OVER_SCALE_FACTOR;
    }
    currentScale = smallScale;
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    //判断如果不是双指缩放时，就采用 GestrueDetector 监听
    boolean result = mScaleGestureDetector.onTouchEvent(event);
    if (!mScaleGestureDetector.isInProgress()) {
      result = mGestureDetector.onTouchEvent(event);
    }
    return result;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    //float currentScale = smallScale + (bigScale - smallScale) * scaleFraction;
    //缩放进度【（当前缩放大小-最小缩放大小）/ (最大缩放大小-最小缩放大小)】
    float scaleFraction = (currentScale - smallScale) / (bigScale - smallScale);
    //偏移位置
    canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction);
    //缩放
    canvas.scale(currentScale, currentScale, getWidth() / 2f, getHeight() / 2f);
    //绘制图片
    canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, mPaint);
  }

  //手势监听事件
  private class GestureListener extends GestureDetector.SimpleOnGestureListener {
    //每次 ACTION_DOWN 事件出现的时候都会被调⽤，在这⾥返回 true 可以保证必然消费掉事件
    @Override public boolean onDown(MotionEvent e) {
      return true;
    }

    // ⽤户单击时被调⽤(⻓按后松⼿不会调⽤、双击的第⼆下时不会被调⽤)
    @Override public boolean onSingleTapUp(MotionEvent e) {
      return false;
    }

    // ⽤户单击时被调⽤
    // 和 onSingleTapUp() 的区别在于，⽤户的⼀次点击不会⽴即调⽤这个⽅法，
    // ⽽是在⼀定时间后（300ms），确认⽤户没有进⾏双击，这个⽅法才会被调⽤
    @Override public boolean onSingleTapConfirmed(MotionEvent e) {
      return false;
    }

    // ⽤户按下 100ms 不松⼿后会被调⽤，⽤于标记「可以显示按下状态了」
    @Override public void onShowPress(MotionEvent e) {
      super.onShowPress(e);
    }

    // ⽤户⻓按（按下 500ms 不松⼿）后会被调⽤
    // 这个 500ms 在 GestureDetectorCompat 中变成了 600ms (有问题)
    @Override public void onLongPress(MotionEvent e) {
      super.onLongPress(e);
    }

    // ⽤户双击时被调⽤
    // 注意：第⼆次触摸到屏幕时就调⽤，⽽不是抬起时
    @Override public boolean onDoubleTap(MotionEvent e) {
      isBig = !isBig;
      if (isBig) {
        offsetX =
            (e.getX() - getWidth() / 2f) - (e.getX() - getWidth() / 2) * bigScale / smallScale;
        offsetY =
            (e.getY() - getHeight() / 2f) - (e.getY() - getHeight() / 2) * bigScale / smallScale;
        fixOffsets();
        getScaleAnimator().start();
      } else {
        getScaleAnimator().reverse();
      }
      return false;
    }

    //⽤户双击第⼆次按下时、第⼆次按下后移动时、第⼆次按下后抬起时都会被调⽤
    //常⽤于「双击拖拽」的场景
    @Override public boolean onDoubleTapEvent(MotionEvent e) {
      return false;
    }

    // ⽤户滑动时被调⽤
    // 第⼀个事件是⽤户按下时的 ACTION_DOWN 事件，第⼆个事件是当前事件
    // 偏移是按下时的位置 - 当前事件的位置
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
      if (isBig) {
        offsetX += -distanceX;
        offsetY += -distanceY;
        fixOffsets();
        invalidate();
      }
      return false;
    }

    // ⽤于滑动时迅速抬起时被调⽤，⽤于⽤户希望控件进⾏惯性滑动的场景
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
      if (isBig) {
        scroller.fling((int) offsetX, (int) offsetY, (int) velocityX, (int) velocityY,
            -(int) (bitmap.getWidth() * bigScale - getWidth()) / 2,
            (int) (bitmap.getWidth() * bigScale - getWidth()) / 2,
            -(int) (bitmap.getHeight() * bigScale - getHeight()) / 2,
            (int) (bitmap.getHeight() * bigScale - getHeight()) / 2
        );
        postOnAnimation(mFlingRunner);
      }
      return false;
    }
  }

  //双指缩放事件
  private class ScaleGestureListener extends SimpleOnScaleGestureListener {
    float initialScale;

    //缩放开始
    @Override public boolean onScaleBegin(ScaleGestureDetector detector) {
      initialScale = currentScale;
      return true;
    }

    //正在缩放
    @Override public boolean onScale(ScaleGestureDetector detector) {
      currentScale = initialScale * detector.getScaleFactor();
      invalidate();
      return false;
    }
  }

  //死循环刷新界面
  class flingRunner implements Runnable {
    @Override
    public void run() {
      if (scroller.computeScrollOffset()) {
        offsetX = scroller.getCurrX();
        offsetY = scroller.getCurrY();
        invalidate();
        postOnAnimation(this);
      }
    }
  }

  /**
   * 越界判断
   */
  private void fixOffsets() {
    offsetX = Math.max(offsetX, -(bitmap.getWidth() * bigScale - getWidth()) / 2);
    offsetX = Math.min(offsetX, (bitmap.getWidth() * bigScale - getWidth()) / 2);
    offsetY = Math.max(offsetY, -(bitmap.getHeight() * bigScale - getHeight()) / 2);
    offsetY = Math.min(offsetY, (bitmap.getHeight() * bigScale - getHeight()) / 2);
  }

  private ObjectAnimator getScaleAnimator() {
    if (scaleAnimator == null) {
      scaleAnimator = ObjectAnimator.ofFloat(this, "currentScale", smallScale, bigScale);
    }
    return scaleAnimator;
  }
}
