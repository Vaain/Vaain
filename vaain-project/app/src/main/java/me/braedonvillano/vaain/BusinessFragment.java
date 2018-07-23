package me.braedonvillano.vaain;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Product;


public class BusinessFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // variables for products and recycle view
    private ParseUser user;
    private List<Product> productIds;
    private List<Product> products;

    private ProductAdapter productAdapter;
    private RecyclerView rvProducts;

    private BusinessFragmentInterface businessInterface;

    public interface BusinessFragmentInterface {
        void onFragmentInteraction(Uri uri);
    }

    public BusinessFragment() {
    }

    public static BusinessFragment newInstance(String param1, String param2) {
        BusinessFragment fragment = new BusinessFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business, container, false);

        user = ParseUser.getCurrentUser();
        productIds = (List<Product>) user.get("products");
        rvProducts = view.findViewById(R.id.rvProducts);
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(productIds);
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvProducts.setAdapter(productAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            }
        });

//        getProductsTranscendence();
        loadPostsNoUser();

//        addProduct(createFakeProduct("Welp!", "U Know", 39.99));

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (businessInterface != null) {
            businessInterface.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof BusinessFragmentInterface) {
//            businessInterface = (BusinessFragmentInterface) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        businessInterface = null;
    }

    /* below are the non-boilerplate functions  */

    public Product createFakeProduct(String description, String name,  Number price) {
        Product newProd = new Product();

        newProd.setDescription(description);
        newProd.setName(name);
        newProd.setPrice(price);

        return newProd;
    }

    // this is a general function that adds a product to a users list
    public void addProduct(final Product newProd) {
        newProd.setBeaut(ParseUser.getCurrentUser());
        newProd.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("BusinessFragment", "Created Product");
                    addProductToUser(newProd);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    // we need to then add the product to the users array
    public void addProductToUser(Product newProd) {
        user.add("products", newProd);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getContext(), "Product Added", Toast.LENGTH_LONG).show();
                productAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getProductsTranscendence() {
        for (int i = 0; i < productIds.size(); i++) {
            productIds.get(i).fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    Log.d("********", "they have been updated");
                    products.add((Product) object);
                    productAdapter.notifyItemInserted(products.size() - 1);
                }
            });
        }
    }

    public void loadPostsNoUser() {
        final Product.Query postQuery = new Product.Query();
        postQuery.getTop();

        postQuery.findInBackground(new FindCallback<Product>() {
            @Override
            public void done(final List<Product> objects, ParseException e) {
                if (e == null) {
//                    ParseUser user = ParseUser.getCurrentUser();
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("BusinessFragment", "Product[" + i + "] = " + objects.get(i).getDescription());
//                        user.add("products", objects.get(i));
//                        user.removeAll("products", objects);
                    }
//                    user.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e != null) return;
//                        }
//                    });
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
