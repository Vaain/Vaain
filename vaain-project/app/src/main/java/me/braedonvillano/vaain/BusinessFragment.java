package me.braedonvillano.vaain;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Location;
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

    Switch swSun;
    Switch swMon;
    Switch swTues;
    Switch swWed;
    Switch swThurs;
    Switch swFri;
    Switch swSat;

    Button btnSun;
    Button btnMon;
    Button btnTues;
    Button btnWed;
    Button btnThurs;
    Button btnFri;
    Button btnSat;

    private List<Location> locations;
    private List<String> locNames;
    private Spinner spLocation1;
    private Spinner spLocation2;
    private Spinner spLocation3;
    ArrayAdapter<String> adapter;





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

        //Attach products to recycler view
        user = ParseUser.getCurrentUser();
        productIds = (List<Product>) user.get("products");
        rvProducts = view.findViewById(R.id.rvProducts);
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(productIds);
        rvProducts.setAdapter(productAdapter);

        // initialize other views
        swSun = view.findViewById(R.id.swSun);
        swMon = view.findViewById(R.id.swMon);
        swTues = view.findViewById(R.id.swTues);
        swWed = view.findViewById(R.id.swWed);
        swThurs = view.findViewById(R.id.swThurs);
        swFri = view.findViewById(R.id.swFri);
        swSat = view.findViewById(R.id.swSat);

        btnSun = view.findViewById(R.id.btnSun);
        btnMon = view.findViewById(R.id.btnMon);
        btnTues = view.findViewById(R.id.btnTues);
        btnWed = view.findViewById(R.id.btnWed);
        btnThurs = view.findViewById(R.id.btnThurs);
        btnFri = view.findViewById(R.id.btnFri);
        btnSat = view.findViewById(R.id.btnSat);




        swSun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchChange(btnSun, b);
            }
        });

        swMon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchChange(btnMon, b);
            }
        });
        swTues.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchChange(btnTues, b);
            }
        });
        swWed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchChange(btnWed, b);
            }
        });
        swThurs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchChange(btnThurs, b);
            }
        });
        swFri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchChange(btnFri, b);
            }
        });
        swFri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchChange(btnFri, b);
            }
        });
        swSat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchChange(btnSat, b);
            }
        });

        locations = new ArrayList<>();
        locNames = new ArrayList<>();
        locNames.add("");



        ParseQuery<ParseUser> query2 = ParseUser.getQuery();
        query2.include("loc1");
        query2.include("loc2");
        query2.include("loc3");
        try {
            user = query2.get(ParseUser.getCurrentUser().getObjectId());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void switchChange(Button button, boolean state){
        Drawable buttonPurple = getResources().getDrawable(R.drawable.rounded_button_purple);
        if(state){
          button.setBackground(buttonPurple);
        }else{
            Drawable buttonGrey = getResources().getDrawable(R.drawable.rounded_button_grey);
            button.setBackground(buttonGrey);
        }
    }


    public void onButtonPressed(Uri uri) {
        if (businessInterface != null) {
            businessInterface.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        businessInterface = null;
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("Settings","preferences updates");
            }
        });
    }

    /* this will be used when the CreateProductActivity TODOs are completed  */
    public void addProductToAdapter(Product newProduct) {
        products.add(newProduct);
        productAdapter.notifyItemInserted(products.size() - 1);
    }

}
