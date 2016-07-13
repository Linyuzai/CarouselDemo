package com.linyuzai.carousel;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class XViewPager extends ViewPager {
    public static final String TAG = XViewPager.class.getSimpleName();

    private XScroller xScroller;
    private int manualDuration;
    private int autoDuration;

    private boolean isHand;
    private boolean isAutoScrollable;
    private int intervalDuration;
    private int currentPosition;
    private OnXPageChangeListener xPageChangeListener;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (!isHand && isAutoScrollable) {
                xScroller.setDuration(autoDuration);
                XViewPager.this.setCurrentItem(currentPosition + 1);
            }
        }
    };

    public XViewPager(Context context) {
        super(context);
        init(context);
    }

    public XViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    protected void init(Context context) {
        addOnPageChangeListener(new OnPageChangeListenerImpl());
        intervalDuration = 5000;
        autoDuration = 250;
        manualDuration = 250;
        xScroller = new XScroller(context);
        changeDuration();
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        setCurrentItem(1, false);
        handler.removeMessages(0);
    }

    public void setAutoScrollable(boolean autoScrollable) {
        isAutoScrollable = autoScrollable;
    }

    public void setScrollDuration(int duration) {
        xScroller.setDuration(duration);
        autoDuration = duration;
    }

    public void setIntervalDuration(int duration) {
        intervalDuration = duration;
    }

    public void setOnXPageChangeListener(OnXPageChangeListener listener) {
        xPageChangeListener = listener;
    }

    public void startPlay() {
        handler.sendEmptyMessageDelayed(0, intervalDuration);
    }

    public void stopPlay() {
        handler.removeMessages(0);
    }

    private void changeDuration() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(this, xScroller);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            isHand = true;
            xScroller.setDuration(manualDuration);
            //    Log.e(TAG, "onTouchEvent");
        }
        return super.onTouchEvent(ev);
    }

    class XScroller extends Scroller {

        private int duration = 250;

        public XScroller(Context context) {
            super(context);
        }

        public XScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public XScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, duration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, this.duration);
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }

    public abstract static class XFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public XFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            fragments.add(0, getLastFragment(fragments.get(fragments.size() - 1).getArguments()));
            fragments.add(getFirstFragment(fragments.get(1).getArguments()));
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void notifyUpdate() {
            for (int position = 1; position < getCount() - 1; position++)
                bindData(fragments.get(position), position - 1);
            bindData(fragments.get(0), getCount() - 3);
            bindData(fragments.get(getCount() - 1), 0);
        }

        protected abstract Fragment getFirstFragment(Bundle bundle);

        protected abstract Fragment getLastFragment(Bundle bundle);

        protected abstract void bindData(Fragment fragment, int position);
    }

    /*public abstract static class XViewAdapter extends PagerAdapter {
        private List<Object> datas;
        private List<View> views;

        public XViewAdapter(List<Object> datas) {
            datas.add(0, datas.get(datas.size() - 1));
            datas.add(datas.get(1));
            this.datas = datas;
            createView();
        }

        private void createView() {
            views = new ArrayList<>();
            for (int i = 1; i < getCount() - 1; i++)
                views.add(getView(i - 1, datas.get(i)));
            views.add(0, getView(views.size() - 1, datas.get(0)));
            views.add(getView(0, datas.get(1)));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return true;
        }

        protected abstract View getView(int position, Object date);

    }*/

    public class OnPageChangeListenerImpl implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //    Log.e(TAG, "onPageScrolled:" + position);
            if (xPageChangeListener != null)
                xPageChangeListener.onXPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            //   Log.e(TAG, "onPageSelected:" + position);
            currentPosition = position;
            if (xPageChangeListener != null)
                xPageChangeListener.onXPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //   Log.e(TAG, "onPageScrollStateChanged:" + state);
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                handler.removeMessages(0);
                if (currentPosition == getAdapter().getCount() - 1) {
                    setCurrentItem(1, false);
                    if (xPageChangeListener != null)
                        xPageChangeListener.onCurrentPageSelected(0);
                } else if (currentPosition == 0) {
                    setCurrentItem(getAdapter().getCount() - 2, false);
                    if (xPageChangeListener != null)
                        xPageChangeListener.onCurrentPageSelected(getAdapter().getCount() - 3);
                } else {
                    if (xPageChangeListener != null)
                        xPageChangeListener.onCurrentPageSelected(currentPosition - 1);
                }

                isHand = false;
                if (isAutoScrollable)
                    handler.sendEmptyMessageDelayed(0, intervalDuration);
            }
            if (xPageChangeListener != null)
                xPageChangeListener.onXPageScrollStateChanged(state);
        }
    }

    public interface OnXPageChangeListener {
        void onXPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onXPageSelected(int position);

        void onXPageScrollStateChanged(int state);

        void onCurrentPageSelected(int position);
    }
}
