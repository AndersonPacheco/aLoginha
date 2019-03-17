package br.com.alojinha.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.alojinha.R;
import br.com.alojinha.model.Banner;

public class BannerAdapter extends PagerAdapter {

    private List<Banner> data;
    private Context ctx;

    public BannerAdapter(Context ctx, List<Banner> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object instantiateItem(View collection, int position) {
        ImageView myImageView = new ImageView(ctx);

        Glide
                .with(ctx)
                .load(data.get(position).getUrlImagem())
                .centerCrop()
                .into(myImageView);

        ((ViewPager)collection).addView(myImageView);
        return myImageView;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public void startUpdate(View arg0) {
    }

    @Override
    public void finishUpdate(View arg0) {
    }
}
