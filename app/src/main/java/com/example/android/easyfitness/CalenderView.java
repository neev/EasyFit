package com.example.android.easyfitness;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;

public class CalenderView extends AppCompatActivity {

    CalendarView calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_view);
        //initializes the calendarview

        initializeCalendar();

    }



    public void initializeCalendar() {

        calendar = (CalendarView) findViewById(R.id.calendarView);


        // sets whether to show the week number.

        calendar.setShowWeekNumber(false);


        // sets the first day of week according to Calendar.

        // here we set Monday as the first day of the Calendar

        calendar.setFirstDayOfWeek(1);


        //The background color for the selected week.

        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.pink));


        //sets the color for the dates of an unfocused month.

        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.aluminum));


        //sets the color for the separator line between weeks.

        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.indigo));


        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.

        calendar.setSelectedDateVerticalBar(R.color.indigo);


        //sets the listener to be notified upon selected date change.

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            //show the selected date as a toast

            @Override

            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

                Toast.makeText(getApplicationContext(), day + "/" + (month +1)+ "/" + year, Toast
                        .LENGTH_LONG).show();

            }

        });
    }

}
