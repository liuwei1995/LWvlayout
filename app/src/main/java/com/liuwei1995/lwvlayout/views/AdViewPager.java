package com.liuwei1995.lwvlayout.views;

/**
 * Created by linxins on 17-6-1.
 */

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.liuwei1995.lwvlayout.R;
import com.liuwei1995.lwvlayout.adapter.AdPagerAdapter;


/**
 * 广播轮播图封装的viewpager
 * Created by liuwei on 2017/4/27
 */

public final class AdViewPager extends RelativeLayout {

    public AdViewPager(Context context) {
        this(context,null);
    }

    public AdViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private ViewPager viewPager;

    public AdViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.ad_view_pager, this, true);
        viewPager = (ViewPager) findViewById(R.id.vp_ad);
        viewPager.setFocusable(true);
        viewPager.setOnTouchListener(new adOnTouchListener());

        viewPager.addOnPageChangeListener(new adPageChangeListener());
        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        this.setLayoutParams(layoutParams);
    }

    private long waitingTime = 3000;//等待时间

    public long getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(long waitingTime) {
        if(waitingTime < 1000)waitingTime = 1000;
        this.waitingTime = waitingTime;
    }
    public void onPause() {
        removeSend();
    }

    public void onStop() {
    }

    public void onResume() {
        sendDelayed();
    }
    private int listSize = 1;
    @CallSuper
    public void setAdPagerAdapter(AdPagerAdapter adPagerAdapter){
        viewPager.setAdapter(adPagerAdapter);
        if(adPagerAdapter != null){
            listSize = adPagerAdapter.getListSize();
            /**
             * 2147483647 / 2 = 1073741820 - 1
             * 设置ViewPager的当前项为一个比较大的数，以便一开始就可以左右循环滑动
             */
            int n = Integer.MAX_VALUE / 2 % adPagerAdapter.getListSize();
            int itemPosition = Integer.MAX_VALUE / 2 - n;
            viewPager.setCurrentItem(itemPosition);
            sendDelayed();
        }
    }
    public static final int SEND_DELAYED = 0;
    private synchronized void sendDelayed(){
        handler.removeMessages(SEND_DELAYED);
        handler.sendEmptyMessageDelayed(SEND_DELAYED,waitingTime);
    }
    private synchronized void removeSend(){
        handler.removeMessages(SEND_DELAYED);
    }
    private int add = 1;
    private  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(viewPager.getCurrentItem()+add);
        }
    };

    private int l = 0;

    public synchronized void setCurrentItem(@IntRange(from = 0) int item){
        if(viewPager != null){
            int currentItem = viewPager.getCurrentItem();
            int m = item % listSize - currentItem % listSize ;
            if(m > 0){
                removeSend();
                l = --m;
                viewPager.setCurrentItem(currentItem + 1);
            }else if (m < 0){
                removeSend();
                l = ++m;
                viewPager.setCurrentItem(currentItem - 1);
            }else {
                l = 0 ;
            }
        }
    }
    private static final String TAG = "AdViewPager";


    private class adOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    removeSend();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    sendDelayed();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    sendDelayed();
                    break;
            }
            return false;
        }
    }
    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     *
     */

    private class adPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(onPageChangeListener != null){
                //为保证角标对应  position%listSize
                onPageChangeListener.onPageScrolled(position%listSize,positionOffset,positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if(onPageChangeListener != null){
                //为保证角标对应  position%listSize
                onPageChangeListener.onPageSelected(position%listSize);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case 1:// 手势滑动，空闲中
                    removeSend();
                    Log.e(TAG, "onPageScrollStateChanged: "+state);
                    break;
                case 2:// 界面切换中
                    Log.e(TAG, "onPageScrollStateChanged: "+state);
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    if(l == 0){
                        sendDelayed();
                    }else if (l > 0){
                        --l;
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }else if (l < 0){
                        ++l;
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    }
                    Log.e(TAG, "onPageScrollStateChanged: "+state);
                    break;
            }
            if(onPageChangeListener != null){
                onPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    }
    private OnPageChangeListener onPageChangeListener;

    public OnPageChangeListener getOnPageChangeListener() {
        return onPageChangeListener;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    /**
     * 为了方便实现的时候不重写所有方法就使用  abstract 而不使用  interface
     */
    public static abstract class OnPageChangeListener{

        /**
         * This method will be invoked when the current page is scrolled, either as part
         * of a programmatically initiated smooth scroll or a user initiated touch scroll.
         *
         * @param position Position index of the first page currently being displayed.
         *                 Page position+1 will be visible if positionOffset is nonzero.
         * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
         * @param positionOffsetPixels Value in pixels indicating the offset from position.
         */
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

        }

        /**
         * This method will be invoked when a new page becomes selected. Animation is not
         * necessarily complete.
         *
         * @param position Position index of the new selected page.
         */
        public void onPageSelected(int position){

        }

        /**
         * Called when the scroll state changes. Useful for discovering when the user
         * begins dragging, when the pager is automatically settling to the current page,
         * or when it is fully stopped/idle.
         *
         * @param state The new scroll state.
         * @see ViewPager#SCROLL_STATE_IDLE
         * @see ViewPager#SCROLL_STATE_DRAGGING
         * @see ViewPager#SCROLL_STATE_SETTLING
         */
        public void onPageScrollStateChanged(int state){

        }
    }
}