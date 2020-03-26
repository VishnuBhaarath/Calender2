package com.example.calender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    CalendarView calendarView;
    private TextView date_view;
    private EditText editText;
    private Databasehelper myDb;
    private Button button;
   private String eventdetails,Date;
   public String selectedDate;
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new Databasehelper(this);
        date_view=(TextView)findViewById(R.id.dateview);
        button=(Button)findViewById(R.id.btadd);
        editText=(EditText)findViewById(R.id.entertheevents);
        eventdetails=editText.getText().toString().trim();
        calendarView=(CalendarView)findViewById(R.id.calender);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // index is start with 0
                 Date
                        = dayOfMonth + "-"
                        + (month + 1) + "-" + year;
                selectedDate = Integer.toString(year) + Integer.toString(month) + Integer.toString(dayOfMonth);

                // set this date in TextView for Display
                date_view.setText(Date);


            }
        });
      button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              FirebaseDatabase.getInstance().getReference().child("ALLEVENTS").child(selectedDate).setValue(editText.getText().toString().trim());
              editText.setText(" ");
          }
      });


    }

    }


