package me.braedonvillano.vaain;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import me.braedonvillano.vaain.models.Appointment;
import me.braedonvillano.vaain.models.Location;
import me.braedonvillano.vaain.models.Product;
import me.braedonvillano.vaain.models.Request;

import static me.braedonvillano.vaain.LocationSchedule.PotentialAppointment;


public class ClientRequestsFragment extends Fragment implements SearchProductsAdapter.Callback {

    private List<Product> products;
    private TextView selectDate;
    private TextView selectTime;
    private TextView dateSelected;
    private EditText rComments;
    private TextView rService;
    private Button rSubmit;
    private Dialog mDialog;
    private ImageView profilePic;


    private OnFragmentInteractionListener mmListener;
    private RequestFragmentInterface mInterface;
    private Product mProduct;
    private ParseUser mBeaut;
    private String mDate;
    private String mTime;
    private BasicDateTime mDateTime;
    public List<Appointment> appointments;
    public PotentialAppointment[] potentialAppointments;
    public PotentialAppointment selectedAppointmet;
    public ParseUser user;
    public TextView app1;
    public TextView app2;
    public TextView app3;
    public TextView app4;
    public TextView app5;
    public Button next;
    private int numCLicks;
    private RatingBar ratingBar;

    private DatePickerDialog.OnDateSetListener cDateSetListener;
    private TimePickerDialog.OnTimeSetListener cTimeSetListener;

    public ClientRequestsFragment() {}

    /* this method should be called before this fragment mounts */
    public void setProduct(Product product) {
        mProduct = product;
        mBeaut = mProduct.getBeaut();
    }

