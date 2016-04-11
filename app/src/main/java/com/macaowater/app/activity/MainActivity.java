package com.macaowater.app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.macaowater.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.lv_left_menu)
    ListView lvLeftMenu;
    @Bind(R.id.dl_right)
    LinearLayout dlRight;
    @Bind(R.id.dl_left)
    DrawerLayout dlLeft;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private ArrayAdapter<String> adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] listData = {"Java", "Kotlin", "Oc", "Swift"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);

        toolBar.setTitle("ToolBar");//设置标题
        toolBar.setTitleTextColor(Color.parseColor("#ffffff"));//设置标题颜色
        setSupportActionBar(toolBar);

        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //创建返回键，并且实现开/闭
        mDrawerToggle = new ActionBarDrawerToggle(this, dlLeft, toolBar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                text.setText("");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                text.setText("my name is DrawerLayout");
            }
        };

        mDrawerToggle.syncState();
        dlLeft.setDrawerListener(mDrawerToggle);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listData);
        lvLeftMenu.setAdapter(adapter);


    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
