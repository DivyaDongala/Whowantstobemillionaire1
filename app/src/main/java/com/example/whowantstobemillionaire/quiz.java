package com.example.whowantstobemillionaire;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;

public class quiz extends AppCompatActivity  {
    private TextView Question;
    private TextView Score;
    private TextView QuestionCount;
    private TextView CountDown;
    private RadioGroup rbGroup;
    private RadioButton radiob1;
    private RadioButton radiob2;
    private RadioButton radiob3;
    private Button buttonConfirmNext;



    private List<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Question = findViewById(R.id.question);
        Score = findViewById(R.id.score);
        QuestionCount = findViewById(R.id.questionCount);
        CountDown = findViewById(R.id.countdown);
        rbGroup = findViewById(R.id.radio_group);
        radiob1 = findViewById(R.id.button1);
        radiob2 = findViewById(R.id.button2);
        radiob3 = findViewById(R.id.button3);
        buttonConfirmNext = findViewById(R.id.confirm_next);


        QuizDBHelper dbHelper = new QuizDBHelper(this);
        questionList = dbHelper.getAllQuestions();
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);

        showNextQuestion();

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if any button is not selected pop up will come up to select an answer
                if (!answered) {
                    if (radiob1.isChecked() || radiob2.isChecked() || radiob3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(quiz.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion() {
        //comparing with question count and total questions.
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            Question.setText(currentQuestion.getQuestion());
            radiob1.setText(currentQuestion.getOption1());
            radiob2.setText(currentQuestion.getOption2());
            radiob3.setText(currentQuestion.getOption3());

            questionCounter++; //counter is increased if the total count is less than questioncounter

            QuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("Confirm");
        } else {
            finishQuiz();
        }
    }

    private void checkAnswer() {
        answered = true;

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

        if (answerNr == currentQuestion.getAnswerNr()) {
         score =score +1000;
            Score.setText("You Earned: " + score);
            //pop up message comes when the correct answer is selected
            Toast.makeText(quiz.this, "Correct Answer, you earned $1000", Toast.LENGTH_SHORT).show();

        } else{
            //pop up message comes when the wrong answer is selected
            Toast.makeText(quiz.this, "Wrong Answer, You don't get any point", Toast.LENGTH_SHORT).show();
        }

        showSolution();
    }
    private void showSolution() {
        radiob1.setTextColor(Color.RED);
        radiob2.setTextColor(Color.RED);
        radiob3.setTextColor(Color.RED);
        switch (currentQuestion.getAnswerNr()) {
            case 1:
                radiob1.setTextColor(Color.GREEN);
                break;
            case 2:
                radiob2.setTextColor(Color.GREEN);
                break;
            case 3:
                radiob3.setTextColor(Color.GREEN);
                break;
        }

        //if the question counter is less than total count it will go to next question else the quiz ends
        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Next");
        } else {
            buttonConfirmNext.setText("Finish");
        }
    }

    private void finishQuiz() {
        finish();
        if(score > 5000) {
            Toast.makeText(quiz.this, "Congratulations ,You won the game", Toast.LENGTH_SHORT).show();
        }
        else{
            //pop up message comes when the wrong answer is selected
            Toast.makeText(quiz.this, "You Lost the Game", Toast.LENGTH_SHORT).show();
        }
    }
}