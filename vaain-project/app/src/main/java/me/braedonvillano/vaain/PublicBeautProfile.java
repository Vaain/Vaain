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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Product;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicBeautProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicBeautProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicBeautProfile extends Fragment implements BeautsProductsAdapter.Callback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private List<Product> products;
    private BeautsProductsAdapter beautsProductAdapter;
    private RecyclerView.LayoutManager gridLayoutManager;
    private RecyclerView rvBeautsProducts;
    private ParseUser user;

    private ParseImageView ivProfileImage;
    private TextView tvName;
    private TextView tvEmail;
    private Button btnFollow;
    List<ParseUser> followedBeauts;

    public PublicBeautProfile() {
        // Required empty public constructor
    }

    public void setUser(ParseUser newUser){
        user = newUser;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublicBeautProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static PublicBeautProfile newInstance(String param1, String param2) {
        PublicBeautProfile fragment = new PublicBeautProfile();
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

        View view = inflater.inflate(R.layout.fragment_public_beaut_profile, container, false);
        products = new ArrayList<>();
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        btnFollow = view.findViewById(R.id.btnFollow);

        followedBeauts = ParseUser.getCurrentUser().getList("follow");
        if(followedBeauts.contains(user)){
            btnFollow.setBackground(getResources().getDrawable(R.drawable.rounded_button_purple));
            btnFollow.setText("Followed");
        }

        tvName.setText(user.getString("Name"));
        tvEmail.setText(user.getUsername());

        if(user.get("profileImage") != null){
            ParseFile file = user.getParseFile("profileImage");
            Glide.with(this).load(file.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivProfileImage);
            ivProfileImage.loadInBackground();
        }
        loadProducts();
        rvBeautsProducts = (RecyclerView) view.findViewById(R.id.rvBeautsProducts);

        beautsProductAdapter = new BeautsProductsAdapter(products,this);
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvBeautsProducts.setLayoutManager(gridLayoutManager);
        rvBeautsProducts.setAdapter(beautsProductAdapter);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser client = ParseUser.getCurrentUser();
                if(followedBeauts.contains(user)){
                    List<ParseUser> removeBeauts = new ArrayList<>();
                    removeBeauts.add(0,user);
                    client.removeAll("follow",removeBeauts);
                    btnFollow.setBackground(getResources().getDrawable(R.drawable.rounded_button_grey));
                    btnFollow.setText("Follow");

                }else {
                    client.addUnique("follow", user);
                    btnFollow.setBackground(getResources().getDrawable(R.drawable.rounded_button_purple));
                    btnFollow.setText("Followed");
                }
                client.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("Follow", "Followers updates");
                        } else {
                            // Error saving object, print the logs
                            e.printStackTrace();
                        }

                    }
                });
            }

        });

        return view;
    }


    public void loadProducts() {
        final Product.Query prodQuery = new Product.Query().withBeaut();
        prodQuery.whereEqualTo("beaut",user);
        prodQuery.findInBackground(new FindCallback<Product>() {
            @Override
            public void done(List<Product> objects, ParseException e) {
                if (e != null) return;
                for (int i = 0; i < objects.size(); i++) {
                    Log.d("***********", objects.get(i).toString());
                    products.add(objects.get(i));
                    beautsProductAdapter.notifyItemInserted(products.size() - 1);
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement thingy");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRequestProduct(Product product, int code) {
        mListener.onProductClick(product,code);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onProductClick(Product product, int code);
    }
}
