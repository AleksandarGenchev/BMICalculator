package com.example.bmicalculator;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class History extends DBActivity {
    protected EditText editHeight, editWeight;
    protected ListView simpleList;
    protected Button btnDelete;
    protected TextView textView, tvResult;
    protected String ID;
    protected void HistoryFiller(){
        try {
            final ArrayList<String> listResults = new ArrayList<>();
            SelectSQL("SELECT * FROM INFORMATION ORDER BY ID ",
                    null,
                    new OnSelectSuccess() {
                        @Override
                        public void OnElementSelected(String ID, String Weight, String Height, String Result) {
                            listResults.add(ID + "\t" + Weight + "\t" + Height + "\t" + Result + "\n");
                        }
                    }
            );
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.activity_list_view,
                    R.id.textView,
                    listResults
            );

            simpleList.setAdapter(arrayAdapter);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        HistoryFiller();
    }

    protected void BackToMain(){
        finishActivity(200);
        Intent intent = new Intent(History.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        simpleList=findViewById(R.id.simpleList);
        textView = findViewById(R.id.textView);
        btnDelete = findViewById(R.id.btnDelete);
        editHeight = findViewById(R.id.editHeight);
        editWeight = findViewById(R.id.editWeight);
        tvResult = findViewById(R.id.tvResult);
        Bundle b = getIntent().getExtras();
        if (b!=null){
            ID = b.getString("ID");
            editWeight.setText(b.getString("Weight"));
            editHeight.setText(b.getString("Height"));
            tvResult.setText(b.getString("Result"));
        }

        initDB();
        HistoryFiller();

        btnDelete.setOnClickListener(view -> {
            ExecSQL("DELETE FROM INFORMATION",
                    null,
                    new OnQuerySuccess() {
                        @Override
                        public void OnSuccess() {
                            Toast.makeText(History.this,
                                    "Delete Successful",
                                    Toast.LENGTH_LONG).show();
                            BackToMain();
                        }
                    },
                    new OnQueryError() {
                        @Override
                        public void OnError(String error) {
                            Toast.makeText(History.this,
                                    error,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
            );
        });

    }

}