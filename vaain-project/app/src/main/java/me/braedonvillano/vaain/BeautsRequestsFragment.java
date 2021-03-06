package me.braedonvillano.vaain;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Request;


public class BeautsRequestsFragment extends Fragment implements  BeautRequestsAdapter.Callback, BeautRequestDetail.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    RecyclerView rvRequests;
    List<Request> requests;

    private RequestsFragmentInterface requestsInterface;

    @Override
    public void onFragmentInteraction(Request request, int code) {
        onDetail(request,code);
    }

    public interface RequestsFragmentInterface {
        void onFragmentInteraction(Request request, int code);
    }



    public BeautsRequestsFragment() {
    }

    public static BeautsRequestsFragment newInstance(String param1, String param2) {
        BeautsRequestsFragment fragment = new BeautsRequestsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_beaut_requests, container, false);
        requests = new ArrayList<>();

        rvRequests = view.findViewById(R.id.rvRequests);
        BeautRequestsAdapter adapter = new BeautRequestsAdapter(requests, this);
        getParseRequests(adapter);
        rvRequests.setAdapter(adapter);
        rvRequests.setLayoutManager(new LinearLayoutManager(getContext()));



        return view;
    }


    @Override
    public void onDetail(Request request, int code) {
        if (requestsInterface != null) {
            requestsInterface.onFragmentInteraction(request, code );
        }
    }

    void getParseRequests(final BeautRequestsAdapter adapter) {
        ParseQuery<Request> requestQuery = new Request.Query().withBeaut().withClient().withProduct();
        requestQuery.whereEqualTo("beaut", ParseUser.getCurrentUser());
        requestQuery.findInBackground(new FindCallback<Request>() {
            @Override
            public void done(List<Request> objects, com.parse.ParseException e) {

                if (e == null) {
                    requests = objects;
                    int size = objects.size();
                    adapter.clear();
                    adapter.addAll(requests);
                    adapter.notifyItemRangeInserted(0,size);
                    Log.d("BeautsRequestsFragment", "Retrieved " + size + " requests");
                } else {
                    Log.d("BeautsRequestsFragment", "Error: " + e.getMessage());
                }
            }
        });

    }

//    void getPQ(){
//        Log.d("****","Check");
//        ParseQuery<Request> pQuery = new Request.Query();
//        pQuery.findInBackground(new FindCallback<Request>() {
//            @Override
//            public void done(List<Request> objects, com.parse.ParseException e) {
//                Log.d("****","WORK");
//            }
//        });
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RequestsFragmentInterface) {
            requestsInterface = (RequestsFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        requestsInterface = null;
    }
}
