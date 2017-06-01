package com.liuwei1995.lwvlayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.liuwei1995.lwvlayout.adapter.AdPagerAdapter;
import com.liuwei1995.lwvlayout.adapter.HealthyAdapter;
import com.liuwei1995.lwvlayout.adapter.HealthyViewHolder;
import com.liuwei1995.lwvlayout.adapter.LRecyclerViewAdapter;
import com.liuwei1995.lwvlayout.adapter.LViewholder;
import com.liuwei1995.lwvlayout.entity.FindEntity;
import com.liuwei1995.lwvlayout.views.AdViewPager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdViewPager adViewPager = (AdViewPager) findViewById(R.id.ad_vp);
        List<String> list_ad = new ArrayList<>();
        for (int i = 0; i < imageUrls.length; i++) {
            list_ad.add(imageUrls[i]);
        }
        adViewPager.setAdPagerAdapter(new AdPagerAdapter<String>(list_ad,R.layout.ad_item) {
            @Override
            public void convert(View view, String item, int position) {
                ImageView iv = (ImageView) view.findViewById(R.id.iv_ad);
                downloadAsyncTask downloadAsyncTask = new downloadAsyncTask(iv);
                downloadAsyncTask.execute(item);
            }
        });
        List<FindEntity> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            FindEntity findEntity = new FindEntity();
            findEntity.setViewType(i);
            if (i == 0){
                ArrayList<String> stringArrayList = new ArrayList<>();
                for (int j = 0; j < 30; j++) {
                    stringArrayList.add("MenuList="+j);
                }
                findEntity.setMenuList(stringArrayList);
            }
            if (i == 1){
                ArrayList<String> stringArrayList = new ArrayList<>();
                for (int j = 0; j < 30; j++) {
                    stringArrayList.add("TagList="+j);
                }
                findEntity.setTagList(stringArrayList);
            }
            if (i == 2){
                ArrayList<String> stringArrayList = new ArrayList<>();
                for (int j = 0; j < 30; j++) {
                    stringArrayList.add("TypeList="+j);
                }
                findEntity.setTypeList(stringArrayList);
            }
            list.add(findEntity);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        HealthyAdapter<String> adapter = new HealthyAdapter<String>(list.get(0).getMenuList(),R.layout.find_0_item) {
//            @Override
//            public void convert(HealthyViewHolder holder, String item, int position) {
//                holder.setText(R.id.tv,item);
//            }
//        };
//        recyclerView.setAdapter(adapter);


        recyclerView.setAdapter(new FindAdapater(list));

    }
    public  class FindAdapater extends RecyclerView.Adapter<HealthyViewHolder>{

        private List<FindEntity> list;

        public FindAdapater(List<FindEntity> list) {
            this.list = list;
        }


        @Override
        public HealthyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == 0){
                 view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_0, null);
            }else if (viewType == 1){
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_1, null);
            }else if (viewType == 2){
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_2, null);
            }
            return new HealthyViewHolder(view,viewType);
        }

        @Override
        public void onBindViewHolder(HealthyViewHolder holder, int position) {
            int viewType = holder.getItemViewType();
            if (viewType == 0){
                setFind0(holder,position);
            }else if (viewType == 1){
                setFind1(holder,position);
            }else if (viewType == 2){
                setFind2(holder,position);
            }
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return list.get(position).getViewType();
        }
        public void setFind0(HealthyViewHolder holder, int position){
            RecyclerView recyclerView = (RecyclerView) holder.itemView;
            FindEntity findEntity = list.get(position);
            HealthyAdapter<String> adapter = new HealthyAdapter<String>(findEntity.getMenuList(),R.layout.find_0_item) {
                @Override
                public void convert(HealthyViewHolder holder, String item, int position) {
                    holder.setText(R.id.tv,item);
                }
            };
            recyclerView.setAdapter(adapter);
        }
        public void setFind1(HealthyViewHolder holder, int position){
            RecyclerView recyclerView = (RecyclerView) holder.itemView;
            FindEntity findEntity = list.get(position);
            HealthyAdapter<String> adapter = new HealthyAdapter<String>(findEntity.getTagList(),R.layout.find_1_item) {
                @Override
                public void convert(HealthyViewHolder holder, String item, int position) {
                    holder.setText(R.id.tv,item);
                }
            };
            recyclerView.setAdapter(adapter);
        }
        public void setFind2(HealthyViewHolder holder, int position){
            RecyclerView recyclerView = (RecyclerView) holder.itemView;
            FindEntity findEntity = list.get(position);
            HealthyAdapter<String> adapter = new HealthyAdapter<String>(findEntity.getTypeList(),R.layout.find_2_item) {
                @Override
                public void convert(HealthyViewHolder holder, String item, int position) {
                    holder.setText(R.id.tv,item);
                }
            };
            recyclerView.setAdapter(adapter);
        }
    }
    public class downloadAsyncTask extends AsyncTask<String,Object,Bitmap>{

        public ImageView mImageView;

        public downloadAsyncTask(ImageView imageView) {
            mImageView = imageView;
        }

        //下载
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = urlConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (IOException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null && !bitmap.isRecycled())
                 mImageView.setImageBitmap(bitmap);
            else
                mImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        }
    }
    private String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
            "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
            "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
            "http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
            "http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};
}
