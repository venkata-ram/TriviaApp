package com.venkataram.mytriviaapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
                updateQuestion();
                break;
            case R.id.false_button:

                checkAnswer(false);
                updateQuestion();
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

        if(userChoice == actualAnswer){
            Toast.makeText(this, "That's correct", Toast.LENGTH_SHORT).show();
            fadeAnimation();
        }
        else{
            Toast.makeText(this, "That's wrong", Toast.LENGTH_SHORT).show();
            shakeAnimation();
        }

    }
    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(getColor(R.color.light_sandal));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeAnimation(){
        CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(150);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(getColor(R.color.light_sandal));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}