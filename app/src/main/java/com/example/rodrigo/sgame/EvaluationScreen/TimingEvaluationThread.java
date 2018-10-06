package com.example.rodrigo.sgame.EvaluationScreen;

import com.example.rodrigo.sgame.EvaluationActivity;

public class TimingEvaluationThread extends  Thread {
    EvaluationActivity activity;
    public TimingEvaluationThread(EvaluationActivity activity){
     this.activity=activity;

    }

}
