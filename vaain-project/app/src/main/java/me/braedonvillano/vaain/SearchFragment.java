package me.braedonvillano.vaain;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Product;


public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private FragmentInteraction mListener;
    private RecyclerView rvSearch;
    private searchRecyclerViewAdapter searchRecyclerViewAdapter;
    private RecyclerView.LayoutManager gridLayoutManager;
    private List<Product> products;

    private SearchFragmentInterface searchInterface;

    public interface SearchFragmentInterface {
        void onFragmentInteraction(Uri uri);
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        products = new ArrayList<>();

        loadProducts();
        rvSearch = view.findViewById(R.id.rvSearch);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);
        rvSearch.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvSearch.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        searchRecyclerViewAdapter = new searchRecyclerViewAdapter(products);
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvSearch.setLayoutManager(gridLayoutManager);
        rvSearch.setAdapter(searchRecyclerViewAdapter);


        loadTopPosts();
        return view;
    }

    public void loadProducts() {
        final Product.Query prodQuery = new Product.Query();

        prodQuery.include("beaut");
        prodQuery.findInBackground(new FindCallback<Product>() {
            @Override
            public void done(List<Product> objects, ParseException e) {
                if (e != null) return;
                for (int i = 0; i < objects.size(); i++) {
                    Log.d("***********", objects.get(i).toString());
                    products.add(objects.get(i));
                    searchRecyclerViewAdapter.notifyItemInserted(products.size() - 1);
                }
            }
        });
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (searchInterface != null) {
            searchInterface.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        searchInterface = null;
    }

    public void loadTopPosts() {


        final Product.Query postQuery = new Product.Query();
        postQuery.getTop();

        postQuery.findInBackground(new FindCallback<Product>() {
            @Override
            public void done(final List<Product> objects, ParseException e) {
                if (e == null) {
               //     ParseUser user = ParseUser.getCurrentUser();
                    //for (int i = 0; i < objects.size(); i++) {
                    //Log.d("BusinessFragment", "Product[" + i + "] = " + objects.get(i).getDescription());
//                        user.add("products", objects.get(i));
//                        user.removeAll("products", objects);

                 //   user.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e != null) return;
//                        }
                    //});
                } else {
                    e.printStackTrace();
                }
            }
        });
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */



}
