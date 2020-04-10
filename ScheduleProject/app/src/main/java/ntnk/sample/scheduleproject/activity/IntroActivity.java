package ntnk.sample.scheduleproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.adapter.IntroSlidePagerAdapter;

public class IntroActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private LinearLayout dotsLayout;

    private IntroSlidePagerAdapter pagerAdapter;
    TextView[] mDots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        viewPager = findViewById(R.id.introViewPager);
        dotsLayout = findViewById(R.id.dotsLayout);

        pagerAdapter = new IntroSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(viewListener);
        addDotsIndicator(0);
    }
    public void addDotsIndicator(int pos) {
        mDots = new TextView[4];
        dotsLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorGreenTransparent));
            dotsLayout.addView(mDots[i]);
        }
        if(mDots.length > 0) {
            mDots[pos].setTextColor(getResources().getColor(R.color.colorGreen));
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
