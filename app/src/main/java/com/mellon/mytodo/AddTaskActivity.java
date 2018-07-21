package com.mellon.mytodo;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    DataHelper dbHelper;
    Button but1;
    DatePickerDialog dpd;
    int no;
    int startYear = 0, startMonth = 0, startDay = 0;
    EditText text1, text2, text3, text4, text5, text6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        //today date
        Date your_date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(your_date);
        startYear = cal.get(Calendar.YEAR);
        startMonth = cal.get(Calendar.MONTH);
        startDay = cal.get(Calendar.DAY_OF_MONTH);


        //Auto fill logic
        AutoCompleteTextView textView = findViewById(R.id.autocomplete_status);
        String[] status = getResources().getStringArray(R.array.status_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, status);
        textView.setAdapter(adapter);

        dbHelper = new DataHelper(this);
        text1 = findViewById(R.id.editText1);
        text2 = findViewById(R.id.editText2);
        text3 = findViewById(R.id.editText3);
        text4 = findViewById(R.id.editText4);
        text5 = findViewById(R.id.autocomplete_status);
        text6 = findViewById(R.id.editText5);
        but1 =  findViewById(R.id.button1);

        //validation
        text1.addTextChangedListener(textValidation);
        text2.addTextChangedListener(textValidation);

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("insert into todo(no, task, start, due, stats, dtls) values('" +
                        text1.getText().toString() + "','" +
                        text2.getText().toString() + "','" +
                        text3.getText().toString() + "','" +
                        text4.getText().toString() + "','" +
                        text5.getText().toString() + "','" +
                        text6.getText().toString() + "')");
                Toast.makeText(getApplicationContext(), "Task Added", Toast.LENGTH_LONG).show();
                MainActivity.ma.RefreshList();
                finish();
            }
        });

    }

    //Validation Button Logic
    private TextWatcher textValidation = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String text01 = text1.getText().toString().trim();
            String text02 = text2.getText().toString().trim();

            but1.setEnabled(!text01.isEmpty() && !text02.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    //calender
    @Override
    public void onResume() {
        super.onResume();
        dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("startDatepickerdialog");
        if (dpd != null) dpd.setOnDateSetListener(this);
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        startYear = year;
        startMonth = monthOfYear;
        startDay = dayOfMonth;
        String date = +dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        if (no == 1 ) {
            EditText task_date = findViewById(R.id.editText3);
            task_date.setText(date);
        }
        else {
            EditText task_date = findViewById(R.id.editText4);
            task_date.setText(date);
        }
    }

    public void showStartDatePickerStart(View v) {
        dpd = DatePickerDialog.newInstance(AddTaskActivity.this, startYear, startMonth, startDay);
        dpd.setOnDateSetListener(this);
        dpd.show(getFragmentManager(), "startDatepickerdialog");
        no = 1;
    }

    public void showStartDatePickerDue(View v) {
        dpd = DatePickerDialog.newInstance(AddTaskActivity.this, startYear, startMonth, startDay);
        dpd.setOnDateSetListener(this);
        dpd.show(getFragmentManager(), "startDatepickerdialog");
        no = 2;
    }
}
