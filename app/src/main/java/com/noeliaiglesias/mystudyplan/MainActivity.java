package com.noeliaiglesias.mystudyplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends FragmentActivity {
    public BottomNavigationView navigationView;
    FragmentManager fm = getSupportFragmentManager();
    private static final int NUM_PAGES=4;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



       viewPager2 = findViewById(R.id.myViewPager);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                navigationView.getMenu().getItem(position).setChecked(true);

                boolean remover=false;
                if(position >=2){
                    remover=true;
                }
               if(Boolean.TRUE.equals(remover)){
                  hideFragment();
               }else {
                   showFragment();
               }
            }
        });
       pagerAdapter = new ScreenSlidePagerAdapter(this);
       viewPager2.setAdapter(pagerAdapter);
       navigationView =  findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    @Override
    public void onBackPressed() {
        if (viewPager2.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
        }
    }


    public void aniadirAsignatura(View v){
        EditText editText = findViewById(R.id.asignatura);
        if(editText.getText().toString().isEmpty()){
            return;
        }
        SharedPreferences prefs =
                getSharedPreferences("MisAsignaturas", Context.MODE_PRIVATE);
        int numAsignatura = prefs.getInt("numAsig", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
        Map<String,?> keys = prefs.getAll();
        ArrayList<String> items=new ArrayList<>();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            if(!entry.getKey().equals("numAsig")){
                items.add(entry.getValue().toString());
            }
        }
       if(!items.contains(editText.getText().toString())){
           editor.putString("asignatura"+numAsignatura, editText.getText().toString());
           numAsignatura++;
           editor.putInt("numAsig", numAsignatura);
           editor.apply();
       }else{
           Toast.makeText(this, "La asignatura ya está añadida", Toast.LENGTH_LONG).show();
       }
        editText.setText("");

    }
    private void showFragment() {
        Objects.requireNonNull(fm.findFragmentById(R.id.fragment_container)).requireView().requestLayout();
        Objects.requireNonNull(fm.findFragmentById(R.id.fragment_container)).requireView().getLayoutParams().height=LinearLayout.LayoutParams.WRAP_CONTENT;
       Objects.requireNonNull(fm.findFragmentById(R.id.fragment_container)).requireView().setVisibility(View.VISIBLE);
    }


    public List<Fragment> getVisibleFragment(){
        List<Fragment> fragmentList = fm.getFragments();
        List<Fragment> fragmentsVisible= new ArrayList<>();
        for(Fragment fragment: fragmentList){
            if(fragment!=null && fragment.isVisible()){
                fragmentsVisible.add(fragment);
            }
        }
        return fragmentsVisible;
    }

    private void hideFragment(){
        Objects.requireNonNull(fm.findFragmentById(R.id.fragment_container)).requireView().requestLayout();
        Objects.requireNonNull(fm.findFragmentById(R.id.fragment_container)).requireView().getLayoutParams().height=0;
        Objects.requireNonNull(fm.findFragmentById(R.id.fragment_container)).requireView().setVisibility(View.INVISIBLE);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment f = null;

            switch (item.getItemId()) {
                case R.id.navigation_temas:
                    showFragment();
                    f = new StudyFragment.StudyList();
                    break;

                case R.id.navigation_repasos:
                    showFragment();
                    f = new RepasoFragment();
                    break;
                case R.id.navigation_examenes:
                    hideFragment();
                    f = new CalendarioFragment();

                    break;
                case R.id.navigation_config:
                    hideFragment();
                    f= new ConfigFragment();
                    break;
            }

            viewPager2.setCurrentItem(item.getOrder());
            return true;
        }

    };

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position){
                case 0:
                    return new StudyFragment.StudyList();

                case 1:
                    return new RepasoFragment();

                case 2:
                    return new CalendarioFragment();

                case 3:
                    return new ConfigFragment();

            }
          return new Fragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

}
