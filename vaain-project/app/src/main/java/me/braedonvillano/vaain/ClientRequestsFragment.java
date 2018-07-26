package me.braedonvillano.vaain;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import me.braedonvillano.vaain.models.Product;


public class ClientRequestsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Product> products;
    private TextView selectDate;
    private TextView selectTime;
    private EditText dateSelected;
    private EditText timeSelected;
    private TextView rService;
    private Button btnSubmit;

    public interface RequestsFragmentInterface {
        void onFragmentInteraction(Uri uri);
    }

    private RequestsFragmentInterface requestsInterface;

    public ClientRequestsFragment() {}


    public static RequestsFragment newInstance(String param1, String param2) {
        RequestsFragment fragment = new RequestsFragment();
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
        View view = inflater.inflate(R.layout.fragment_client_requests, container, false);

        selectDate = view.findViewById(R.id.tvSelectDate);
        selectTime = view.findViewById(R.id.tvSelectTime);
        dateSelected = view.findViewById(R.id.etDateSelected);
        timeSelected = view.findViewById(R.id.etTimeSelected);

        rService = view.findViewById(R.id.tvRService);


        Bundle bundle = getArguments();
        String prodName = bundle.getString("ProductName");

        rService.setText(prodName);


        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();


                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePicker = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker view, final int year, final int month,
                                          final int dayOfMonth) {

                        @SuppressLint("SimpleDateFormat")

                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                        cal.set(year, month, dayOfMonth);
                        String dateString = sdf.format(cal.getTime());

                        dateSelected.setText(dateString); // set the date
                    }
                }, month, day, year);

                cal.set(Calendar.MONTH,Calendar.AUGUST);
                cal.set(Calendar.DAY_OF_MONTH,1);
                datePicker.getDatePicker().setMinDate(cal.getTimeInMillis());

                cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
                cal.set(Calendar.DAY_OF_MONTH,30);
                datePicker.getDatePicker().setMaxDate(cal.getTimeInMillis());

                //datePicker.getDatePicker().setCalendarViewShown(false);
                datePicker.show();
                //dialog.show();
            }
        });

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePicker = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(final TimePicker view, final int hourOfDay, final int minute) {
                        Toast.makeText(getContext(), "The hour is " + Integer.toString(hourOfDay), Toast.LENGTH_LONG).show();

                       // @SuppressLint("SimpleDateFormat")

//                        SimpleDateFormat sdt = new SimpleDateFormat("hh:mm a");
//                        c.set(hourOfDay, minute);
//                        String timeString = sdt.format(c.getTime());
//
//                        timeSelected.setText(timeString);
                        int hour = hourOfDay % 12;
                        timeSelected.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                minute, hourOfDay < 12 ? "am" : "pm"));

                    }

                }, hour, minute, DateFormat.is24HourFormat(getActivity()));

                timePicker.show();
            }
        });
        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (requestsInterface != null) {
            requestsInterface.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof RequestsFragmentInterface) {
//            requestsInterface = (RequestsFragmentInterface) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        requestsInterface = null;
    }



}

