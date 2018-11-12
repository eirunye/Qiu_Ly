package com.hxnidc.qiu_ly.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by on 2017/7/3 15:40
 * Author：yrg
 * Describe:
 */

public class PathAnimationTabLayout extends LinearLayout {

    public interface OnAnimatorListener {
        void onAnimatorStart(ViewGroup parent, View view, int position);
        void onAnimatorEnd(ViewGroup parent, View view, int position);
    }

    private PathIndicator currentIndicator;
    private OnAnimatorListener onAnimatorListener;
    private int currentPosition;
    private int defaultPosition;
    private SparseArray<PathIndicator> mIndicators;

    /************ for path *************/
    private static final int ANIMATOR_DURATION = 250;
    private static final int PATH_COLOR = 0xffff0000;
    private static final int STROKE_WIDTH = 4;
    private static final int MARGIN_RADIUS = 6;
    private Paint mPathPaint;
    private ValueAnimator valueAnimator;

    public PathAnimationTabLayout(Context context) {
        this(context, null);
    }

    public PathAnimationTabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathAnimationTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public PathAnimationTabLayout(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setColor(PATH_COLOR);
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStrokeWidth(STROKE_WIDTH);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(ANIMATOR_DURATION);
        valueAnimator.addUpdateListener(new TheAnimatorUpdateListener());
    }

    public void setOnAnimatorListener(OnAnimatorListener onAnimatorListener) {
        this.onAnimatorListener = onAnimatorListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        OnClickListener onClickListener = new OnChildClickListener();
        for (int i = 0, len = getChildCount(); i < len; i++) {
            View view = getChildAt(i);
            view.setOnClickListener(onClickListener);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mIndicators = null;

    }




    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (currentIndicator == null) {
            currentIndicator = getPathIndicator(0, 1);
            currentIndicator.build(0);
        }
        currentIndicator.draw(canvas);
    }

    private void performAnimator() {
        if (valueAnimator != null)
            valueAnimator.start();
    }

    private PathIndicator getPathIndicator(int startChild, int endChild) {
        if (startChild == endChild)
            throw new IllegalArgumentException("startChild and endChild must be different.");
        if (mIndicators == null) {
            mIndicators = new SparseArray<>();
        }
        int key = startChild * 31 + endChild;
        PathIndicator pathIndicator = mIndicators.get(key);
        if (pathIndicator == null) {
            IndicatorInfo indicatorInfo = getIndicatorByChildView(getChildAt(startChild), getChildAt(endChild));
            pathIndicator = new PathIndicator(indicatorInfo, mPathPaint);
            mIndicators.put(key, pathIndicator);
        }
        return pathIndicator;
    }

    private IndicatorInfo getIndicatorByChildView(View startChild, View endChild) {
        int startCenterX = (startChild.getLeft() + startChild.getRight()) / 2;
        int startCenterY = (startChild.getTop() + startChild.getBottom()) / 2;
        int endCenterX = (endChild.getLeft() + endChild.getRight()) / 2;
        int endCenterY = (endChild.getTop() + endChild.getBottom()) / 2;
        int radius = startChild.getWidth() < startChild.getHeight()
                ? startChild.getWidth() / 2 + MARGIN_RADIUS
                : startChild.getHeight() / 2 + MARGIN_RADIUS;
        return new IndicatorInfo(startCenterX, startCenterY, endCenterX, endCenterY,  radius);
    }


    private class OnChildClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {

            int lastPosition = currentPosition;
            currentPosition = indexOfChild(v);
            if (lastPosition == currentPosition)
                return;
            if (onAnimatorListener != null) {
                onAnimatorListener.onAnimatorStart(PathAnimationTabLayout.this, getChildAt(lastPosition), lastPosition);
            }
            currentIndicator = getPathIndicator(lastPosition, currentPosition);
            performAnimator();
            if (onAnimatorListener != null) {
                onAnimatorListener.onAnimatorEnd(PathAnimationTabLayout.this, v, currentPosition);
            }
        }
    }

    private class TheAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            final float offset = (float) animation.getAnimatedValue();
            currentIndicator.build(offset);
            invalidate();
        }
    }

    private static class IndicatorInfo {
        int startCenterX;
        int startCenterY;
        int endCenterX;
        int endCenterY;
        int radius;

        IndicatorInfo(int startCenterX,
                      int startCenterY,
                      int endCenterX,
                      int endCenterY,
                      int radius) {
            this.startCenterX = startCenterX;
            this.startCenterY = startCenterY;
            this.endCenterX = endCenterX;
            this.endCenterY = endCenterY;
            this.radius = radius;
        }

        boolean leftToRight(){
            return startCenterX < endCenterX;
        }

        float circumference() {
            return 3.14f * 2 * radius;
        }
        @Override
        public String toString() {
            return "IndicatorInfo{" + "startCenterX=" + startCenterX + ", startCenterY=" +
                    startCenterY + ", endCenterX=" + endCenterX + ", endCenterY=" + endCenterY + ", " +
                    "radius=" + radius + '}';
        }
    }

    private static class PathIndicator {

        private float animationLength;
        private final PathMeasure pathMeasure;
        private float circumference;
        private Path mDrawPath;
        private Paint paint;


        PathIndicator(IndicatorInfo indicatorInfo, Paint paint) {
            this.paint = paint;
            circumference = indicatorInfo.circumference();
            pathMeasure = new PathMeasure(createPathByDirection(indicatorInfo), false);
            animationLength = pathMeasure.getLength() - circumference;
        }

        void build(float offset) {
            mDrawPath = new Path();
            float offsetLength = offset * animationLength;
            pathMeasure.getSegment(offsetLength, circumference + offsetLength, mDrawPath, true);
        }

        void draw(Canvas canvas) {
            canvas.drawPath(mDrawPath, paint);
        }

        private Path createPathByDirection(IndicatorInfo indicatorInfo) {
            Path path = new Path();
            path.addArc(new RectF(
                            indicatorInfo.startCenterX - indicatorInfo.radius,
                            indicatorInfo.startCenterY - indicatorInfo.radius,
                            indicatorInfo.startCenterX + indicatorInfo.radius,
                            indicatorInfo.startCenterY + indicatorInfo.radius),
                    90,
                    indicatorInfo.leftToRight() ? -359 : 359);
            path.rLineTo(indicatorInfo.endCenterX - indicatorInfo.startCenterX, 0);
            path.arcTo(new RectF(
                            indicatorInfo.endCenterX - indicatorInfo.radius,
                            indicatorInfo.endCenterY - indicatorInfo.radius,
                            indicatorInfo.endCenterX + indicatorInfo.radius,
                            indicatorInfo.endCenterY + indicatorInfo.radius),
                    90,
                    indicatorInfo.leftToRight() ? -359 : 359);
            return path;
        }


    }

}
