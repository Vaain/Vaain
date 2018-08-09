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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Appointment;


public class BeautsAppointmentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Appointment> appts;
    private RecyclerView rvAppointments;

    private OnFragmentInteractionListener mListener;

    public BeautsAppointmentsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BeautsAppointmentsFragment newInstance(String param1, String param2) {
        BeautsAppointmentsFragment fragment = new BeautsAppointmentsFragment();
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
        View view = inflater.inflate(R.layout.fragment_beauts_appointments, container, false);
        appts = new ArrayList<>();

        rvAppointments = view.findViewById(R.id.rvAppointments);
        BeautHistoryAdapter adapter = new BeautHistoryAdapter(appts);
        rvAppointments.setAdapter(adapter);
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
        getAppointments(adapter);
        return view;
    }


    void getAppointments(final BeautHistoryAdapter adapter){
        ParseQuery<Appointment> query1 = new Appointment.Query().withBeaut().withClient().withProduct();
        query1.whereEqualTo("isComplete",false);
        query1.findInBackground(new FindCallback<Appointment>() {
            @Override
            public void done(List<Appointment> objects, ParseException e) {
                if(e == null) {
                    appts.addAll(objects);
                    int size = appts.size();
                    adapter.clear();
                    adapter.addAll(appts);
                    adapter.notifyItemRangeInserted(0,size);
                    Log.d("BeautsHistoryFragment", "Retrieved " + size + "fetched appointments");
                }else {
                    e.printStackTrace();
                }
            }
        });

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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
