package com.example.calender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    CalendarView calendarView;
    private TextView date_view,events;
    private EditText editText;
    private Databasehelper myDb;
    private ListView listView;
    private int n;
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
        listView=(ListView)findViewById(R.id.listview);
        final ArrayList<String> list=new ArrayList<>();
        final ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
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
                DatabaseReference myref=FirebaseDatabase.getInstance().getReference().child("ALLEVENTS").child(selectedDate);
                myref.addValueEventListener(new ValueEventListener() {
                    private static final String TAG = "df";

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            list.add(snapshot.getValue().toString());
                            String value = snapshot.getValue(String.class);
                            Log.d(TAG, "Value is: " + value);

                        }
                        adapter.notifyDataSetChanged();

                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "Failed to read value.", databaseError.toException());

                    }
                });



            }
        });
      button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              n=n+1;
              FirebaseDatabase.getInstance().getReference().child("ALLEVENTS").child(selectedDate).child("Event "+ n).setValue(editText.getText().toString().trim());
              editText.setText(" ");
          }
      });


    }

    }


