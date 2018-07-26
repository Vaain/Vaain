package me.braedonvillano.vaain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public class BeautMainActivity extends AppCompatActivity {

    // define fragments here
    final BeautsRequestsFragment beautsRequestsFragment = new BeautsRequestsFragment();
    final BusinessFragment businessFragment = new BusinessFragment();
    final Fragment beautProfileFragment = new BeautProfileFragment();

    private FragmentManager fragmentManager;

    public final static int ADD_PRODUCT_ACTIVITY = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beaut_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.beaut_bottom_navigation);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.beaut_frag_holder, beautsRequestsFragment).commit();

        // handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.action_requests:
                            changeMainFragment(beautsRequestsFragment);
                            return true;
                        case R.id.action_business:
                            changeMainFragment(businessFragment);
                            return true;
                        case R.id.action_profile:
                            changeMainFragment(beautProfileFragment);
                            return true;
                        case R.id.action_schedule:
                            changeMainFragment(beautProfileFragment);
                            return true;
                        case R.id.action_add:
                            openAddProductActivity();
                            return true;
                        default:
                            return false;
                    }
                }

            });
    }

    public void changeMainFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.beaut_frag_holder, fragment).commit();
    }

    public void openAddProductActivity() {
        // TODO: consider adding the dialogue fragment back here to clean up create flow -- a bit choppy
        Intent intent = new Intent(BeautMainActivity.this, CreateProductActivity.class);
        startActivityForResult(intent, ADD_PRODUCT_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Log.d("BeautMainActivity", "FIX THE RESULT CODE");
            return;
        }

        switch (requestCode) {
            case ADD_PRODUCT_ACTIVITY: {
                changeMainFragment(businessFragment);
                break;
            }
            default:
                Log.e("OnActivityResult", "The requestCode did not match any case!");
                break;
        }
    }
}
