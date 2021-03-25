package com.venkataram.mytriviaapp.data;

import com.venkataram.mytriviaapp.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncCallBack {
    void processFinished(ArrayList<Question> questionArrayList);
}
