package me.braedonvillano.vaain;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Location;



public class ClientSettingsFragment extends Fragment {


    private List<Location> locations;
    private List<String> locNames;
    private Spinner spLocation1;
    private Spinner spLocation2;
    private Spinner spLocation3;
    private ParseUser user;
    private Button btnLogout;
    ArrayAdapter<String> adapter;


    public ClientSettingsFragment() {
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

        View view = inflater.inflate(R.layout.fragment_client_settings, container, false);
        spLocation1 = (Spinner) view.findViewById(R.id.spLocation1);
        spLocation2 = (Spinner) view.findViewById(R.id.spLocation2);
        spLocation3 = (Spinner) view.findViewById(R.id.spLocation3);
        btnLogout = view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        ParseQuery<Location> query = ParseQuery.getQuery("Location");
        try {
            locations.addAll(query.find());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < locations.size(); i++) {
            locNames.add(locations.get(i).getString("address"));
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item);
        adapter.addAll(locNames);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spLocation1.setAdapter(adapter);
        spLocation2.setAdapter(adapter);
        spLocation3.setAdapter(adapter);
        checkPreferences();
        spLocation1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0)user.put("loc1",locations.get(i-1));

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spLocation2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0)user.put("loc2",locations.get(i-1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spLocation3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0)user.put("loc3",locations.get(i-1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        return view;
    }

    private void logout() {
        ParseUser.logOut();
        assert(ParseUser.getCurrentUser() == null);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void checkPreferences() {
        Location loc1 = (Location)user.get("loc1");
        Location loc2 = (Location)user.get("loc2");
        Location loc3 = (Location)user.get("loc3");

        for(int i = 0; i < locNames.size();i++) {
            if(loc1 != null && loc1.getString("address").compareTo(locNames.get(i)) == 0)
                spLocation1.setSelection(i,true);
            if(loc2 != null && loc2.getString("address").compareTo(locNames.get(i)) == 0)
                spLocation2.setSelection(i,true);
            if(loc3 != null && loc3.getString("address").compareTo(locNames.get(i)) == 0)
                spLocation3.setSelection(i,true);
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
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("Settings","preferences updates");
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
