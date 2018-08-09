package me.braedonvillano.vaain;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.parse.ParseUser;

import me.braedonvillano.vaain.models.Product;

public class MainActivity extends AppCompatActivity implements SearchFragment.SearchFragmentInterface,ClientFollowingFragment.FollowingFragmentInterface,PublicBeautProfile.OnFragmentInteractionListener,ProfileFragment.OnFragmentInteractionListener {

    private FragmentManager fragmentManager;

    final ProfileFragment profileFragment = new ProfileFragment();
    final SearchFragment searchFragment = new SearchFragment();
    final ClientRequestsFragment requestFragment = new ClientRequestsFragment();
    final ClientAccountFragment clientAccountFragment = new ClientAccountFragment();
    final PublicBeautProfile beautProfile2 = new PublicBeautProfile();
    final ClientSettingsFragment clientSettingsFragment = new ClientSettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        loadInitialFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_profile:
                                changeMainFragment(profileFragment);
                                return true;
                            case R.id.action_search:
                                changeMainFragment(searchFragment);
                                return true;
                            case R.id.action_requests:
                                changeMainFragment(clientAccountFragment);
                                return true;
                        }
                        return false;
                    }

                });
    }

    private void loadInitialFragment()
    {
        Fragment initialFragment = searchFragment;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_placeholder, initialFragment);
        fragmentTransaction.commit();
    }

    public void changeMainFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_placeholder, fragment).commit();
    }

    @Override
    public void renderRequestFlow(Product product, int code) {
        if(code == SearchProductsAdapter.REQUEST_CODE) {
            requestFragment.setProduct(product);
            changeMainFragment(requestFragment);
        }else if(code == SearchProductsAdapter.PROFILE_CODE){
            beautProfile2.setUser(product.getBeaut());
            changeMainFragment(beautProfile2);
        }
    }


    @Override
    public void publicProfileCallback(ParseUser beaut, int code) {
            beautProfile2.setUser(beaut);
            changeMainFragment(beautProfile2);


    }


    @Override
    public void onProductClick(Product product, int code) {
        renderRequestFlow(product,code);
    }

    @Override
    public void onFragmentInteraction(ParseUser beaut, int code) {
        changeMainFragment(clientSettingsFragment);

    }
}