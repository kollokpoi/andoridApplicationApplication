package com.example.bottom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.bottom.fragments.AchieveFragment;
import com.example.bottom.fragments.ChatFragment;
import com.example.bottom.fragments.SettingsFragment;
import com.example.bottom.models.ApartmentStatus;
import com.example.bottom.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //messageModes
    public static final int SELF_MESSAGE =0;
    public static final int DIALOG_MESSAGE =1;

    //userItemModes
    public static final int MAIN_CHAT = 0;
    public static final int GROUP_CHAT = 1;
    public static final int PRIVATE_CHAT = 2;

    public static final int CHAT_FRAGMENT = 0;
    public static final int CHAT_ACTIVITY = 2;

    public static final List<ApartmentStatus> apartmentStatuses = new ArrayList();
    public static User appUser;
    public static String userToken="";
    public static final String BASE_URL="http://192.168.1.4:5167/api/";

    private static final Fragment chatFragment = new ChatFragment();
    private static final Fragment achieveFragment = new AchieveFragment();
    private static final Fragment settingsFragment = new SettingsFragment();

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = userToken;
        User user = appUser;

        apartmentStatuses.add(new ApartmentStatus(0,"Не построено"));
        apartmentStatuses.add(new ApartmentStatus(1,"Строится"));
        apartmentStatuses.add(new ApartmentStatus(2,"Построено"));
        apartmentStatuses.add(new ApartmentStatus(3,"Принято"));
        apartmentStatuses.add(new ApartmentStatus(4,"Замечания"));

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.view_pager);

        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(chatFragment);
        fragmentList.add(achieveFragment);
        fragmentList.add(settingsFragment);

        pagerAdapter = new com.example.bottom.adapters.PagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                // Обновление выбранного элемента нижнего меню при смене страницы
                updateToolbarTitle(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        // Установка слушателя событий для нижнего меню навигации
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int position = 0;
            switch (item.getItemId()) {
                case R.id.chat_item:
                    position = 0;
                    break;
                case R.id.achiev_item:
                    position = 1;
                    break;
                case R.id.settings_item:
                    position = 2;
                    break;
            }
            viewPager.setCurrentItem(position);
            return true;
        });
        // Установите начальную страницу
        viewPager.setCurrentItem(0);
    }


    private void updateToolbarTitle(int position) {
        Toolbar toolbar = findViewById(R.id.maintoolbar);
        if (toolbar != null) {
            String title = (String) bottomNavigationView.getMenu().getItem(position).getTitle();
            toolbar.setTitle(title);
        }
    }
}