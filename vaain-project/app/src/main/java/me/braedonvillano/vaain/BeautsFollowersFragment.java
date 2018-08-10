package me.braedonvillano.vaain;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BeautsFollowersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BeautsFollowersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BeautsFollowersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<ParseUser> beautsFollowers;
    RecyclerView rvBeautsFollowers;

    private OnFragmentInteractionListener mListener;

    public BeautsFollowersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BeautsFollowersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BeautsFollowersFragment newInstance(String param1, String param2) {
        BeautsFollowersFragment fragment = new BeautsFollowersFragment();
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
        View view = inflater.inflate(R.layout.fragment_beauts_followers, container, false);
        beautsFollowers = new ArrayList<>();

        rvBeautsFollowers = view.findViewById(R.id.rvBeautsFollowers);
        ClientFollowAdapter adapter = new ClientFollowAdapter(beautsFollowers);
        fetchBeauts(adapter);
        rvBeautsFollowers.setAdapter(adapter);
        rvBeautsFollowers.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void fetchBeauts(final ClientFollowAdapter adapter) {
        final List<ParseUser> clients = new ArrayList<>();
        ParseQuery<ParseUser> query = ParseUser.getQuery().include("follow");
        query.whereEqualTo("isClient",true);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null){
                    clients.addAll(objects);
                    for(int i = 0; i < clients.size();i++) {
                        final ParseUser client = clients.get(i);
                        List<ParseUser> clientsFollowing = (List<ParseUser>) client.get("follow");
                        ParseUser beaut = ParseUser.getCurrentUser();
                        //if(clientsFollowing != null && clientsFollowing.contains(beaut.toString()))beautsFollowers.add(beautsFollowers.size(),client);
                        //TODO: Make this not ugly
                        if(clientsFollowing != null){
                            for(int j = 0; j < clientsFollowing.size();j++){
                                if(clientsFollowing.get(j).getUsername().compareTo(beaut.getUsername()) == 0)beautsFollowers.add(beautsFollowers.size(),client);
                            }
                        }
                    }
                    adapter.addAll(beautsFollowers);
                    adapter.notifyDataSetChanged();
                }else{
                    e.printStackTrace();
                }
            }
        });


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onFragmentInteraction(Uri uri);
    }
}
