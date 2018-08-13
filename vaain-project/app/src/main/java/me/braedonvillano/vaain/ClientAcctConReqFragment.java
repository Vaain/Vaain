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
import com.parse.ParseQuery;

import java.util.List;

import me.braedonvillano.vaain.models.Appointment;

public class ClientAcctConReqFragment extends Fragment {
    RecyclerView rvReqCon;

    private OnFragmentInteractionListener mListener;

    public ClientAcctConReqFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view = inflater.inflate(R.layout.fragment_client_acct_con_req, container, false);

        ParseQuery<Appointment> appointmentParseQuery = new Appointment.Query().withBeaut().withClient();
        //requestQuery.whereEqualTo("beaut", ParseUser.getCurrentUser());
        appointmentParseQuery.include("product");
        appointmentParseQuery.include("beaut");
        appointmentParseQuery.whereEqualTo("isComplete", false);
        appointmentParseQuery.findInBackground(new FindCallback<Appointment>() {
            @Override
            public void done(List<Appointment> objects, com.parse.ParseException e) {

                if (e == null) {
                    //requests = objects;
                    int size = objects.size();
                    rvReqCon = view.findViewById(R.id.rvReqCon);
                    ClientAccountAdapterAppoint adapter = new ClientAccountAdapterAppoint(objects);
                    rvReqCon.setAdapter(adapter);
                    rvReqCon.setLayoutManager(new LinearLayoutManager(getContext()));



                } else {}
            }
        });
        return view;
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
            mListener = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
