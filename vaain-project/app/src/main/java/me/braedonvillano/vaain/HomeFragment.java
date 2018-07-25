package me.braedonvillano.vaain;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment {
    //ProductsAdapter productsAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;

    private HomeFragmentInterface homeInterface;

    public interface HomeFragmentInterface {
        void onFragmentInteraction(Uri uri);
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (homeInterface != null) {
            homeInterface.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragmentInterface) {
            homeInterface = (HomeFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeInterface = null;
    }
}
