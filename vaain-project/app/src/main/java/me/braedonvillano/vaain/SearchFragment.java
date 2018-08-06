package me.braedonvillano.vaain;

import android.content.Context;
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


public class SearchFragment extends Fragment implements SearchProductsAdapter.Callback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // private FragmentInteraction mListener;
    private RecyclerView rvSearch;
    private SearchProductsAdapter searchRecyclerViewAdapter;
    private RecyclerView.LayoutManager gridLayoutManager;
    private List<Product> products;

    private SearchFragmentInterface searchInterface;

    public interface SearchFragmentInterface {
        void renderRequestFlow(Product product, int code);
    }

    public SearchFragment() {
    }

    // TODO: rename and change types and number of parameters
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
        searchRecyclerViewAdapter = new SearchProductsAdapter(products, this);
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvSearch.setLayoutManager(gridLayoutManager);
        rvSearch.setAdapter(searchRecyclerViewAdapter);

        return view;
    }

    public void loadProducts() {
        final Product.Query prodQuery = new Product.Query().withBeaut();
        prodQuery.findInBackground(new FindCallback<Product>() {
            @Override
            public void done(List<Product> objects, ParseException e) {
                if (e != null) return;
                for (int i = 0; i < objects.size(); i++) {
//                    Log.d("***********", objects.get(i).toString());
                    products.add(objects.get(i));
                    searchRecyclerViewAdapter.notifyItemInserted(products.size() - 1);
                }
            }
        });
    }

    @Override
    public void onRequestProduct(Product product, int code) {
        searchInterface.renderRequestFlow(product, code);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragmentInterface) {
            searchInterface = (SearchFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        searchInterface = null;
    }
}
