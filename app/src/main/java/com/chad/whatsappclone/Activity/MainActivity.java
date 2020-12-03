package com.chad.whatsappclone.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chad.whatsappclone.Adapter.FragmentViewPagerAdapter;
import com.chad.whatsappclone.Fragments.CallsFragment;
import com.chad.whatsappclone.Fragments.CameraFragment;
import com.chad.whatsappclone.Fragments.ChatsFragment;
import com.chad.whatsappclone.Fragments.StatusFragment;
import com.chad.whatsappclone.R;
import com.chad.whatsappclone.Settings.SettingsActivity;
import com.chad.whatsappclone.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initialize();
    }

    @SuppressLint("InflateParams")
    private void initialize() {
        setUpWithViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        setSupportActionBar(binding.toolbar);

        View tab = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_camera_tab, null);
        Objects.requireNonNull(binding.tabLayout.getTabAt(0)).setCustomView(tab);

        binding.viewPager.setCurrentItem(1);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                changeFabIcon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpWithViewPager(ViewPager viewPager) {

        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        adapter.addFragment(new CameraFragment(), "Camera");
        adapter.addFragment(new ChatsFragment(), "Chats");
        adapter.addFragment(new StatusFragment(), "Status");
        adapter.addFragment(new CallsFragment(), "Calls");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
         case R.id.menu_search:
             Toast.makeText(MainActivity.this, "Search Selected", Toast.LENGTH_LONG).show();
            return true;
//         case R.id.action_more:
//             Toast.makeText(MainActivity.this, "More Selected", Toast.LENGTH_LONG).show();
//             return true;
         case R.id.action_new_group:
              Toast.makeText(MainActivity.this, "New Group Selected", Toast.LENGTH_LONG).show();
              return true;
         case R.id.action_new_brodcast:
                Toast.makeText(MainActivity.this, "New brodcast Selected", Toast.LENGTH_LONG).show();
                return true;
         case R.id.action_wa_web:
              Toast.makeText(MainActivity.this, "WhatsApp Web Selected", Toast.LENGTH_LONG).show();
              return true;
         case R.id.action_starred_messages:
              Toast.makeText(MainActivity.this, "Starred Messages Selected", Toast.LENGTH_LONG).show();
              return true;
         case R.id.action_settings:
              Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
              startActivity(intent);
              return true;


        }
        return  super.onOptionsItemSelected(item);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeFabIcon(final int index) {
        binding.fabAction.hide();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            switch(index) {
                case 0:
                    binding.fabAction.hide();
                    break;
                case 1:
                    binding.fabAction.show();
                    binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_chat));
                    binding.fabAction.setOnClickListener(v -> {
                        Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
                        startActivity(intent);
                    });
                    break;
                case 2:
                    binding.fabAction.show();
                    binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_camera));
                    break;
                case 3:
                    binding.fabAction.show();
                    binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_call));
                    break;
            }
        }, 400);

    }
}