package me.braedonvillano.vaain;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.braedonvillano.vaain.models.Appointment;
import me.braedonvillano.vaain.models.Location;
import me.braedonvillano.vaain.models.Product;
import me.braedonvillano.vaain.models.Request;


public class ClientRequestsFragment extends Fragment {

    private List<Product> products;
    private TextView selectDate;
    private TextView selectTime;
    private TextView dateSelected;
    private TextView timeSelected;
    private EditText rComments;
    private TextView rService;
    private Button rSubmit;

    private Product mProduct;
    private ParseUser mBeaut;
    private String mDate;
    private String mTime;
    private BasicDateTime mDateTime;
    public List<Appointment> appointments;
    public RadioButton btnloc1;
    public RadioButton btnloc2;
    public RadioButton btnloc3;
    public TextView app1;
    public TextView app2;
    public TextView app3;
    public TextView app4;
    public TextView app5;

    private DatePickerDialog.OnDateSetListener cDateSetListener;
    private TimePickerDialog.OnTimeSetListener cTimeSetListener;

    public ClientRequestsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_requests, container, false);

        mProduct = new Product();

        selectDate = view.findViewById(R.id.tvSelectDate);
        selectTime = view.findViewById(R.id.tvSelectTime);
        dateSelected = view.findViewById(R.id.etDateSelected);
        timeSelected = view.findViewById(R.id.etTimeSelected);
        rComments = view.findViewById(R.id.mtRequestComments);
        rService = view.findViewById(R.id.tvRService);
        rSubmit = view.findViewById(R.id.btnSubmit);
        mDateTime = new BasicDateTime();
//        selectDate.setOnClickListener(new SelectDatePickerListener());
        btnloc1 = view.findViewById(R.id.rbtn1);
        btnloc2 = view.findViewById(R.id.rbtn2);
        btnloc3 = view.findViewById(R.id.rbtn3);

        btnloc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(view);
            }
        });
        app1 = view.findViewById(R.id.tvApp1);
        app2 = view.findViewById(R.id.tvApp2);
        app3 = view.findViewById(R.id.tvApp3);
        app4 = view.findViewById(R.id.tvApp4);
        app5 = view.findViewById(R.id.tvApp5);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });
        selectTime.setOnClickListener(new SelectTimePickerListener());
        rSubmit.setOnClickListener(new RequestCreateSubmit());

        app1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app1.setBackgroundColor(Color.parseColor("#000000"));
                Toast.makeText(getContext(), "You Choose this appointment", Toast.LENGTH_SHORT).show();
            }
        });
        app1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (app1.getBackground().toString().compareTo("#000000") == 0) {
                    app1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                return true;
            }
        });



        return view;
    }

    /* this method should be called before this fragment mounts */
    public void setProduct(Product product) {
        mProduct = product;
        mBeaut = mProduct.getBeaut();
    }

    public void onRadioButtonClicked(View view) {
        ParseUser user = null;
        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.include("loc1");
        userParseQuery.include("loc2");
        userParseQuery.include("loc3");
        try {
            user = userParseQuery.get(ParseUser.getCurrentUser().getObjectId());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbtn1:
                if (checked) {
                    Location loc1 = (Location) user.get("loc1");
                    String addy = loc1.getString("address");
                    LocationSchedule locationSchedule = new LocationSchedule(addy, 1);
                    ArrayList arrayList = locationSchedule.generateAppointments(6);
                    LocationSchedule.PotentialAppointment app = (LocationSchedule.PotentialAppointment) arrayList.get(0);

                    String app11 = app.appDate.toString();
                    app1.setText(app11);
                    // need generate appoints to generate a date and time slot avalible seems to generate a date but fails to generate a time slot.
                    String app22 = arrayList.get(1).toString();
                    app2.setText(app22);
                    String app33 = arrayList.get(2).toString();
                    app3.setText(app33);
                    String app44 = arrayList.get(3).toString();
                    app4.setText(app44);
                    String app55 = arrayList.get(4).toString();
                    app5.setText(app55);

                }
                    break;
            case R.id.rbtn2:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.rbtn3:
                if (checked)
                    //
                    break;
        }
    }

    /* logic for making request and pushing request to parse */
    public void makeRequest() {
        Request newRequest = new Request();

        newRequest.setClient(ParseUser.getCurrentUser());
        newRequest.setProduct(mProduct);
        newRequest.setBeaut(mBeaut);

        newRequest.setDateTime(mDateTime.getDateObject());
        newRequest.setDescription(rComments.getText().toString());

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
//
//    private class SelectDate implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            CalendarView cal = view.findViewById(R.id.calendarViewReq);
//        }
//    }



    /* below are the on-click listeners for this fragment */
    class SelectDatePickerListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final Calendar cal = Calendar.getInstance();

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Light,new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(final android.widget.DatePicker view, final int year, final int month, final int dayOfMonth) {
                    @SuppressLint("SimpleDateFormat")

                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

                    mDateTime.setDate(year, month, dayOfMonth);
                    cal.set(year, month, dayOfMonth);
                    mDate = sdf.format(cal.getTime());

                    dateSelected.setText(mDate); // set the date
                }
            }, month, day, year);

            cal.set(Calendar.MONTH, Calendar.AUGUST);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            datePicker.getDatePicker().setMinDate(cal.getTimeInMillis());

            cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
            cal.set(Calendar.DAY_OF_MONTH, 30);
            datePicker.getDatePicker().setMaxDate(cal.getTimeInMillis());



            datePicker.show();
        }
    }

    class SelectTimePickerListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(final TimePicker view, final int hr, final int min) {
                    mDateTime.setTime(hr, min);
                    Toast.makeText(getContext(), Integer.toString(hr), Toast.LENGTH_LONG).show();
                    int hour = hr % 12;
                    timeSelected.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, min, hr < 12 ? "am" : "pm"));
                }

            }, hour, minute, DateFormat.is24HourFormat(getActivity()));

            timePicker.show();
        }
    }

    class RequestCreateSubmit implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            makeRequest();
