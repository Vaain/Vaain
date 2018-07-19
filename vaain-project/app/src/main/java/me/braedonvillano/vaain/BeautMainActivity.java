package me.braedonvillano.vaain;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class BeautMainActivity extends AppCompatActivity {

    // define your fragments here
    final RequestsFragment requestsFragment = new RequestsFragment();
    final BusinessFragment businessFragment = new BusinessFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beaut_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.beaut_bottom_navigation);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.beauts_frag_placeholder, requestsFragment).commit();

        // handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    switch (item.getItemId()) {
                        case R.id.action_requests:
                            fragmentTransaction.replace(R.id.beauts_frag_placeholder, requestsFragment).commit();
                            return true;
                        case R.id.action_business:
                            fragmentTransaction.replace(R.id.beauts_frag_placeholder, businessFragment).commit();
                            return true;
                        default:
                            return false;
                    }
                }

            });
    }
}
