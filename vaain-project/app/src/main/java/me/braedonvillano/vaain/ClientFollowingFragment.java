package me.braedonvillano.vaain;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Request;

public class ClientFollowingFragment extends Fragment implements ClientFollowAdapter.Callback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<ParseUser> beauts;
    private RecyclerView rvClientFollowing;

    private FollowingFragmentInterface followingInterface;


    public ClientFollowingFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ClientFollowingFragment newInstance(String param1, String param2) {
        ClientFollowingFragment fragment = new ClientFollowingFragment();
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
        final View view = inflater.inflate(R.layout.fragment_client_following, container, false);
        beauts = new ArrayList<>();

        rvClientFollowing = view.findViewById(R.id.rvClientFollowing);
        ClientFollowAdapter adapter = new ClientFollowAdapter(beauts,this);
        List<ParseUser> beautsIds = (List<ParseUser>) ParseUser.getCurrentUser().get("follow");
        fetchBeauts(beautsIds,adapter);
        rvClientFollowing.setAdapter(adapter);
        rvClientFollowing.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    private void fetchBeauts(final List<ParseUser> beautsIds, final ClientFollowAdapter adapter) {
        beauts.clear();
        for(int i = 0; i < beautsIds.size();i++) {
            final ParseUser beautId = beautsIds.get(i);
            beautId.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    beauts.add(beauts.size(),beautId);
                    adapter.clearAll();
                    adapter.addAll(beauts);
                    adapter.notifyItemInserted(beauts.size());
                }
            });
        }

    }

    @Override
    public void onClickBeaut(ParseUser beaut, int code) {
        followingInterface.publicProfileCallback(beaut,code);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FollowingFragmentInterface) {
            followingInterface = (FollowingFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement thingy");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        followingInterface = null;
    }

    public interface FollowingFragmentInterface {
        void publicProfileCallback(ParseUser beaut, int code);
    }



}
