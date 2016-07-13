package com.linyuzai.carouseldemo;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.linyuzai.carousel.XCarouselView;
import com.linyuzai.carousel.XViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    XCarouselView xCarouselView;
    XViewPager xViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xCarouselView = (XCarouselView) findViewById(R.id.xcv);
        //     xCarouselView = new XCarouselView(this);
        //     xCarouselView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        xCarouselView.setPointDefaultDrawable(getResources().getDrawable(R.mipmap.point_small));
        xCarouselView.setPointSelectDrawable(getResources().getDrawable(R.mipmap.point_big));
        xCarouselView.setPointStyle(200, 5, 10);
        xCarouselView.setPageSavedLimit(3);
        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            list.add(new CFragment());
        xCarouselView.setAdapter(new XCarouselView.XFragmentAdapter(getSupportFragmentManager(), list) {
            @Override
            protected Fragment getFirstFragment(Bundle bundle) {
                return new CFragment();
            }

            @Override
            protected Fragment getLastFragment(Bundle bundle) {
                return new CFragment();
            }

            @Override
            protected void bindData(Fragment fragment, int position) {

            }
        }, R.id.xpv_id);
        xCarouselView.setOnXPageChangeListener(new XCarouselView.OnXPageChangeListener() {
            @Override
            public void onXPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onXPageSelected(int position) {

            }

            @Override
            public void onXPageScrollStateChanged(int state) {

            }

            @Override
            public void onCurrentPageSelected(int position) {
                Log.e("select", position + "");
            }
        });

        xViewPager = (XViewPager) findViewById(R.id.xvp);
        assert xViewPager != null;
        xViewPager.setAutoScrollable(true);
        List<Fragment> list2 = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            list2.add(new CFragment());
        xViewPager.setAdapter(new XViewPager.XFragmentAdapter(getSupportFragmentManager(), list2) {
            @Override
            protected Fragment getFirstFragment(Bundle bundle) {
                return new CFragment();
            }

            @Override
            protected Fragment getLastFragment(Bundle bundle) {
                return new CFragment();
            }

            @Override
            protected void bindData(Fragment fragment, int position) {

            }
        });
    }
}
