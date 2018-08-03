package me.braedonvillano.vaain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class BeautProfileFragment extends Fragment {

    ParseImageView ivProfileImage;
    TextView tvName;
    TextView tvEmail;
    Button btnLogout;
    ParseUser user;

    public BeautProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beaut_profile, container, false);

        user = ParseUser.getCurrentUser();

        //attach views variables
        tvEmail = view.findViewById(R.id.tvEmail);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        btnLogout = view.findViewById(R.id.btnLogout);
        tvName = view.findViewById(R.id.tvName);





        //assign values to views
        tvEmail.setText(user.getEmail());
        tvName.setText(user.getString("Name"));
        //assign profileImage
        if(user.get("profileImage") != null){
            ParseFile file = user.getParseFile("profileImage");
            Glide.with(this).load(file.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivProfileImage);
            ivProfileImage.loadInBackground();
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);



        return view;
    }

    private void setupViewPager(ViewPager viewPager) {


        ProfileFragment.Adapter adapter = new ProfileFragment.Adapter(getChildFragmentManager());
        //TODO create the actual frags and change to these
        adapter.addFragment(new ClientHistoryFragment(), "Payment");
        adapter.addFragment(new ClientHistoryFragment(), "History");
        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void logout() {
        ParseUser.logOut();
        assert(ParseUser.getCurrentUser() == null);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
