package com.example.telemed2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import android.os.AsyncTask;


public class MainActivity extends AppCompatActivity {
    RadioButton GenderM, GenderK, ScholarY, ScholarN, HipertenY, HipertenN, AlcY, AlcN, HandicapY, HandicapN, DiabY, DiabN, SMSY, SMSN;
    private DatePickerDialog datePickerDialog, datePickerDialog2;
    private Button dateButton, dateButton2, submit;
    EditText Age;
    int Gender = 0, Scholarship = 0, Hipertension = 0, Alcoholic = 0, Handicap = 0, Diabetic = 0, SMS = 0;
    String selectedDate=" ", selectedDate2=" " ;
    int TimeToVisit;
    List prediction = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GenderM = findViewById(R.id.GenderM);
        GenderK = findViewById(R.id.GenderK);
        ScholarY = findViewById(R.id.S_T);
        ScholarN = findViewById(R.id.S_N);

        HipertenY = findViewById(R.id.NC_T);
        HipertenN = findViewById(R.id.NC_N);
        AlcY = findViewById(R.id.A_T);
        AlcN = findViewById(R.id.A_N);
        HandicapY = findViewById(R.id.N_T);
        HandicapN = findViewById(R.id.N_N);
        DiabY = findViewById(R.id.C_T);
        DiabN = findViewById(R.id.C_N);
        SMSY = findViewById(R.id.SMS_T);
        SMSN = findViewById(R.id.SMS_N);

        submit =  findViewById(R.id.submitButton);
        Age   = findViewById(R.id.editTextNumber);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (GenderM.isChecked()) {
                    Gender = 1;
                }
                if (GenderK.isChecked()) {
                    Gender = 0;
                }
                if (ScholarY.isChecked()) {
                    Scholarship = 1;
                }
                if (ScholarN.isChecked()) {
                    Scholarship = 0;
                }
                if (HipertenY.isChecked()) {
                    Hipertension = 1;
                }
                if (HipertenN.isChecked()) {
                    Hipertension = 0;
                }
                if (AlcY.isChecked()) {
                    Alcoholic = 1;
                }
                if (AlcN.isChecked()) {
                    Alcoholic = 0;
                }
                if (HandicapY.isChecked()) {
                    Handicap = 1;
                }
                if (HandicapN.isChecked()) {
                    Handicap = 0;
                }
                if (DiabY.isChecked()) {
                    Diabetic = 1;
                }
                if (DiabN.isChecked()) {
                    Diabetic = 0;
                }
                if (SMSY.isChecked()) {
                    SMS = 1;
                }
                if (SMSN.isChecked()) {
                    SMS = 0;
                }

                System.out.println(Gender);
                System.out.println(Scholarship);
                System.out.println(Hipertension);
                System.out.println(Alcoholic);
                System.out.println(Handicap);
                System.out.println(Diabetic);
                System.out.println(SMS);
                System.out.println(selectedDate);
                System.out.println(selectedDate2);
                System.out.println(Age.getText());

                selectedDate = selectedDate.replace(" ", "-");
                selectedDate2 = selectedDate2.replace(" ", "-");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
                    LocalDate date = LocalDate.parse(selectedDate, formatter);
                    LocalDate date2 = LocalDate.parse(selectedDate2, formatter);
                    long daysBetween = ChronoUnit.DAYS.between(date, date2);
                    TimeToVisit= (int) Math.abs(daysBetween);
                }

                prediction.add(Gender);
                prediction.add(Scholarship);
                prediction.add(Hipertension);
                prediction.add(Alcoholic);
                prediction.add(Handicap);
                prediction.add(Diabetic);
                prediction.add(SMS);
                prediction.add(TimeToVisit);
                prediction.add(Age.getText());

                System.out.println("Dane:"+ prediction);

                HttpTask httpTask = new HttpTask();
                httpTask.execute();
            }
        });

        initDatePickers();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton2 = findViewById(R.id.datePickerButton2);
        dateButton.setText(getTodaysDate());
        dateButton2.setText(getTodaysDate());

    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    private void initDatePickers() {
        DatePickerDialog.OnDateSetListener dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                selectedDate = date;
                dateButton.setText(date);
            }
        };

        DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton2.setText(date);
                selectedDate2 = date;
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style= AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener1, year, month, day);
        datePickerDialog2 = new DatePickerDialog(this, style, dateSetListener2, year, month, day);
    }
    private String makeDateString(int day, int month, int year) {
        return  day + " "+ month +" "  + year;
    }
    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
    public void openDatePicker2(View view) {
        datePickerDialog2.show();
    }
    private class HttpTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                OkHttpClient client = new OkHttpClient();

                JSONObject data = new JSONObject();
                data.put("gender", Gender);
                data.put("age", Integer.parseInt(Age.getText().toString()));
                data.put("scholarship", Scholarship);
                data.put("hipertension", Hipertension);
                data.put("alcoholic", Alcoholic);
                data.put("handicap", Handicap);
                data.put("diabetic", Diabetic);
                data.put("SMS", SMS);
                data.put("timetovisit", TimeToVisit);

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody requestBody = RequestBody.create(JSON, data.toString());
                Request request = new Request.Builder()
                        .url("http://192.168.0.168:5000/predict")
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();

                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Przetwórz odpowiedź
            try {
                if (result != null) {
                    String res;
                    int result1 = Integer.parseInt(result);
                    if(result1==1){
                         res= "Pacjent nie przyjdzie na wizytę";
                    }
                    else{
                        res="Pacjent przyjdzie na wizytę";
                    }

                    System.out.println(result);

                    Toast.makeText(MainActivity.this, res, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                }
            } finally {

            }
            }

        }


    }



