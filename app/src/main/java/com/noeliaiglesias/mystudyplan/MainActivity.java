package com.noeliaiglesias.mystudyplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {
    public BottomNavigationView navigationView;
    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if( fragment == null){
            fragment = new StudyFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }


       navigationView =  findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }




    public void aniadirAsignatura(View v){
        SharedPreferences prefs =
                getSharedPreferences("MisAsignaturas", Context.MODE_PRIVATE);
        int numAsignatura = prefs.getInt("numAsig", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
        EditText editText = findViewById(R.id.asignatura);
        editor.putString("asignatura"+numAsignatura, editText.getText().toString());
        numAsignatura++;
        editor.putInt("numAsig", numAsignatura);
        editor.commit();
        editText.setText("");
    }
    private void showFragment(Fragment frg) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, frg)
                .commit();

    }

    public List<Fragment> getVisibleFragment(){
        List<Fragment> fragmentList = fm.getFragments();
        List<Fragment> fragmentsVisible= new ArrayList<>();
        if(fragmentList!=null){
            for(Fragment fragment: fragmentList){
                if(fragment!=null && fragment.isVisible()){
                    fragmentsVisible.add(fragment);
                }
            }
        }
        return fragmentsVisible;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment f = null;

            switch (item.getItemId()) {
                case R.id.navigation_temas:
                    f = new StudyFragment.StudyList();
                    break;

                case R.id.navigation_repasos:
                    f = new RepasoFragment();
                    break;
                case R.id.navigation_examenes:
                    f = new CalendarioFragment();
                    break;
            }

            if(f!=null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_recycler,f)
                        .commit();

                return true;
            }

            return false;
        }

    };
}
