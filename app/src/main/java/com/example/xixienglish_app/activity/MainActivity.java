package com.example.xixienglish_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.BaseActivity;
import com.example.xixienglish_app.adapter.DrawerAdapter;
import com.example.xixienglish_app.adapter.DrawerItem;
import com.example.xixienglish_app.adapter.SimpleDrawerItemAdapter;
import com.example.xixienglish_app.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

import java.util.Arrays;

public class MainActivity extends BaseActivity implements DrawerAdapter.OnItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    private SlidingRootNav mSlidingRootNav;
    private String[] mMenuTitles;
    private Drawable[] mMenuIcons;
    private DrawerAdapter mAdapter;
    private Button btnDrawer;

    // 定义侧边栏选项位置
    private static final int POS_ARTICLE = 0;
    private static final int POS_VIDEO = 1;
    private static final int POS_CLASS = 2;
    private static final int POS_HOME = 3;
    private static final int POS_LOGOUT = 4;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        btnDrawer = findViewById(R.id.btn_drawer);
        mMenuTitles = ResUtils.getStringArray(R.array.menu_titles);
        mMenuIcons = ResUtils.getDrawableArray(this, R.array.menu_icons);

        // 初始化侧边栏
        initSlidingMenu(super.mSavedInstanceState);
    }

    @Override
    protected void initData() {
        // 导航下标局域栏
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        btnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        initSlidingMenu(null);
    }

    // 侧滑栏状态判断
    public void openMenu() {
        if (mSlidingRootNav != null) {
            mSlidingRootNav.openMenu();
            mSlidingRootNav.getLayout().findViewById(R.id.drawer_menu).setVisibility(View.VISIBLE);
        }
    }

    public void closeMenu() {
        if (mSlidingRootNav != null) {
            mSlidingRootNav.closeMenu();
            mSlidingRootNav.getLayout().findViewById(R.id.drawer_menu).setVisibility(View.GONE);
        }
    }

    public boolean isMenuOpen() {
        if (mSlidingRootNav != null) {
            return mSlidingRootNav.isMenuOpened();
        }
        return false;
    }

    public boolean isMenuClosed() {
        if (mSlidingRootNav != null) {
            return mSlidingRootNav.isMenuClosed();
        }
        return false;
    }

    // 初始化侧滑栏列表项信息
    private DrawerItem createItemFor(int position) {
        return new SimpleDrawerItemAdapter(mMenuIcons[position], mMenuTitles[position])
                .withIconTint(ThemeUtils.resolveColor(this, R.attr.xui_config_color_content_text))
                .withTextTint(ThemeUtils.resolveColor(this, R.attr.xui_config_color_content_text))
                .withSelectedIconTint(ThemeUtils.resolveColor(this, R.attr.colorAccent))
                .withSelectedTextTint(ThemeUtils.resolveColor(this, R.attr.colorAccent));
    }

    // 封装初始化侧边栏，代码一般，耦合性较高，initView集成在里面
    private void initSlidingMenu(Bundle savedInstanceState) {
        // 初始化侧滑栏数据，调用库调参数
        mSlidingRootNav = new SlidingRootNavBuilder(this)
                .withGravity(ResUtils.isRtl() ? SlideGravity.RIGHT : SlideGravity.LEFT)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        LinearLayout mLLMenu = mSlidingRootNav.getLayout().findViewById(R.id.drawer_menu);
        final AppCompatImageView ivQrcode = mSlidingRootNav.getLayout().findViewById(R.id.iv_qrcode);
        final AppCompatImageView ivSetting = mSlidingRootNav.getLayout().findViewById(R.id.iv_setting);

        mAdapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_ARTICLE).setChecked(true),
                createItemFor(POS_VIDEO),
                createItemFor(POS_CLASS),
                createItemFor(POS_HOME),
                createItemFor(POS_LOGOUT)));
        // 重要逻辑
        mAdapter.setListener(this);

        // 将adapter写入列表项
        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(mAdapter);

        // closeMenu();
        mLLMenu.setVisibility(View.GONE);

        mAdapter.setSelected(POS_ARTICLE);
        // 开启菜单初始显示功能
        mSlidingRootNav.setMenuLocked(false);
        mSlidingRootNav.getLayout().addDragStateListener(new DragStateListener() {
            @Override
            public void onDragStart() {
                showToast("DragStart!");

            }

            @Override
            public void onDragEnd(boolean isMenuOpened) {
                showToast("DragEnd!");
/*                if (isMenuOpen()){
                    mLLMenu.setVisibility(View.VISIBLE);
                }
                else if (isMenuClosed()) {
                    mLLMenu.setVisibility(View.GONE);
                }*/
                // 迷惑??
/*                if (isMenuOpened) {
                    if (!GuideCaseView.isShowOnce(MainActivity.this, getString(R.string.guide_key_sliding_root_navigation))) {
                        final GuideCaseView guideStep1 = new GuideCaseView.Builder(MainActivity.this)
                                .title("点击进入，可切换主题样式~")
                                .titleSize(18, TypedValue.COMPLEX_UNIT_SP)
                                .focusOn(ivSetting)
                                .build();

                        final GuideCaseView guideStep2 = new GuideCaseView.Builder(MainActivity.this)
                                .title("点击进入，扫码关注~")
                                .titleSize(18, TypedValue.COMPLEX_UNIT_SP)
                                .focusOn(ivQrcode)
                                .build();

                        new GuideCaseQueue()
                                .add(guideStep1)
                                .add(guideStep2)
                                .show();
                        GuideCaseView.setShowOnce(MainActivity.this, getString(R.string.guide_key_sliding_root_navigation));
                    }
                }*/
            }
        });
    }


    @Override
    public void onItemSelected(int position) {
        switch (position) {
            case POS_ARTICLE:
            case POS_VIDEO:
            case POS_CLASS:
            case POS_HOME:
/*                if (mTabLayout != null) {
                    TabLayout.Tab tab = mTabLayout.getTabAt(position);
                    if (tab != null) {
                        tab.select();
                    }
                }*/
                showToast("Test");
                closeMenu();
                break;
            case POS_LOGOUT:
                DialogLoader.getInstance().showConfirmDialog(
                        this,
                        "是否确认退出?",
                        "是",
                        (dialog, which) -> {
                            dialog.dismiss();
//                            TokenUtils.handleLogoutSuccess();
                            finish();
                        },
                        "否",
                        (dialog, which) -> dialog.dismiss()
                );
                break;
            default:
                break;
        }
    }
}