package com.example.linson.materialdemo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "log";
    private DrawerLayout dl;
    private ArrayList<FruitBean> mlist = new ArrayList<>();;
    private SwipeRefreshLayout srl;
    int[] arr = {R.mipmap.fruit_1,
            R.mipmap.fruit_2,
            R.mipmap.fruit_3,
            R.mipmap.fruit_4,
            R.mipmap.fruit_5,
            R.mipmap.fruit_6,
            R.mipmap.fruit_7,
            R.mipmap.fruit_8,
            R.mipmap.fruit_9,
            R.mipmap.fruit_10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dl = (DrawerLayout) findViewById(R.id.dl);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        }
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setCheckedItem(R.id.nav_1);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                dl.closeDrawers();
                return true;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"DDDD",Snackbar.LENGTH_LONG).setAction("click", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });
        initData();
        initUI();
    }

    private void initUI() {
        final MyAdapter myAdapter = new MyAdapter(mlist);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setAdapter(myAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(gridLayoutManager);
        srl = (SwipeRefreshLayout) findViewById(R.id.srl);
        srl.setColorSchemeResources(R.color.colorPrimary);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh: ");
                initData();
                myAdapter.notifyDataSetChanged();
                srl.setRefreshing(false);
            }
        });
    }

    private void initData() {
        mlist.clear();
        for (int i = 0; i < 10; i++) {
            FruitBean fruitBean = new FruitBean();
            fruitBean.setName(String.valueOf(new Random().nextInt(10000)));
            fruitBean.setImageId(arr[new Random().nextInt(10)]);
            mlist.add(fruitBean);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.del:
                Log.i(TAG, "onOptionsItemSelected: del");
                break;
            case R.id.setting:
                Log.i(TAG, "onOptionsItemSelected: setting");
                break;
            case android.R.id.home:
                Log.i(TAG, "onOptionsItemSelected: home");
                dl.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        ArrayList<FruitBean> list = null;
        private Context context;

        public MyAdapter(ArrayList list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
            context = parent.getContext();

            final View inflate = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
            final ViewHolder viewHolder = new ViewHolder(inflate);
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int postion = viewHolder.getAdapterPosition();
                    FruitBean fruitBean = mlist.get(postion);
                    Intent intent = new Intent(context, FruitActivity.class);
                    intent.putExtra(FruitActivity.FRUIT_NAME, fruitBean.getName());
                    intent.putExtra(FruitActivity.FRUIT_IMAGE_ID, fruitBean.getImageId());
                    context.startActivity(intent);
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            FruitBean bean = list.get(position);
            holder.item_tv.setText(bean.getName());
            Glide.with(getApplicationContext()).load(bean.getImageId()).into(holder.item_iv);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView item_iv;
            TextView item_tv;
            CardView cardView;
            public ViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView;
                item_iv = (ImageView) itemView.findViewById(R.id.item_iv);
                item_tv = (TextView) itemView.findViewById(R.id.item_tv);
            }
        }
    }

}
