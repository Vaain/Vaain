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

import me.braedonvillano.vaain.models.Request;

public class BeautMainActivity extends AppCompatActivity implements BeautsRequestsFragment.RequestsFragmentInterface ,BeautRequestDetail.OnFragmentInteractionListener,BeautProfileFragment.Callback{

    // define fragments here
    final BeautApptRequestFragment beautApptRequestFragment = new BeautApptRequestFragment();
    final BusinessFragment businessFragment = new BusinessFragment();
    final Fragment beautProfileFragment = new BeautProfileFragment();
    final BeautRequestDetail beautRequestDetail = new BeautRequestDetail();
    final ClientSettingsFragment beautSettingsFragment = new ClientSettingsFragment();

    private FragmentManager fragmentManager;

    public final static int ADD_PRODUCT_ACTIVITY = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beaut_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.beaut_bottom_navigation);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.beaut_frag_holder, beautApptRequestFragment).commit();

        // handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.action_requests:
                            changeMainFragment(beautApptRequestFragment);
                            return true;
                        case R.id.action_profile:
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

    @Override
    public void onFragmentInteraction(Request request, int code) {
        if(code == BeautRequestsAdapter.REQUEST_CODE) {
            beautRequestDetail.setRequest(request);
            changeMainFragment(beautRequestDetail);
        }else if(code == BeautRequestDetail.LOCATION_CODE){
//            alertDialog.setRequest(request);
//            alertDialog.show(fragmentManager, "fragment_alert");
             changeMainFragment(beautApptRequestFragment);


        }
    }

    @Override
    public void settingsCallback() {
        changeMainFragment(beautSettingsFragment);
    }
}
