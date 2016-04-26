package com.macaowater.app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.macaowater.app.R;
import com.macaowater.app.view.KnobView;
import com.macaowater.app.view.OutputView;
import com.macaowater.app.view.ThreeView;
import com.sevenheaven.iosswitch.ShSwitchView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Karma on 2016/4/20.
 */
public class MainActivity extends AppCompatActivity {


    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.switch_view)
    ShSwitchView mSwitchView;
    @Bind(R.id.zuo_biao)
    ThreeView mZuoBiao;
    @Bind(R.id.output_view)
    OutputView mOutputView;
    @Bind(R.id.bar3)
    KnobView mBar3;
    @Bind(R.id.main_layout)
    RelativeLayout mMainLayout;
    @Bind(R.id.lv_left_menu)
    ListView mLvLeftMenu;
    @Bind(R.id.dl_right)
    LinearLayout mDlRight;
    @Bind(R.id.dl_left)
    DrawerLayout mDlLeft;
    private ArrayAdapter<String> adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] listData = {"Java", "Kotlin", "Oc", "Swift"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);

        //开关
        mSwitchView.setOn(true);

        mToolBar.setTitle("ToolBar");//设置标题
        mToolBar.setTitleTextColor(Color.parseColor("#ffffff"));//设置标题颜色
        setSupportActionBar(mToolBar);

        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //创建返回键，并且实现开/闭
        mDrawerToggle = new ActionBarDrawerToggle(this, mDlLeft, mToolBar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };

        mDrawerToggle.syncState();
        mDlLeft.setDrawerListener(mDrawerToggle);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listData);
        mLvLeftMenu.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
