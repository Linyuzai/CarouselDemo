package com.linyuzai.carousel;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class XCarouselView extends RelativeLayout implements XViewPager.OnXPageChangeListener {
    public static final String TAG = XCarouselView.class.getSimpleName();

    private Context context;
    private XViewPager xViewPager;
    private PagerAdapter adapter;
    private LinearLayout linearLayout;
    private List<ImageView> points;

    private OnXPageChangeListener xPageChangeListener;

    private Drawable pointSelectDrawable;
    private Drawable pointDefaultDrawable;

    private int pointCount;
    private int selectPoint;

    private int pointSize;
    private int pointSpace;
    private int bottomSpace;

    public XCarouselView(Context context) {
        super(context);
        init(context);
    }

    public XCarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public XCarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public XCarouselView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    protected void init(Context context) {
        this.context = context;
        pointSize = 20;
        pointSpace = 10;
        bottomSpace = 10;
        xViewPager = new XViewPager(context);
        xViewPager.setScrollDuration(1500);
        xViewPager.setAutoScrollable(true);
        xViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        xViewPager.setOnXPageChangeListener(this);
        linearLayout = new LinearLayout(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER);
        points = new ArrayList<>();
        addView(xViewPager);
        addView(linearLayout);
    }

    protected ImageView getPointView(ImageView point, int position) {
        point.setLayoutParams(new LinearLayout.LayoutParams(pointSize, pointSize));
        point.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (position == 0)
            point.setImageDrawable(pointSelectDrawable);
        else
            point.setImageDrawable(pointDefaultDrawable);
        point.setPadding(pointSpace, 0, pointSpace, bottomSpace);
        return point;
    }

    public void setPointStyle(int pointSize, int pointSpace, int bottomSpace) {
        this.pointSize = pointSize;
        this.pointSpace = pointSpace;
        this.bottomSpace = bottomSpace;
    }

    public void setOnXPageChangeListener(OnXPageChangeListener listener) {
        xPageChangeListener = listener;
    }

    public void setPointSelectDrawable(Drawable drawable) {
        pointSelectDrawable = drawable;
    }

    public void setPointDefaultDrawable(Drawable drawable) {
        pointDefaultDrawable = drawable;
    }

    public void setPageSavedLimit(int count) {
        xViewPager.setOffscreenPageLimit(count + 2);
    }

    public void setAdapter(PagerAdapter adapter, int id) {
        xViewPager.setId(id);
        xViewPager.setAdapter(adapter);
        this.adapter = adapter;
        redraw();
    }

    public void redraw() {
        if (adapter == null)
            return;
        points.clear();
        linearLayout.removeAllViews();
        pointCount = adapter.getCount() - 2;
        for (int i = 0; i < pointCount; i++) {
            ImageView point = getPointView(new ImageView(context), i);
            linearLayout.addView(point);
            points.add(point);
        }
        selectPoint = 0;
    }

    public void setAutoScrollable(boolean autoScrollable) {
        xViewPager.setAutoScrollable(autoScrollable);
    }

    public void startPlay() {
        xViewPager.startPlay();
    }

    public void stopPlay() {
        xViewPager.stopPlay();
    }

    @Override
    public void onXPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (xPageChangeListener != null)
            xPageChangeListener.onXPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onXPageSelected(int position) {
        if (xPageChangeListener != null)
            xPageChangeListener.onXPageSelected(position);
    }

    @Override
    public void onXPageScrollStateChanged(int state) {
        if (xPageChangeListener != null)
            xPageChangeListener.onXPageScrollStateChanged(state);
    }

    @Override
    public void onCurrentPageSelected(int position) {
        if (selectPoint != position) {
            points.get(selectPoint).setImageDrawable(pointDefaultDrawable);
            points.get(position).setImageDrawable(pointSelectDrawable);
            selectPoint = position;
        }
        if (xPageChangeListener != null)
            xPageChangeListener.onCurrentPageSelected(position);
    }

    public abstract static class XFragmentAdapter extends XViewPager.XFragmentAdapter {

        public XFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm, fragments);
        }
    }

    /*public abstract static class XViewAdapter extends XViewPager.XViewAdapter {

        public XViewAdapter(List<Object> datas) {
            super(datas);
        }
    }*/

    public interface OnXPageChangeListener extends XViewPager.OnXPageChangeListener {
        void onXPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onXPageSelected(int position);

        void onXPageScrollStateChanged(int state);

        void onCurrentPageSelected(int position);
    }
}
