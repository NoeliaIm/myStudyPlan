package com.noeliaiglesias.mystudyplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    private void showFragment(Fragment frg) {
        if( frg == null){
            frg = new StudyFragment();
            fm.beginTransaction().add(R.id.fragment_container, frg).commit();
        }
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

    private void hideFragment(Fragment frg){
        if(frg != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(frg)
                    .commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment f = null;
            Fragment fragment = fm.findFragmentById(R.id.fragment_container);

            switch (item.getItemId()) {
                case R.id.navigation_temas:
                    showFragment(fragment);
                    f = new StudyFragment.StudyList();
                    break;

                case R.id.navigation_repasos:
                    showFragment(fragment);
                    f = new RepasoFragment();
                    break;
                case R.id.navigation_examenes:
                    hideFragment(fragment);
                    f = new CalendarioFragment();
                    break;
                case R.id.navigation_config:
                    hideFragment(fragment);
                    f= new ConfigFragment();
                    break;
            }

            if(f!=null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_recycler,f)
                        .setPrimaryNavigationFragment(f)
                        .commit();
                return true;
            }

            return false;
        }

    };
}