    // TODO: handle mutual exclusion for buttons and other UI @braedon

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mProduct = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_requests, container, false);

        mDialog = new Dialog(getContext());
        mDialog.setContentView(R.layout.request_dialog);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        potentialAppointments = new PotentialAppointment[5];

        numCLicks = 0;
        ratingBar = view.findViewById(R.id.ratingBar);
        selectDate = view.findViewById(R.id.tvSelectDate);
        selectTime = view.findViewById(R.id.tvSelectTime);
        dateSelected = view.findViewById(R.id.etDateSelected);
        rComments = view.findViewById(R.id.mtRequestComments);
        rService = view.findViewById(R.id.tvRService);
        rSubmit = view.findViewById(R.id.btnSubmit);
        profilePic = view.findViewById(R.id.ivRProPic);
        mDateTime = new BasicDateTime();
        selectDate.setOnClickListener(new SelectDatePickerListener());
        ImageButton ibloc1 = mDialog.findViewById(R.id.ibtnloc1);
        ImageButton ibloc2 = mDialog.findViewById(R.id.ibtnloc2);
        ImageButton ibloc3 = mDialog.findViewById(R.id.ibtnloc3);
        next = mDialog.findViewById(R.id.btnNext);



        final int ranRate = new Random().nextInt(5);
        ratingBar.setRating(Float.parseFloat(String.valueOf(ranRate)));

        if (mBeaut.get("profileImage") != null) {
            ParseFile file = mBeaut.getParseFile("profileImage");
            Glide.with(this).load(file.getUrl()).apply(RequestOptions.circleCropTransform()).into(profilePic);
        }
        // add callback so service and propic can be retrieved from dialog or search fragment
       rService.setText(mBeaut.get("Name").toString());

        app1 = view.findViewById(R.id.tvApp1);
        app2 = view.findViewById(R.id.tvApp2);
        app3 = view.findViewById(R.id.tvApp3);
        app4 = view.findViewById(R.id.tvApp4);
        app5 = view.findViewById(R.id.tvApp5);

        rSubmit.setOnClickListener(new RequestCreateSubmit());
        app1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onSlotClick((TextView) view, 0); }
        });
        app2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onSlotClick((TextView) view, 1); }
        });
        app3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onSlotClick((TextView) view, 2); }
        });
        app4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onSlotClick((TextView) view, 3); }
        });
        app5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onSlotClick((TextView) view, 4); }
        });


        user = null;
        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.include("loc1");
        userParseQuery.include("loc2");
        userParseQuery.include("loc3");
        try {
            user = userParseQuery.get(ParseUser.getCurrentUser().getObjectId());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final ParseUser finalUser = user;
        ibloc1.setOnClickListener(new LocationHandler("loc1"));
        ibloc2.setOnClickListener(new LocationHandler("loc2"));
        ibloc3.setOnClickListener(new LocationHandler("loc3"));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                selectTime.setText("Please select a time slot");
                selectTime.setBackground(getResources().getDrawable(R.drawable.border_slot));
            }
        });

        return view;
    }

    public void onSlotClick(TextView view, int appointmentSlot) {
        rComments.setVisibility(View.VISIBLE);
        selectedAppointmet = potentialAppointments[appointmentSlot];
        numCLicks++;
        if (numCLicks % 2 == 0) {
            view.setBackgroundColor(Color.parseColor("#00000000"));
            Toast.makeText(getContext(), "You deselected this appointment", Toast.LENGTH_SHORT).show();
            rSubmit.setBackgroundColor(Color.parseColor("#515050"));

        } else {
            view.setBackground(getResources().getDrawable(R.drawable.border_slot));
            Toast.makeText(getContext(), "You selected this appointment", Toast.LENGTH_SHORT).show();
            rSubmit.setBackgroundColor(Color.parseColor("#EB5E55"));
            if (view.getText().length() == 0) {
                Toast.makeText(getContext(), "Please choose a location first", Toast.LENGTH_LONG).show();
            }
        }
    }

    /* logic for making request and pushing request to parse */
    public void makeRequest() {
        Request newRequest = new Request();

        if (mProduct == null) {
            Toast.makeText(getContext(), "BAD", Toast.LENGTH_LONG).show();
            return;
        }

        newRequest.setClient(ParseUser.getCurrentUser());
        newRequest.setProduct(mProduct);
        newRequest.setBeaut(mBeaut);

        Date dateTime = new Date(selectedAppointmet.appDate.getTimeInMillis());
        newRequest.setDateTime(dateTime);
        // TODO: uncomment below line when dataset is clean and products have lengths
//        newRequest.setLength(mProduct.getLength());
        newRequest.setLength(1);
        newRequest.setSeat(selectedAppointmet.seatId);
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

                mInterface.onBookAppointment();
                Toast.makeText(getContext(), "Request Made!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /* below are the on-click listeners for this fragment */
    class SelectDatePickerListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final Calendar cal = Calendar.getInstance();

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            //Theme_DeviceDefault_Light

            final DatePickerDialog datePicker = new DatePickerDialog(getContext(), R.style.datepicker,new DatePickerDialog.OnDateSetListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDateSet(final android.widget.DatePicker view, final int year, final int month, final int dayOfMonth) {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy ");
                    mDateTime.setDate(year, month, dayOfMonth);
                    cal.set(year, month, dayOfMonth);
                    mDate = sdf.format(cal.getTime());
                    dateSelected.setText(mDate); // set the date

                    if (dateSelected.getText().length() != 0) {
                        mDialog.show();
                    }
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

    class RequestCreateSubmit implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            makeRequest();
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

        ArrayList<PotentialAppointment> potApps;
        potApps = locSched.generateAppointments(1);

        for (PotentialAppointment app : potApps) {
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

     class LocationHandler implements View.OnClickListener {
         private String location;
         private ParseUser finalUser;

         public LocationHandler(String location) {
             this.location = location;
             finalUser = user;
         }

         @Override
         public void onClick(View view) {
             /* get location and address from user */
             Location loc = (Location) finalUser.get(location);
             String addy = loc.getString("address");

             /* build schedule and generate appointments TODO: actually remove appointments */
             LocationSchedule locationSchedule = new LocationSchedule(addy, 1);
             ArrayList dailyList = locationSchedule.generateAppointmentsDaily(6);

             Integer day = Integer.valueOf(mDate.substring(4, 6));
             ArrayList appointmentList = (ArrayList) dailyList.get(day - 1);
             Integer[] randNums = new Integer[5];
             String[] strings1 = new String[5];

             for (int i = 0; i < 5; i++) {
                 final int random = new Random().nextInt(10);
                 if (Arrays.asList(randNums).contains(random) == false) {
                     randNums[i] = random;
                 } else {
                     randNums[i] = random + 1;
                 }
             }
             for (int i = 0; i < 5; i++) {
                 potentialAppointments[i] = (PotentialAppointment) appointmentList.get(Integer.valueOf(randNums[i]));
                 strings1[i] = potentialAppointments[i].start.toString();
             }

             app1.setText(mDate + " at " + strings1[0]);
             app2.setText(mDate + " at " + strings1[1]);
             app3.setText(mDate + " at " + strings1[2]);
             app4.setText(mDate + " at " + strings1[3]);
             app5.setText(mDate + " at " + strings1[4]);

             mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#515050")));
             next.setBackgroundColor(Color.parseColor("#EB5E55"));
         }
     }


    /* unused boilerplate methods */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RequestFragmentInterface) {
            mInterface = (RequestFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRequestProduct(Product product, int code) {
        mmListener.onProductClick(product, code);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onProductClick(Product product, int code);
    }

    public interface RequestFragmentInterface {
        void onBookAppointment();
    }

}

