package com.venkataram.mytriviaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.venkataram.mytriviaapp.data.AnswerListAsyncCallBack;
import com.venkataram.mytriviaapp.data.QuestionBank;
import com.venkataram.mytriviaapp.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Question> questionList = new QuestionBank().getQuestions(new AnswerListAsyncCallBack() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                Log.d("Main", "onCreate: "+questionArrayList.toString());
            }
        });

    }
}