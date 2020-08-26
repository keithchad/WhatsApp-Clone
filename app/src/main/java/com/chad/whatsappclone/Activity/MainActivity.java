package com.chad.whatsappclone.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.chad.whatsappclone.Fragments.CallsFragment;
import com.chad.whatsappclone.Fragments.ChatsFragment;
import com.chad.whatsappclone.Fragments.StatusFragment;
import com.chad.whatsappclone.R;
import com.chad.whatsappclone.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setUpWithViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        setSupportActionBar(binding.toolbar);

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

        MainActivity.SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new ChatsFragment(), "Chats");
        adapter.addFragment(new StatusFragment(), "Status");
        adapter.addFragment(new CallsFragment(), "Calls");


        viewPager.setAdapter(adapter);

    }

    private static class SectionPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionPagerAdapter(@NonNull FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
              Toast.makeText(MainActivity.this, "Settings Selected", Toast.LENGTH_LONG).show();
              return true;


        }
        return  super.onOptionsItemSelected(item);
    }

    private void changeFabIcon(final int index) {
        binding.fabAction.hide();

        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                switch(index) {
                    case 0:
                        binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_chat));
                        break;
                    case 1:
                        binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_camera));
                        break;
                    case 2:
                        binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_call));
                        break;
                }

                binding.fabAction.show();
            }
        }, 400);
    }
}