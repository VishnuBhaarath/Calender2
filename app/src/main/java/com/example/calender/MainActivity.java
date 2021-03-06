package com.example.calender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
 import java.util.Date;

public class MainActivity extends AppCompatActivity {

    CalendarView calendarView;
    private TextView date_view,events;
    private EditText editText;
    private Databasehelper myDb;
    private ListView listView;
    private long n;
    private Button button,btadn;
    private String eventdetails,Date;
    public String selectedDate;
    private RelativeLayout relativeLayout;
    private Toolbar toolbar;
    private static final String TAG = "MainActivity";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private long m;
    ArrayAdapter adapter;
    ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        myDb = new Databasehelper(this);
        date_view=(TextView)findViewById(R.id.dateview);
        relativeLayout=(RelativeLayout)findViewById(R.id.layout);
        relativeLayout.setVisibility(View.INVISIBLE);
        button=(Button)findViewById(R.id.btsave);
        btadn=(Button)findViewById(R.id.btadd);
        editText=(EditText)findViewById(R.id.entertheevents);
        listView=(ListView)findViewById(R.id.listview);
        list=new ArrayList<>();
       adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        eventdetails=editText.getText().toString().trim();
        Selectdate();
        calendarView=(CalendarView)findViewById(R.id.calender);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Date =dayOfMonth+"-"+(month+1)+"-"+year;
                selectedDate = Integer.toString(dayOfMonth) + Integer.toString(month+1) + Integer.toString(year);
                date_view.setText(Date);
                add();

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this,"Enter event",Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("ALLEVENTS").child(selectedDate).child("Event " + n).setValue(editText.getText().toString().trim());
                }
                editText.setText(" ");
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        });
        mDisplayDate = (TextView) findViewById(R.id.displaydate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "displayDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String date = day+"-"+ month+"-"+year;
                date_view.setText(date);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String dateString = date;
                try{
                    //formatting the dateString to convert it into a date
                    Date df= sdf.parse(dateString);
                    System.out.println("Given Time in milliseconds : "+df.getTime());
                    calendarView.setDate(df.getTime());


                }catch(ParseException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }
        };
        btadn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.VISIBLE);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item2:
                SharedPreferences sharedPref = getSharedPreferences("myKey", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("date", selectedDate);
                editor.apply();
                startActivity(new Intent(MainActivity.this,detailedview.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Intializing selected date at the begining as the current date
    public void Selectdate(){
        DateFormat df = new SimpleDateFormat("ddMyyyy");
        Date dateobj = new Date();
        n=0;
        selectedDate= df.format(dateobj);
        add();
    }
  public void add(){
      DatabaseReference myref=FirebaseDatabase.getInstance().getReference().child("ALLEVENTS").child(selectedDate);
      myref.addValueEventListener(new ValueEventListener() {


          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              list.clear();
              m=dataSnapshot.getChildrenCount();
              n=m;
              for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                  list.add(snapshot.getValue().toString());

                  String value = snapshot.getValue(String.class);
                  Log.d(TAG, "Value is: " + m);

              }
              adapter.notifyDataSetChanged();

          }
          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {
              Log.w(TAG, "Failed to read value.", databaseError.toException());

          }
      });

      }


}

