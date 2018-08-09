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
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Appointment;
import me.braedonvillano.vaain.models.Request;


public class ClientHistoryFragment extends Fragment {

    List<Appointment> pastAppts;
    RecyclerView rvAppointments;

    public ClientHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_history, container, false);
        pastAppts = new ArrayList<>();

        rvAppointments = view.findViewById(R.id.rvPastAppts);
        BeautHistoryAdapter adapter = new BeautHistoryAdapter(pastAppts);
        getParseHistory(adapter);
        rvAppointments.setAdapter(adapter);
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    void getParseHistory(final BeautHistoryAdapter adapter) {
        ParseQuery<Appointment> apptQuery = new Appointment.Query().withBeaut().withClient().withProduct();
        if(ParseUser.getCurrentUser().getBoolean("isClient")){
            apptQuery.whereEqualTo("client", ParseUser.getCurrentUser());
        }else{
            apptQuery.whereEqualTo("beaut", ParseUser.getCurrentUser());
        }
        apptQuery.whereEqualTo("isComplete",true);
        apptQuery.findInBackground(new FindCallback<Appointment>() {
            @Override
            public void done(List<Appointment> objects, ParseException e) {

                if (e == null) {
                    pastAppts = objects;
                    int size = objects.size();
                    adapter.clear();
                    adapter.addAll(pastAppts);
                    adapter.notifyItemRangeInserted(0,size);
                    Log.d("BeautsHistoryFragment", "Retrieved " + size + " past appointments");
                } else {
                    Log.d("BeautsHistoryFragment", "Error: " + e.getMessage());
                }
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
