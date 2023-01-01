package com.kyadevs.stepdroid.EvaluationScreen;

import com.kyadevs.stepdroid.EvaluationActivity;

public class TimingEvaluationThread extends  Thread {
    EvaluationActivity activity;
    public TimingEvaluationThread(EvaluationActivity activity){
     this.activity=activity;

    }

}
