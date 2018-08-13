package me.braedonvillano.vaain;

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

public class ClientAcctPastReqFragment extends Fragment {
    List<Appointment> appointments;
    RecyclerView rvPastAcct;

    public ClientAcctPastReqFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_client_acct_past_req, container, false);

        ParseQuery<Appointment> appointmentParseQuery = new Appointment.Query().withBeaut().withClient().withProduct().withLocation();
        //requestQuery.whereEqualTo("beaut", ParseUser.getCurrentUser());
        appointmentParseQuery.whereEqualTo("isComplete", true);
        appointmentParseQuery.findInBackground(new FindCallback<Appointment>() {
            @Override
            public void done(List<Appointment> appointments, com.parse.ParseException e) {


                if (e == null) {
                        //appoint = objects;
                        int size = appointments.size();
                        rvPastAcct = view.findViewById(R.id.rvPastAcct);
                        ClientAccountAdapterAppoint adapter = new ClientAccountAdapterAppoint(appointments);
                        rvPastAcct.setAdapter(adapter);
                        rvPastAcct.setLayoutManager(new LinearLayoutManager(getContext()));

                } else {}
            }
        });
        return view;
    }
}
