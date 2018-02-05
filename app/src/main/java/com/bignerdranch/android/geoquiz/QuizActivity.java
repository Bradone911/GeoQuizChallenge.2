package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String QUESTION_LIST = "question_list";

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private Button mCheatButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            int[] mQuestionAnswerArray = savedInstanceState.getIntArray(QUESTION_LIST);
            for (int i=0; i<mQuestionBank.length; i++)  {
                mQuestionBank[i].setAnswered(mQuestionAnswerArray[i]);
            }
        }



        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
//      int question = mQuestionBank[mCurrentIndex].getTextResId();
//      mQuestionTextView.setText(question);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              Toast.makeText(QuizActivity.this,
//                      R.string.correct_toast,
//                      Toast.LENGTH_SHORT).show();
                checkAnswer(true);
            }
        });


        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              Toast.makeText(QuizActivity.this,
//                      R.string.incorrect_toast,
//                      Toast.LENGTH_SHORT).show();
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener()   {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex = 1) % mQuestionBank.length;
//              int question = mQuestionBank[mCurrentIndex].getTextResId();
//              mQuestionTextView.setText(question);
                mIsCheater = false;
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                //starts cheatActivity
//              Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
//              startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex == 0) {
                    mCurrentIndex = mQuestionBank.length - 1;
                } else {
                    mCurrentIndex = mCurrentIndex - 1;
                    updateQuestion();
                }
            }
        });

        updateQuestion();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)  {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        int[] mQuestionAnswerArray = new int[mQuestionBank.length];
        for (int i=0; i<mQuestionBank.length; i++)  {
            mQuestionAnswerArray[i] = mQuestionBank[i].isAnswered();
        }
        savedInstanceState.putIntArray(QUESTION_LIST, mQuestionAnswerArray);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }


    private void updateQuestion()   {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        setButtons();
    }

    private void setButtons()   {
        if (mQuestionBank[mCurrentIndex].isAnswered() > 0)  {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }else   {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }


    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                mQuestionBank[mCurrentIndex].setAnswered(2);
                messageResId = R.string.correct_toast;
            } else {
                mQuestionBank[mCurrentIndex].setAnswered(1);
                messageResId = R.string.incorrect_toast;
            }
        }
            setButtons();
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        if (requestCode != Activity.RESULT_OK)  {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT)  {
            if (data == null)   {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }
}
