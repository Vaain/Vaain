package me.braedonvillano.vaain;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import me.braedonvillano.vaain.models.Product;
import me.braedonvillano.vaain.models.Request;


public class ClientRequestsFragment extends Fragment {

    private List<Product> products;
    private TextView selectDate;
    private TextView selectTime;
    private EditText dateSelected;
    private EditText timeSelected;
    private TextView rService;
    private Button btnSubmit;

    private Product mProduct;
    private ParseUser mBeaut;

    private DatePickerDialog.OnDateSetListener cDateSetListener;
    private TimePickerDialog.OnTimeSetListener cTimeSetListener;

    public interface RequestsFragmentInterface {
        void onFragmentInteraction(Uri uri);
    }

    private RequestsFragmentInterface requestsInterface;

    public ClientRequestsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    public void onTimeSet(final TimePicker view, final int hr, final int min) {
                        int hour = hr % 12;
                        timeSelected.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, min, hr < 12 ? "am" : "pm"));

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

    public void makeRequest() {
        Request newRequest = new Request();

        newRequest.setClient(ParseUser.getCurrentUser());
        newRequest.setProduct(mProduct);
        newRequest.setBeaut(mProduct.getBeaut());
//        newRequest.setDateTime();
//        newRequest.setDescription();

        sendRequest(newRequest);
    }

    public void sendRequest(Request request) {
        request.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("Client Request Error", "save did not work!");
                    e.printStackTrace();
                    return;
                }
                // TODO: route the user to the appointments/requests page via interface
                Toast.makeText(getContext(), "Request Made!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setProduct(Product product) {
        mProduct = product;
        mBeaut = mProduct.getBeaut();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        requestsInterface = null;
    }



}

