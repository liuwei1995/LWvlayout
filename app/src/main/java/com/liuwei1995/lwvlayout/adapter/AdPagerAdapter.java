package com.liuwei1995.lwvlayout.adapter;

/**
 * Created by linxins on 17-6-1.
 */

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 广告轮播图的 PagerAdapter
 * Created by liuwei on 2017/4/27
 */

public abstract class AdPagerAdapter<T> extends PagerAdapter {


    private static final String TAG = "AdPagerAdapter";

    private final Map<Integer,View> map;

    protected List<T> list;
    protected  int resource;

    public AdPagerAdapter(List<T> list, @LayoutRes int resource) {
        this.list = list;
        this.resource = resource;
        map = new HashMap<>();
    }

    public <E extends View> E getView(View view,@IdRes int id){
        return (E) view.findViewById(id);
    }

//    public void setImageView(String uri, ImageView imageview){
//        ImageLoader instance = ImageLoader.getInstance();
//        instance.displayImage(uri,imageview);
//    }

    public int getListSize(){
        return list == null ? 0 : list.size();
    }
    @Override
    public int getCount() {
        //设置成最大，使用户看不到边界
        return list == null || list.size() == 0 ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        View view = (View)object;
        ((ViewPager) container).removeView(view);
        position = position % list.size();
        map.put(position,view);
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        long start = System.currentTimeMillis();
        final int index = position % list.size();
        View view = null;
        if (map.get(index) == null) {
            view = LayoutInflater.from(container.getContext()).inflate(resource, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClick != null)
                        onItemClick.onClick(list.get(index),index);
                }
            });
        } else {
            view = map.remove(index);
        }
        convert(view, list.get(index), index);
        container.addView(view);
        Log.e(TAG, "instantiateItem: time: \t" +(System.currentTimeMillis() - start));
        return view;
    }

    public abstract void convert(View view, T item, int position);

    private OnItemClick<T> onItemClick;

    public OnItemClick<T> getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(OnItemClick<T> onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick<T>{
        void onClick(T item,int position);
    }
}