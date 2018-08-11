package me.braedonvillano.vaain;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.braedonvillano.vaain.models.Product;

import static me.braedonvillano.vaain.WorkSchedules.getAllTags;


public class SearchFragment extends Fragment implements SearchProductsAdapter.Callback {
    // private FragmentInteraction mListener;
    public static RecyclerView rvSearch;
    private static SearchProductsAdapter searchRecyclerViewAdapter;
    private RecyclerView.LayoutManager gridLayoutManager;
    private List<Product> products;
    private List<Product> mProducts;
    private Button btnFilter;
    private Dialog filterDialog;
    private ArrayList<String> allFilterTags;
    private ArrayAdapter<String> filterAdapter;
    private SearchFragmentInterface searchInterface;

//    public ArrayList<String> filteredTags;
//    public ArrayList<Product> filteredProducts;

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
        mProducts = new ArrayList<>();
        allFilterTags = new ArrayList<>();
        allFilterTags.addAll(Arrays.asList(getAllTags()));
        loadProducts();
        btnFilter = view.findViewById(R.id.btnFilter);
        rvSearch = view.findViewById(R.id.rvSearch);
        searchRecyclerViewAdapter = new SearchProductsAdapter(products, this);
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvSearch.setLayoutManager(gridLayoutManager);
        rvSearch.setAdapter(searchRecyclerViewAdapter);

        btnFilter.setOnClickListener(new OpenFilterList());

        return view;
    }

    public void loadProducts() {
        final Product.Query prodQuery = new Product.Query().getTop().withBeaut();
        prodQuery.include("Name");
        prodQuery.include("beaut");
        prodQuery.include("tagList");
        prodQuery.findInBackground(new FindCallback<Product>() {
            @Override
            public void done(List<Product> objects, ParseException e) {
                if (e != null) return;
                for (int i = 0; i < objects.size(); i++) {
//                    Log.d("***********", objects.get(i).toString());
                    products.add(objects.get(i));
                    mProducts.add(objects.get(i));
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
                searchRecyclerViewAdapter.getFilter().filter(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(final String newText) {
                searchRecyclerViewAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    class OpenFilterList implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // open the dialog list to filter items
            final ArrayList<String> filteredTags = new ArrayList<>();
            final ArrayList<Product> filteredProducts = new ArrayList<>();

            filterDialog = new Dialog(getContext());
            filterDialog.setContentView(R.layout.filter_dialog);
            filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            Button applyFilter =  filterDialog.findViewById(R.id.btnApplyFilter);
            ListView lvFilterTags = filterDialog.findViewById(R.id.lvFilter);
            filterAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, allFilterTags);
            lvFilterTags.setAdapter(filterAdapter);
//            lvFilterTags.setBackgroundColor(Color.CYAN);


            lvFilterTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    // TODO: determine whether item is selected and handle accordingly
                    filteredTags.add(String.valueOf(parent.getItemAtPosition(i)));
                }
            });
//
            applyFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    applyFilter(filteredProducts, filteredTags);
                    filterDialog.dismiss();
                }
            });

            filterDialog.show();
        }
    }

    // call this function to clear the filter
    public void clearFilter() {
        // TODO: reset filter fields once cleared
        searchRecyclerViewAdapter.clear();
        searchRecyclerViewAdapter.addAll(mProducts);
    }

    public void applyFilter(ArrayList<Product> filtProd, ArrayList<String> filtTags) {
        boolean addToList = true;
        for (Product product : mProducts) {
            ArrayList<String> prodTags = product.getTags();
            for (String tag : filtTags) {
                if (prodTags == null) {
                    addToList = false;
                    break;
                }
                if (!prodTags.contains(tag)) {
                    addToList = false;
                    break;
                }
            }
            if (addToList) {
                filtProd.add(product);
//                products.removeAll(Arrays.asList(product));
//                searchRecyclerViewAdapter.notifyDataSetChanged();

            }
            addToList = true;
        }
//        products = filtProd;
        searchRecyclerViewAdapter.clear();
        searchRecyclerViewAdapter.addAll(filtProd);
        filtProd.clear();
//        searchRecyclerViewAdapter.notifyDataSetChanged();
    }
}