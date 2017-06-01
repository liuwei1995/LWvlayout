package com.liuwei1995.lwvlayout.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by linxins on 17-6-1.
 */

public abstract class LRecyclerViewAdapter<T> extends RecyclerView.Adapter<LViewholder> {

    public LRecyclerViewAdapter() {
    }
    protected List<T> list;

    private int mLayoutId;
    private LViewholder mLViewholder;

    public LRecyclerViewAdapter(LViewholder LViewholder) {
        mLViewholder = LViewholder;
    }

    public LRecyclerViewAdapter(int layoutId) {
        mLayoutId = layoutId;
    }

    @Override
    public LViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onViewHolder(parent,viewType);
    }


    public SparseArray<View> mViewSparseArray = null;

    public LRecyclerViewAdapter putLViewholder(int viewType,View view){
        if (mViewSparseArray == null){
            synchronized (LRecyclerViewAdapter.class){
                if (mViewSparseArray ==  null)
                    mViewSparseArray = new SparseArray<>();
            }
        }
        mViewSparseArray.put(viewType,view);
        return this;
    }

    public LViewholder onViewHolder(ViewGroup parent, int viewType){
        if (mLayoutId <= 0)return new LViewholder(mViewSparseArray.get(viewType));
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new LViewholder(view);
    }

    @Override
    public void onBindViewHolder(LViewholder holder, int position) {

    }

    public  abstract void convert(LViewholder holder,T item,int position);

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
