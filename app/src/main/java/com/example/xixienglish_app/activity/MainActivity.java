package com.example.xixienglish_app.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.DrawerAdapter;
import com.example.xixienglish_app.adapter.DrawerItem;
import com.example.xixienglish_app.adapter.SimpleDrawerItemAdapter;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.InformationEntity;
import com.example.xixienglish_app.entity.InformationResponse;
import com.example.xixienglish_app.entity.TranslationResponse;
import com.example.xixienglish_app.entity.TripletEntity;
import com.example.xixienglish_app.fragment.ArticleFragment;
import com.example.xixienglish_app.fragment.ClassFragment;
import com.example.xixienglish_app.fragment.MyFragment;
import com.example.xixienglish_app.fragment.VideoFragment;
import com.example.xixienglish_app.util.PhotoPopupWindow;
import com.example.xixienglish_app.util.StringUtils;
import com.example.xixienglish_app.util.XToastUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.guidview.GuideCaseQueue;
import com.xuexiang.xui.widget.guidview.GuideCaseView;
import com.xuexiang.xui.widget.popupwindow.bar.CookieBar;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements DrawerAdapter.OnItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    private SlidingRootNav mSlidingRootNav;
    private String[] mMenuTitles;
    private Drawable[] mMenuIcons;
    private DrawerAdapter mAdapter;
    private Button btnDrawer;
    private TextView tv_name;

    // 换头像
    private ImageView imageAvatar;
    private PhotoPopupWindow mPhotoPopupWindow;
    // 图片处理逻辑
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SMALL_IMAGE_CUTTING = 2;
    private static final int REQUEST_BIG_IMAGE_CUTTING = 3;
    private static final String IMAGE_FILE_NAME = "icon.jpg";
    private Uri mImageUri;

    // 搜索框，查单词逻辑
    private SearchView searchView;
    public static final int TRANS = 0x1;
    private String english;
    private String chinese;
    private Activity mActivity = this;

    // 定义侧边栏选项位置
    private static final int POS_ARTICLE = 0;
    private static final int POS_VIDEO = 1;
    private static final int POS_CLASS = 2;
    private static final int POS_HOME = 3;
    private static final int POS_LOGOUT = 4;

    /**
     * Handler用于获取中英翻译单词
     */
    protected Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TRANS:
                    chinese = (String)msg.obj;
                    /**
                     * **********       以下接入弹出窗逻辑        **********
                     */
                    CookieBar.builder(mActivity)
                            .setTitle(english)
                            .setMessage(chinese)
                            .setDuration(3000)
                            .setBackgroundColor(R.color.colorPrimaryDark)
                            .setActionColor(android.R.color.white)
                            .setTitleColor(android.R.color.white)
                            .setAction("加入生词本", view -> {

                                addToWordBook(english, chinese);
                            })
                            .show();

                    showToast("翻译" + english);
                    break;
            }
        }

    };


    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        // 侧边栏数据
        btnDrawer = findViewById(R.id.btn_drawer);
        mMenuTitles = ResUtils.getStringArray(R.array.menu_titles);
        mMenuIcons = ResUtils.getDrawableArray(this, R.array.menu_icons);

        // 初始化侧边栏
        initSlidingMenu(super.mSavedInstanceState);

        imageAvatar = findViewById(R.id.iv_avatar);

        // 获取用户名
        initUserName();

        // 获取搜索框
        searchView = findViewById(R.id.search_view);
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

        // 换头像逻辑
        imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoPopupWindow = new PhotoPopupWindow(MainActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 权限申请，读取本地图片
                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // 权限还没有授予，需要在这里写申请权限的代码
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                        } else {
                            // 如果权限已经申请过，直接进行图片选择
                            mPhotoPopupWindow.dismiss();
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            // 判断系统中是否有处理该 Intent 的 Activity
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(intent, REQUEST_IMAGE_GET);
                            } else {
                                // toast可换可不换
                                Toast.makeText(MainActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 权限申请，获取系统摄像头权限
                        showToast("选择拍照");
                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // 权限还没有授予，需要在这里写申请权限的代码
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300);
                        } else {
                            // 权限已经申请，直接拍照
                            mPhotoPopupWindow.dismiss();
                            // 拍照逻辑
                            imageCapture();
                        }
                    }
                }
                );
                // getWindow().getDecorView()获取当前页面的View
                mPhotoPopupWindow.showAtLocation(getWindow().getDecorView(),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        // 搜索框逻辑
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 提交时触发的方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query)) {
                    showToast("请输入要查找的单词~");
                }
                else {
                    // 翻译
                    english = query;
                    getTranslation(query);

                }
                return false;
            }

            // 输入时触发的方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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

        mAdapter.setSelected(POS_ARTICLE);
        // 开启菜单初始显示功能
        mSlidingRootNav.setMenuLocked(false);
        mSlidingRootNav.getLayout().addDragStateListener(new DragStateListener() {
            @Override
            public void onDragStart() {
                // 显示状态栏名称
                tv_name = mSlidingRootNav.getLayout().findViewById(R.id.tv_name);
                if (!getValueFromSp("name").equals("")) {
                    tv_name.setText(getValueFromSp("name"));
                }
            }

            @Override
            public void onDragEnd(boolean isMenuOpened) {

                // 调用库，初始化动画 —— 控件教学，可改***
                if (isMenuOpened) {
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
                }
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
                closeMenu();
                break;
            case POS_LOGOUT:
                DialogLoader.getInstance().showConfirmDialog(
                        this,
                        "是否确认退出?",
                        "是",
                        (dialog, which) -> {
                            dialog.dismiss();
                            finishAffinity();
                        },
                        "否",
                        (dialog, which) -> dialog.dismiss()
                );
                break;
            default:
                break;
        }
    }

    /**
     ************* 更换头像逻辑 ***********
     */
    /**
     * 处理回调结果，重写原生函数
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 回调成功
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 小图切割
                case REQUEST_SMALL_IMAGE_CUTTING:
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
                // 大图切割
                case REQUEST_BIG_IMAGE_CUTTING:
                    Bitmap bitmap = BitmapFactory.decodeFile(mImageUri.getEncodedPath());
                    imageAvatar.setImageBitmap(bitmap);
                    break;
                // 相册选取
                case REQUEST_IMAGE_GET:
                    try {
                        // startSmallPhotoZoom(data.getData());
                        startBigPhotoZoom(data.getData());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    break;
                // 拍照
                case REQUEST_IMAGE_CAPTURE:
                    File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                    // startSmallPhotoZoom(Uri.fromFile(temp));
                    startBigPhotoZoom(temp);
            }
        }
    }

    /**
     * 处理权限回调结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPhotoPopupWindow.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    // 判断系统中是否有处理该 Intent 的 Activity
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_IMAGE_GET);
                    } else {
                        Toast.makeText(MainActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mPhotoPopupWindow.dismiss();
                }
                break;
            case 300:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPhotoPopupWindow.dismiss();
                    imageCapture();
                } else {
                    mPhotoPopupWindow.dismiss();
                }
                break;
        }
    }

    /**
     * 判断系统及拍照
     */
    private void imageCapture() {
        Intent intent;
        Uri pictureUri;
        File pictureFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
        // 判断当前系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pictureUri = FileProvider.getUriForFile(this,
                    "com.example.xixienglish_app.fileProvider", pictureFile);
        } else {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            pictureUri = Uri.fromFile(pictureFile);
        }
        // 去拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }


    /**
     * 小图模式中，保存图片后，设置到视图中
     * 将图片保存设置到视图中
     * 待加入后端逻辑***，可以写入数据库后端中***
     */
    private void setPicToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            // 创建 smallIcon 文件夹
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String storage = Environment.getExternalStorageDirectory().getPath();
                File dirFile = new File(storage + "/smallIcon");
                if (!dirFile.exists()) {
                    if (!dirFile.mkdirs()) {
                        Log.e("TAG", "文件夹创建失败");
                    } else {
                        Log.e("TAG", "文件夹创建成功");
                    }
                }
                File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
                // 保存图片
                FileOutputStream outputStream;
                try {
                    outputStream = new FileOutputStream(file);
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 在视图中显示图片
            imageAvatar.setImageBitmap(photo);
        }
    }

    /**
     * 小图模式切割图片
     * 此方式直接返回截图后的 bitmap，由于内存的限制，返回的图片会比较小
     */
    public void startSmallPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300); // 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_SMALL_IMAGE_CUTTING);
    }

    /**
     * 大图模式切割图片
     * 直接创建一个文件将切割后的图片写入
     */
    public void startBigPhotoZoom(File inputFile) {
        // 创建大图文件夹
        Uri imageUri = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String storage = Environment.getExternalStorageDirectory().getPath();
            File dirFile = new File(storage + "/bigIcon");
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    Log.e("TAG", "文件夹创建失败");
                } else {
                    Log.e("TAG", "文件夹创建成功");
                }
            }
            File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
            imageUri = Uri.fromFile(file);
            mImageUri = imageUri; // 将uri传出，方便设置到视图中
        }

        // 开始切割
        Intent intent = new Intent("com.android.camera.action.CROP");
        // !!! 导入参数可能有问题
        intent.setDataAndType(getImageContentUri(MainActivity.this, inputFile), "image/*");

        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 600); // 输出图片大小
        intent.putExtra("outputY", 600);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false); // 不直接返回数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 返回一个文件
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING);
    }

    /**
     * 函数重载
     */
    public void startBigPhotoZoom(Uri uri) {
        // 创建大图文件夹
        Uri imageUri = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String storage = Environment.getExternalStorageDirectory().getPath();
            File dirFile = new File(storage + "/bigIcon");
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    Log.e("TAG", "文件夹创建失败");
                } else {
                    Log.e("TAG", "文件夹创建成功");
                }
            }
            File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
            imageUri = Uri.fromFile(file);
            mImageUri = imageUri; // 将 uri 传出，方便设置到视图中
        }

        // 开始切割
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 600); // 输出图片大小
        intent.putExtra("outputY", 600);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false); // 不直接返回数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 返回一个文件
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, REQUEST_BIG_IMAGE_CUTTING);
    }

    /**
     * 获取照片对应保存的路径
     */
    public Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 直接获取用户名，显示在界面
     */
    public void initUserName() {
        String token = getValueFromSp("token");
        if (!token.equals("")) {
            HttpCallBack callBack = new HttpCallBack() {
                @Override
                public void onSuccess(String res) {
                    Log.e("onSuccess", res);
//                    showToastSync(res);
                    Gson gson = new Gson();
                    InformationResponse informationResponse = gson.fromJson(res, InformationResponse.class);
                    String name = informationResponse.getData().getName();
                    insertValueToSp("name", name);
                }

                @Override
                public void onFailure(Exception e) {

                }
            };

            OkHttpClient client = new OkHttpClient.Builder().build();
            String url = "http://139.196.153.21:8888/personalMessage";
            Request request = new Request.Builder()
                    .url(url)
                    .get()              // 请求方法是get
                    .addHeader("token", token)
                    .build();
            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callBack.onFailure(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    Log.d("onSuccess","---Success---");
                    callBack.onSuccess(result);
                }
            });
        }
    }

    /**
     * ************         以下为搜索框查单词逻辑         ************
     */
    public void getTranslation(String word) {

        /**
         * MD5加密
         */
        String mix = "20201210000643977" + word + "123456" + "k7nyCImNOwOosUeGvYzU";
        byte[] md5 = new byte[0];
        try {
            md5 = MessageDigest.getInstance("MD5").digest(mix.getBytes());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        StringBuilder hex = new StringBuilder(md5.length * 2);
        for (byte b: md5) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        String sign = hex.toString();


        /**
         * 此处重写回调方法
         */
        HttpCallBack callBack = new HttpCallBack() {

            @Override
            public void onSuccess(String res) {
                Log.e("onSuccess", res);
//                showToastSync(res);

                Gson gson = new Gson();
                TranslationResponse translationResponse = gson.fromJson(res, TranslationResponse.class);
                // 取出translationResult中的译文
                String translation = translationResponse.getTrans_result().get(0).getDst();
                Message msg = handler.obtainMessage();
                msg.what = TRANS;
                msg.obj = translation;
                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        HashMap<String, Object> hash = new HashMap<>();

        hash.put("q", word);
        hash.put("from", "en");
        hash.put("to", "zh");
        hash.put("appid", "20201210000643977");
        hash.put("salt", "123456");
        hash.put("sign", sign);

        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = "http://api.fanyi.baidu.com/api/trans/vip/translate";
        String requestUrl = getAppendUrl(url, hash);
        Request request = new Request.Builder()
                .url(requestUrl)
                .get()              // 请求方法是get
                .build();
        Call call = client.newCall(request);


        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.d("onSuccess","---Success---");
                callBack.onSuccess(result);
            }
        });

    }


    // 拼接url，用于带params发送get请求
    private String getAppendUrl(String url, Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (StringUtils.isEmpty(buffer.toString())) {
                    buffer.append("?");
                } else {
                    buffer.append("&");
                }
                buffer.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += buffer.toString();
        }
        return url;
    }


    /**
     * 加入生词本逻辑
     */
    public void addToWordBook(String englishword, String chineseword) {
        DialogLoader.getInstance().showConfirmDialog(
                mContext,
                "是否确认加入生词本?",
                "是",
                (dialog, which) -> {
                    dialog.dismiss();
                    HashMap<String, Object> bodyInfo = new HashMap<String, Object> () {{
                        put("english", englishword);
                        put("chinese",chineseword);
                    }};
                    System.out.println(bodyInfo);
                    Api.config("/add/glossary", bodyInfo).postRequestWithToken(mActivity, new HttpCallBack() {
                        @Override
                        public void onSuccess(String res) {
//                        showToastSync(res);

                            Gson gson = new Gson();
                            TripletEntity tripletEntity = gson.fromJson(res, TripletEntity.class);
                            if (tripletEntity.getCode() == 200) {
                                Looper.prepare();
                                XToastUtils.toast(tripletEntity.getMsg());
                                Looper.loop();
                            }
                            else {
                                Looper.prepare();
                                XToastUtils.toast(tripletEntity.getMsg());
                                Looper.loop();
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            e.printStackTrace();
                        }
                    });

                },
                "否",
                (dialog, which) -> dialog.dismiss()
        );
    }
}