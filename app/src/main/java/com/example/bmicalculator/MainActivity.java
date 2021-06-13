package com.example.bmicalculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends DBActivity {
    protected EditText editHeight, editWeight;
    protected Button btnCalculate, btnHistory;
    protected TextView tvResult, tvBMIStatus, tvAdvice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editWeight=findViewById(R.id.editWeight);
        editHeight=findViewById(R.id.editHeight);
        btnCalculate=findViewById(R.id.btnCalculate);
        btnHistory=findViewById(R.id.btnHistory);
        tvResult=findViewById(R.id.tvResult);
        tvBMIStatus=findViewById(R.id.tvBMIStatus);
        tvAdvice=findViewById(R.id.tvAdvice);

        initDB();

        btnCalculate.setOnClickListener((view)->{
            if (editWeight.getText().toString().isEmpty() == true){
                editWeight.setError("The field is required!");
            } if(editHeight.getText().toString().isEmpty() == true){
                editHeight.setError("The field is required!");
            }
            else {
                printBMI();



                ExecSQL("INSERT INTO INFORMATION(Weight, Height, Result)" +
                                "VALUES(?, ?, ?)",
                        new Object[]{
                                editWeight.getText().toString(),
                                editHeight.getText().toString(),
                                tvResult.getText().toString()
                        },
                        () ->{
                            Toast.makeText(this, "Record Inserted", Toast.LENGTH_LONG).show();
                        },
                        (error) -> {
                            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                        }

                );
        }});

        

        btnHistory.setOnClickListener((view) ->{

            Intent intent = new Intent(
                    MainActivity.this,
                    History.class
            );
            startActivityForResult(intent, 200);



        });
    }

    private void printBMI() {
        String heightStr = editHeight.getText().toString();
        String weightStr = editWeight.getText().toString();
        if (heightStr != null && weightStr != null) {
            float height = Float.parseFloat(heightStr) / 100;
            float weight = Float.parseFloat(weightStr);
            float bmi = weight / (height * height);
            tvResult.setText(String.format("%.2f", bmi));

            if (bmi < 15) {
                tvBMIStatus.setText("Very severely underweight");
                tvAdvice.setText("You should visit a doctor immediately and start eating more because it's dangerous for your health.");
            } else if (bmi > 15 && bmi < 16) {
                tvBMIStatus.setText("Severely underweight");
                tvAdvice.setText("You should start eating more calorie dense whole foods.");
            } else if (bmi > 16 && bmi < 18.5) {
                tvBMIStatus.setText("Underweight");
                tvAdvice.setText("It is good for you to consume more calories and start weight training.");
            } else if (bmi > 18.5 && bmi < 25) {
                tvBMIStatus.setText("Normal");
                tvAdvice.setText("You are in a pretty normal shape according to the information you provided.");
            } else if (bmi > 25 && bmi < 30) {
                tvBMIStatus.setText("Overweight");
                tvAdvice.setText("You should slightly reduce the calories consumption.");
            } else if (bmi > 30 && bmi < 35) {
                tvBMIStatus.setText("Obese Class I (Moderately Obese)");
                tvAdvice.setText("You should include more movement/physical activity in your daily routine.");
            } else if (bmi > 35 && bmi < 40) {
                tvBMIStatus.setText("Obese Class II (Severely Obese)");
                tvAdvice.setText("You should start reducing the junk food from your menu and start exercising efficiently.");
            } else {
                tvBMIStatus.setText("Obese Class III (Very Severely Obese)");
                tvAdvice.setText("You need special diet from a personal trainer and a strict exercise program.");
            }
        }

    }
}