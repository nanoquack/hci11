package com.questo.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.questo.android.common.Constants;
import com.questo.android.model.Question;
import com.questo.android.view.FlexibleImageView;
import com.questo.android.view.TopBar;

public class QuestionResult extends Activity {

    private TopBar topbar;
    private int currentQuestion;
    private int correctQtnAnswer;
    private String questUuid;
    private String correctAnswer;
    private String currentTournamentTaskUuid;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_result);
        this.init(getIntent().getExtras());

    }

    public void onBackPressed() {
        // TODO show popup and ask
        // or show toast and ask to push back again
        startActivity(new Intent(this, HomeView.class));
    }

    private void init(Bundle extras) {
        boolean correct = extras.getBoolean(Constants.BOOL_CORRECT_ANSWER);
        String qtn = extras.getString(Constants.QUESTION);
        size = extras.getInt(Constants.QUEST_SIZE);
        questUuid = extras.getString(Constants.TRANSITION_OBJECT_UUID);
        currentQuestion = extras.getInt(Constants.NR_ANSWERED_QUESTIONS);
        correctQtnAnswer = extras.getInt(Constants.NR_ANSWERED_QUESTIONS_CORRECT);
        correctAnswer = extras.getString(Constants.CORRECT_ANSWER);
        currentTournamentTaskUuid = extras.getString(Constants.CURRENT_TOURNAMENT_TASK_UUID);
        int count = 10;
        if (size < 10) {
            count = size;
        }

        this.topbar = (TopBar) findViewById(R.id.topbar);
        this.topbar.setLabel(Constants.QUESTION_PROGRESS.replaceFirst("\\{\\}", currentQuestion + 1 + "")
                .replace("{}", count + ""));

        ProgressBar bar = (ProgressBar) findViewById(R.id.progressbar);
        bar.setProgressDrawable(getResources().getDrawable(R.drawable.bg_progressbar));
        bar.setProgress(this.calcProgress(currentQuestion + 1, count));

        FlexibleImageView imgAnswer = (FlexibleImageView) findViewById(R.id.img_answer);
        TextView txtAnswer = (TextView) findViewById(R.id.txt_answer);

        if (correct) {
            imgAnswer.setBackgroundResource(R.drawable.img_check);
            txtAnswer.setText(Html.fromHtml("<big>Your answer is correct!</big>"));
        } else {
            imgAnswer.setBackgroundResource(R.drawable.img_cross);
            txtAnswer.setText(Html
                    .fromHtml("<big><b>Your answer is incorrect!</b></big><br/><br/>The correct answer is:<br/><b>" + correctAnswer + "</b>"));
        }

        
        
        Button btnNext = (Button) findViewById(R.id.btn_next_question);
        btnNext.setOnClickListener(new NextQuestionClickListener());
        
        LinearLayout reportLayout = (LinearLayout)findViewById(R.id.layout_report);
        reportLayout.setOnClickListener(new ReportClickListener(qtn));

    }

    private int calcProgress(int q, int count) {
        return (int) ((q * 100) / count);
    }

    private class NextQuestionClickListener implements OnClickListener {

        @Override
        public void onClick(View btn) {
            Intent intent;
            if (QuestionResult.this.size > QuestionResult.this.currentQuestion + 1) {
                intent = new Intent(QuestionResult.this, QuestionView.class);
                intent.putExtra(Constants.TRANSITION_OBJECT_UUID, QuestionResult.this.questUuid);
                intent.putExtra(Constants.NR_ANSWERED_QUESTIONS, QuestionResult.this.currentQuestion + 1);
                intent.putExtra(Constants.NR_ANSWERED_QUESTIONS_CORRECT, QuestionResult.this.correctQtnAnswer);     
            } else {
                intent = new Intent(QuestionResult.this, QuestCompleteView.class);
                intent.putExtra(Constants.QUEST_SIZE, size);
                intent.putExtra(Constants.NR_ANSWERED_QUESTIONS_CORRECT, QuestionResult.this.correctQtnAnswer);
                intent.putExtra(Constants.TRANSITION_OBJECT_UUID, QuestionResult.this.questUuid);
            }
            intent.putExtra(Constants.CURRENT_TOURNAMENT_TASK_UUID, currentTournamentTaskUuid);
            startActivity(intent);
        }

    }
    
    private class ReportClickListener implements OnClickListener {

        private String question;
        
        public ReportClickListener(String question) {
            this.question = question;
        }
        
        @Override
        public void onClick(View v) {
            startActivity(new Intent(QuestionResult.this, ReportQuestionView.class).putExtra(Constants.QUESTION, this.question));
            overridePendingTransition(R.anim.push_up_in, R.anim.no_change_out);
        }
        
    }
}
