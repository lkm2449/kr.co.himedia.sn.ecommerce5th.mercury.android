package kr.co.himedia.ecommerce.mainproject.main.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.main.fragment.AnimalFragment;
import kr.co.himedia.ecommerce.mainproject.main.fragment.CartFragment;
import kr.co.himedia.ecommerce.mainproject.main.fragment.MainFragment;
import kr.co.himedia.ecommerce.mainproject.main.fragment.MyPageFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnimalFragment animalFragment = new AnimalFragment();
        MainFragment mainFragment = new MainFragment();
        MyPageFragment myPageFragment = new MyPageFragment();
        CartFragment cartFragment = new CartFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.tab2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.tab1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, animalFragment).commit();
                    return true;
                } else if (itemId == R.id.tab2) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
                    return true;
                } else if (itemId == R.id.tab3) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, myPageFragment).commit();
                    return true;
                } else if (itemId == R.id.tab4) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, cartFragment).commit();
                    return true;
                }

                return false;
            }
        });
    }

    public void println(String data) {
        Log.d("MainActivity", data);
    }
}