package com.example.xixienglish_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.xixienglish_app.activity.BaseActivity;
import com.example.xixienglish_app.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLayout() {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(binding.bottomNavView.getMenu()).build();
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
    }

    @Override
    protected void initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
    }
}