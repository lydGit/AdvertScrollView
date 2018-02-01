package lyd.github.advert;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shawn on 18/1/25.
 */

public class AdvertScrollView extends RecyclerView implements LifecycleObserver {

    /**
     * 计时器
     */
    private CountDownTimer timer;

    private LinearLayoutManager layoutManager;

    private OnItemClickListener onItemClickListener;

    /**
     * 是否执行onResume
     */
    private boolean isResume = true;

    /**
     * 控件属性
     * 列表排列方向 水平 垂直
     */
    private int vOrientation;
    /**
     * 控件属性
     * 反向布局(滑动方向切换)
     */
    private boolean vReverseLayout;
    /**
     * 控件属性
     * 切换动画所用的时间(毫秒)
     */
    private long vAnimationVelocity;
    /**
     * 控件属性
     * 切换item的间隔时间(毫秒)
     */
    private long vSwitchVelocity;

    private RecylerMoveUtils moveUtils;

    public AdvertScrollView(Context context) {
        this(context, null);
    }

    public AdvertScrollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdvertScrollView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs, defStyle);
        AdvertScrollLayoutManager layoutManager = new AdvertScrollLayoutManager(getContext(), vOrientation, vReverseLayout);
        layoutManager.setSmoothScrollVelocity(vAnimationVelocity);
        this.setLayoutManager(layoutManager);
        moveUtils = new RecylerMoveUtils(this,layoutManager);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        isResume = true;
        if (timer != null) {
            timer.start();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //判断触摸位置是否超出控件范围
                if (!isBeyondScope(ev, this)) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getFindFirstVisibleItemPosition());
                    }
                }
                break;
            default:
        }
        return true;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        post(new Runnable() {
            @Override
            public void run() {
                if (getChildCount() > 0) {
                    initView();
                }
            }
        });
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        //获取最新的LayoutManager
        layoutManager = (LinearLayoutManager) getLayoutManager();
    }

    /**
     * 初始控件属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void initAttrs(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AdvertScrollView, defStyle, 0);
        vOrientation = typedArray.getInt(R.styleable.AdvertScrollView_orientation, LinearLayoutManager.VERTICAL);
        vReverseLayout = typedArray.getBoolean(R.styleable.AdvertScrollView_reverseLayout, false);
        vAnimationVelocity = typedArray.getInt(R.styleable.AdvertScrollView_animationVelocity, vOrientation == LinearLayoutManager.VERTICAL ? 1000 : 300);
        vSwitchVelocity = typedArray.getInt(R.styleable.AdvertScrollView_switchVelocity, 3000);
        typedArray.recycle();
    }

    private void initView() {
        onMeasureViewSize();
        onScrollChange();
        timer = new CountDownTimer(Integer.MAX_VALUE, vSwitchVelocity) {
            @Override
            public void onTick(long l) {
                if (isResume) {
                    smoothScrollToPosition(getFindFirstVisibleItemPosition());
                    isResume = false;
                } else {
                    smoothScrollToPosition(getFindFirstVisibleItemPosition() + 1);
                 }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    /**
     * 测量RecyleView的Item大小,调整RecyleView大小
     */
    private void onMeasureViewSize() {
        final View view = getChildAt(0);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = view.getWidth();
        params.height = view.getHeight();
        this.setLayoutParams(params);
    }

    /**
     * 设置列表滚动方式，让每次滚动刚好满一行
     */
    private void onScrollChange() {
        new PagerSnapHelper().attachToRecyclerView(this);
    }

    /**
     * 判断手指离开屏幕时，是否超出控件的范围
     *
     * @param event
     * @param view
     * @return true 超出
     */
    private boolean isBeyondScope(MotionEvent event, View view) {
        float touchX = event.getX();
        float touchY = event.getY();
        float viewX = view.getWidth();
        float viewY = view.getHeight();
        return touchX < 0 || touchX > viewX || touchY < 0 || touchY > viewY;
    }

    /**
     * 获取列表头一条item，即当前显示的Item
     *
     * @return
     */
    private int getFindFirstVisibleItemPosition() {
        if (layoutManager == null) {
            layoutManager = (LinearLayoutManager) getLayoutManager();
        }
        return layoutManager.findFirstVisibleItemPosition();
    }

    /**
     * 设置切换item的间隔时间
     * 在setAdapter方法前使用
     *
     * @param vSwitchVelocity
     */
    public void setvSwitchVelocity(long vSwitchVelocity) {
        this.vSwitchVelocity = vSwitchVelocity;
    }

    /**
     * 设置点击方法
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 列表点击时间
     */
    private interface OnItemClickListener {
        void onItemClick(int position);
    }
}

