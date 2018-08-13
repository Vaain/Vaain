package me.braedonvillano.vaain;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;

import com.applandeo.materialcalendarview.utils.CalendarProperties;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private CalendarProperties calendarProperties;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        // delete extra xml
        calendarView = findViewById(R.id.cvCal);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year , int month, int day) {
//            Long date = calendarView.getDate();
              String date = (month + 1) + "/" + day + "/" + year;


              Log.d("CalendarActivity", "onSelectDayChange: date: " + date);
            }

        });

        calendarView.setMinDate(1533106800000L);
        calendarView.setMaxDate(1538290800000L);

    }
}
