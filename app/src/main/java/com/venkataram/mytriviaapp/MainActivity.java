package com.venkataram.mytriviaapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
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
import com.venkataram.mytriviaapp.utils.Prefs;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    
    private TextView questionTextView,counterTextView;
    private TextView scoreTextView,highScoreTextView;
    private Button trueButton,falseButton;
    private ImageButton prevButton,nextButton;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;
    private int score = 0;
    private Prefs prefs;

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
        scoreTextView = findViewById(R.id.score_textview);
        highScoreTextView = findViewById(R.id.high_score_textview);

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

        prefs = new Prefs(MainActivity.this);
        currentQuestionIndex = prefs.getStateIndex();
        updateHighScore();



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
                moveToPrevQuestion();
                break;
            case R.id.next_button:
                moveToNextQuestion();
                break;

        }
    }
    private void moveToPrevQuestion(){
        if (currentQuestionIndex == 0)
            currentQuestionIndex = questionList.size();
        currentQuestionIndex = currentQuestionIndex - 1;
        updateQuestion();
    }
    private void moveToNextQuestion(){
        currentQuestionIndex = (currentQuestionIndex+1) % questionList.size();
        updateQuestion();
    }

    private void updateQuestion() {
        questionTextView.setText(questionList.get(currentQuestionIndex).getAnswer());
        counterTextView.setText(currentQuestionIndex+1 + " out of "+questionList.size());
    }
    private void checkAnswer(boolean userChoice){
        boolean actualAnswer = questionList.get(currentQuestionIndex).isAnswerTrue();

        if(userChoice == actualAnswer){
            score += 10;
            updateScore();

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
                moveToNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeAnimation(){
        CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(300);
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
                moveToNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void updateScore(){
        String scoreString = "Your score : "+score;
        scoreTextView.setText(scoreString);
    }
    private void updateHighScore(){
        String highScoreString = "High score : "+prefs.getMaxScore();
        highScoreTextView.setText(highScoreString);
    }

    @Override
    protected void onPause() {
        super.onPause();
        prefs.saveMaxScore(score);
        prefs.saveState(currentQuestionIndex);
    }
}