//            makeCalendarGarbage();
            getAppointments();
        }
    }

    public void getAppointments() {
        final Appointment.Query appQuery = new Appointment.Query();
        appQuery.findInBackground(new FindCallback<Appointment>() {
            @Override
            public void done(List<Appointment> objects, ParseException e) {
                appointments = objects;
                makeCalendarGarbage();
            }
        });
    }


    public void makeCalendarGarbage() {
        LocationSchedule locSched = new LocationSchedule("14701 Madison Place", 1);
        Boolean prefs[] = { false, true, false, true, false, true, false };
        locSched.removeOffDays(prefs);

        locSched.removeAppointmentList(appointments);

        ArrayList<LocationSchedule.PotentialAppointment> potApps;
        potApps = locSched.generateAppointments(1);

        for (LocationSchedule.PotentialAppointment app : potApps) {
            Log.d("*******", "date -> " + app.appDate.get(Calendar.MONTH) + ", " + app.appDate.get(Calendar.DAY_OF_MONTH));
            Log.d("*******", "seatid -> " + app.seatId);
            Log.d("*******", "start -> " + app.startTime);
        }


//        for (LocationSchedule.Day day : locSched.workDays) {
//            Log.d("*******","day of week: " + Integer.toString(day.day.get(Calendar.DAY_OF_WEEK)));
//
//            for (LocationSchedule.Seat seat : day.seats) {
//                Log.d("*******","-- seatid: " + seat.seatId);
//            }
//        }

        Toast.makeText(getContext(), "Calendar Function!", Toast.LENGTH_LONG).show();
    }

    /* basic time object */
    class BasicDateTime {
        private int mYear, mMonth, mDay, mHour, mMinute;

        public BasicDateTime() {}

        public void setDate(int year, int month, int day) {
            mYear = year - 1900;
            mMonth = month;
            mDay = day;
        }

        public void setTime(int hour, int minute) {
            mHour = hour;
            mMinute = minute;
        }

        public Date getDateObject() {
            return new Date(mYear, mMonth, mDay, mHour, mMinute);
        }
    }




    /* unused boilerplate methods */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

