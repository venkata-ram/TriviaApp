package com.venkataram.mytriviaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.venkataram.mytriviaapp.data.AnswerListAsyncCallBack;
import com.venkataram.mytriviaapp.data.QuestionBank;
import com.venkataram.mytriviaapp.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTextView,counterTextView;
    private Button trueButton,falseButton;
    private ImageButton prevButton,nextButton;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionTextView = findViewById(R.id.question_textview);
        counterTextView = findViewById(R.id.counter_textview);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        prevButton = findViewById(R.id.prev_button);
        nextButton = findViewById(R.id.next_button);

        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);


        questionList = new QuestionBank().getQuestions(new AnswerListAsyncCallBack() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                counterTextView.setText(currentQuestionIndex+1 + " out of "+questionList.size());
                Log.d("Main", "onCreate: "+questionArrayList.toString());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.true_button:
                checkAnswer(true);
                break;
            case R.id.false_button:
                checkAnswer(false);
                break;
            case R.id.prev_button:
                if (currentQuestionIndex == 0)
                    currentQuestionIndex = questionList.size();
                currentQuestionIndex = currentQuestionIndex - 1;
                updateQuestion();
                break;
            case R.id.next_button:
                currentQuestionIndex = (currentQuestionIndex+1) % questionList.size();
                updateQuestion();
                break;

        }
    }

    private void updateQuestion() {
        questionTextView.setText(questionList.get(currentQuestionIndex).getAnswer());
        counterTextView.setText(currentQuestionIndex+1 + " out of "+questionList.size());
    }
    private void checkAnswer(boolean userChoice){
        boolean actualAnswer = questionList.get(currentQuestionIndex).isAnswerTrue();

        if(userChoice == actualAnswer)
            Toast.makeText(this, "That's correct", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "That's wrong", Toast.LENGTH_SHORT).show();
    }
}