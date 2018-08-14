package me.braedonvillano.vaain;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import me.braedonvillano.vaain.models.Request;

public class BeautMainActivity extends AppCompatActivity implements BeautsRequestsFragment.RequestsFragmentInterface ,BeautRequestDetail.OnFragmentInteractionListener,BeautProfileFragment.Callback{

    // define fragments here
    final BeautApptRequestFragment beautApptRequestFragment = new BeautApptRequestFragment();
    final BusinessFragment businessFragment = new BusinessFragment();
    final Fragment beautProfileFragment = new BeautProfileFragment();
    final BeautRequestDetail beautRequestDetail = new BeautRequestDetail();
    final ClientSettingsFragment beautSettingsFragment = new ClientSettingsFragment();

    private FragmentManager fragmentManager;
    private Dialog createDialog;

    public final static int ADD_PRODUCT_ACTIVITY = 1234;
    public final static int ADD_POST_ACTIVITY = 2345;

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
                            openPostProductDialog();
                            return true;
                        default:
                            return false;
                    }
                }

            });

    }

    public void changeMainFragment(Fragment fragment) {
        // TODO: make sure that the bottom nav has the right fragment always (switch statement?)
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.beaut_frag_holder, fragment).commit();
    }

    public void openPostProductDialog() {
        createDialog = new Dialog(this);
        createDialog.setContentView(R.layout.create_content_dialog);
        createDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button openPost =  createDialog.findViewById(R.id.btnModalPost);
        Button openProduct =  createDialog.findViewById(R.id.btnModalProduct);
        Button closeModal = createDialog.findViewById(R.id.btnCloseModal);

        Window window = createDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        openPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog.dismiss();
                openAddPostActivity();
            }
        });

        openProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog.dismiss();
                openAddProductActivity();
            }
        });

        closeModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog.dismiss();
            }
        });

        createDialog.show();
    }

    public void openPictureVideoModal(String postType) {
        // TODO: handle this function if video is added
    }

    public void openAddProductActivity() {
        // TODO: consider adding the dialogue fragment back here to clean up create flow -- a bit choppy
        Intent intent = new Intent(BeautMainActivity.this, CreateProductActivity.class);
        startActivityForResult(intent, ADD_PRODUCT_ACTIVITY);
    }

    public void openAddPostActivity() {
        Intent intent = new Intent(BeautMainActivity.this, CreatePostActivity.class);
        startActivityForResult(intent, ADD_POST_ACTIVITY);
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
