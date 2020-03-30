package ntnk.sample.scheduleproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import ntnk.sample.scheduleproject.R;
import ntnk.sample.scheduleproject.activity.BoardActivity;

public class IntroSlidePagerAdapter extends PagerAdapter {
    private Activity context;
    public static final int[] images = {
            R.drawable.step1,
            R.drawable.step2,
            R.drawable.step3,
            R.drawable.step4
    };
    public static final String[] headings = {
        "First, Create your Board",
         "Second, Schedule the tasks",
         "Third, let us notify you",
         "Finally, Job done! Wonderful\n Let's try"
    };
    public static final String [] desc = {
            "Blah blah Blah blah Blah bla Blah blah Blah blah Blah blah Blah blah Blah blah",
            "Blah blah Blah blah Blah bla Blah blah Blah blah Blah blah Blah blah Blah blah",
            "Blah blah Blah blah Blah bla Blah blah Blah blah Blah blah Blah blah Blah blah",
            "Blah blah Blah blah Blah bla Blah blah Blah blah Blah blah Blah blah Blah blah"
    };
    public IntroSlidePagerAdapter(Activity context) {
        this.context = context;
    }

    private LayoutInflater layoutInflater;
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView imageView = view.findViewById(R.id.imageView);
        TextView headingView = view.findViewById(R.id.headingTextView);
        TextView descView = view.findViewById(R.id.descTextView);
        Button startButton = view.findViewById(R.id.getStartedButton);
        startButton.setVisibility(View.GONE);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoardActivity.class);
                context.startActivity(intent);
            }
        });
        if(position == 3) {
            startButton.setVisibility(View.VISIBLE);
            }
        imageView.setImageResource(images[position]);
        headingView.setText(headings[position]);
        descView.setText(desc[position]);
        if (position == 2) view.setBackgroundColor(context.getResources().getColor(R.color.colorForStep3));
        container.addView(view, 0);

        return view;
    }
}
