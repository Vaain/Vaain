package me.braedonvillano.vaain;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Product;


public class SearchFragment extends Fragment implements SearchProductsAdapter.Callback {
    // private FragmentInteraction mListener;
    public static RecyclerView rvSearch;
    private static SearchProductsAdapter searchRecyclerViewAdapter;
    private RecyclerView.LayoutManager gridLayoutManager;
    private List<Product> products;
    private SearchFragmentInterface searchInterface;

    public interface SearchFragmentInterface {
        void renderRequestFlow(Product product, int code);
    }

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        products = new ArrayList<>();
        loadProducts();
        rvSearch = view.findViewById(R.id.rvSearch);
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);
//        rvSearch.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
//        rvSearch.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        searchRecyclerViewAdapter = new SearchProductsAdapter(products, this);
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvSearch.setLayoutManager(gridLayoutManager);
        rvSearch.setAdapter(searchRecyclerViewAdapter);

        return view;

    }

    public void loadProducts() {
        final Product.Query prodQuery = new Product.Query().withBeaut();
        prodQuery.include("Name");
        prodQuery.include("beaut");
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

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SearchView searchView = view.findViewById(R.id.sv_search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()  {
            @Override
            public boolean onQueryTextSubmit(final String query) {
//                loadSpecificProduct(query);
                final Product.Query prodQuery = new Product.Query().withBeaut();
                prodQuery.include("beaut");
                prodQuery.findInBackground(new FindCallback<Product>() {
                    @Override
                    public void done(List<Product> objects, ParseException e) {
                        if (e != null) return;
                        products.clear();
                        for (int i = 0; i < objects.size(); i++) {
                            Product product = objects.get(i);
                            if (product.getName() != null) {
                                String name = product.getName().toLowerCase();
                                String firstChar = String.valueOf(name.charAt(0));
                                if (firstChar.contains(query)) {
                                    products.add(objects.get(i));
                                }
                            }
                        }
                        searchRecyclerViewAdapter.notifyDataSetChanged();
                        searchView.clearFocus();
                    }
                });
                return true;
            }
            @Override
            public boolean onQueryTextChange(final String newText) {
                final Product.Query prodQuery = new Product.Query().withBeaut();
                prodQuery.include("beaut");
                prodQuery.findInBackground(new FindCallback<Product>() {
                    @Override
                    public void done(List<Product> objects, ParseException e) {
                        if (e != null) return;
                        products.clear();
                        for (int i = 0; i < objects.size(); i++) {
                                if (newText.isEmpty()) {
                                    products.add(objects.get(i));
                                }
                            }
                        searchRecyclerViewAdapter.notifyDataSetChanged();
                        searchView.clearFocus();
                        }
                });
                return true;
            }
        });
    }
}