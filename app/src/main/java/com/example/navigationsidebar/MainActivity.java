package com.example.navigationsidebar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    TextView questionLabel, questionCountLabel, scoreLabel;
    EditText answerEdt;
    Button submitButton;
    ProgressBar progressBar;
    ArrayList<QuestionModel> questionModelArraylist;

    int currentPosition = 0;
    int numberOfCorrectAnswer = 0;
    int questions_displayed = 0;

//    private TextView tv;
    private double first_val;
    private String operation;
    private View v;

    private boolean current_view = false; // activity_main = mcqs

    private CountDownTimer cdt;
    private boolean timer_is_running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.calc_layout);
//        tv = findViewById(R.id.textViewCalc);
        setContentView(R.layout.activity_main);
        this.setTitle("Assignment 3");

        cdt = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                checkAnswer();
            }

            @Override
            public void onFinish() {
                timer_is_running = false;
                checkAnswer();
            }
        };
        cdt.start();
        timer_is_running = true;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
//                    case android.R.id.home:
//                        mDrawerLayout.openDrawer(GravityCompat.START);
//                        return true;

                    case R.id.calc:
                        Log.d("", "calculator selected");
                        setContentView(R.layout.calc_layout);
                        current_view = true; // calculator
                        return true;

                    case R.id.mcq:
                        Log.d("", "mcqs selected");
                        setContentView(R.layout.activity_main);
                        current_view = false; // mcqs

                        questionCountLabel = findViewById(R.id.noQuestion);
                        questionLabel = findViewById(R.id.question);
                        scoreLabel = findViewById(R.id.score);

                        answerEdt = findViewById(R.id.answer);
                        submitButton = findViewById(R.id.submit);
                        progressBar = findViewById(R.id.progress);

                        submitButton.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                checkAnswer();
                            }
                        });

                        answerEdt.setOnKeyListener(new View.OnKeyListener()
                        {
                            public boolean onKey(View v, int keyCode, KeyEvent event)
                            {
                                // If the event is a key-down event on the "enter" button
                                Log.e("event.getAction()",event.getAction()+"");
                                Log.e("event.keyCode()",keyCode+"");
                                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                        (keyCode == KeyEvent.KEYCODE_ENTER))
                                {
                                    checkAnswer();
                                    return true;
                                }
                                return false;
                            }
                        });

                        return true;
                }

                return false;
            }
        });


        questionCountLabel = findViewById(R.id.noQuestion);
        questionLabel = findViewById(R.id.question);
        scoreLabel = findViewById(R.id.score);

        answerEdt = findViewById(R.id.answer);
        submitButton = findViewById(R.id.submit);
        progressBar = findViewById(R.id.progress);

        questionModelArraylist = new ArrayList<>();

        setUpQuestion();

        setData();

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer();
            }
        });

        answerEdt.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                // If the event is a key-down event on the "enter" button
                Log.e("event.getAction()",event.getAction()+"");
                Log.e("event.keyCode()",keyCode+"");
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    checkAnswer();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(mToggle.onOptionsItemSelected(item))
        {
            if(current_view) // calculator
            {
                setContentView(R.layout.activity_main); //mcqs
                current_view = false;

                questionCountLabel = findViewById(R.id.noQuestion);
                questionLabel = findViewById(R.id.question);
                scoreLabel = findViewById(R.id.score);

                answerEdt = findViewById(R.id.answer);
                submitButton = findViewById(R.id.submit);
                progressBar = findViewById(R.id.progress);

                submitButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        checkAnswer();
                    }
                });

                answerEdt.setOnKeyListener(new View.OnKeyListener()
                {
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        // If the event is a key-down event on the "enter" button
                        Log.e("event.getAction()",event.getAction()+"");
                        Log.e("event.keyCode()",keyCode+"");
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER))
                        {
                            checkAnswer();
                            return true;
                        }
                        return false;
                    }
                });
            }
//            else
//            {
//                setContentView(R.layout.calc_layout); //mcqs
//                current_view = true;
//            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void checkAnswer()
    {
        String answerString  = answerEdt.getText().toString().trim();
        if(timer_is_running)
        {
            cdt.cancel();
            if(answerString.equalsIgnoreCase(questionModelArraylist.get(currentPosition).getAnswer()))
            {
                numberOfCorrectAnswer ++;

                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Good job!")
                        .setContentText("Right Asswer")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                        {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog)
                            {
//                                currentPosition ++;
                                questions_displayed++;

                                setData();
                                answerEdt.setText("");
                                sweetAlertDialog.dismiss();
                            }
                        }).show();
            }
            else
            {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Wrong Answer")
                        .setContentText("The right answer is : "+questionModelArraylist.get(currentPosition).getAnswer())
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();

//                                currentPosition ++;
                                questions_displayed++;

                                setData();
                                answerEdt.setText("");
                            }
                        }).show();
            }

//            int x = ((currentPosition+1) * 100) / questionModelArraylist.size();
            int x = ((questions_displayed+1) * 100) / questionModelArraylist.size();

            progressBar.setProgress(x);

            cdt.start();
        }
        else
        {
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Time up")
                    .setContentText("The right answer is : "+questionModelArraylist.get(currentPosition).getAnswer())
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();

//                            currentPosition ++;
                            questions_displayed++;

                            setData();
                            answerEdt.setText("");
                        }
                    }).show();

//            int x = ((currentPosition+1) * 100) / questionModelArraylist.size();
            int x = ((questions_displayed+1) * 100) / questionModelArraylist.size();

            progressBar.setProgress(x);

            cdt.start();
        }


    }




    public void setUpQuestion()
    {
        questionModelArraylist.add(new QuestionModel("A triangle has how many shapes?","3"));
        questionModelArraylist.add(new QuestionModel("What is the square root of 9 ? ","3"));
        questionModelArraylist.add(new QuestionModel("A hexagon has how many sides ? ","6"));
        questionModelArraylist.add(new QuestionModel("Even number just after 211 ? ","212"));
        questionModelArraylist.add(new QuestionModel("The short form 500+40+0 ","540"));

        questionModelArraylist.add(new QuestionModel("200 more than 600 is? ","800"));
        questionModelArraylist.add(new QuestionModel("Forty eight in figures is","48"));
        questionModelArraylist.add(new QuestionModel("The least number among 609, 690, 906 and 960","609"));
        questionModelArraylist.add(new QuestionModel("The smallest 2 digit number is? ","10"));
        questionModelArraylist.add(new QuestionModel("Number comes before 333 is ?","332"));

    }

    public void setData()
    {
        currentPosition = getRandomElement();
        if(questionModelArraylist.size()>questions_displayed)
        {
            questionLabel.setText(questionModelArraylist.get(currentPosition).getQuestionString());

            scoreLabel.setText("Score :" + numberOfCorrectAnswer + "/" + questionModelArraylist.size());
            questionCountLabel.setText("Question No : " + (questions_displayed + 1));
        }
        else
        {
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("You have successfully completed the quiz")
                    .setContentText("Your score is : "+ numberOfCorrectAnswer + "/" + questionModelArraylist.size())
                    .setConfirmText("Restart")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            sDialog.dismissWithAnimation();
                            currentPosition = 0;
                            questions_displayed = 0;
                            numberOfCorrectAnswer = 0;
                            progressBar.setProgress(0);
                            setData();
                        }
                    })
                    .setCancelText("Close")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog)
                        {
                            sDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();
        }

    }


    public void onClickButtons(View view)
    {
        TextView tv = findViewById(R.id.textViewCalc);

        if( ((AppCompatButton)view).getText().equals("1") ||
                ((AppCompatButton)view).getText().equals("2") ||
                ((AppCompatButton)view).getText().equals("3") ||
                ((AppCompatButton)view).getText().equals("4") ||
                ((AppCompatButton)view).getText().equals("5") ||
                ((AppCompatButton)view).getText().equals("6") ||
                ((AppCompatButton)view).getText().equals("7") ||
                ((AppCompatButton)view).getText().equals("8") ||
                ((AppCompatButton)view).getText().equals("9") ||
                ((AppCompatButton)view).getText().equals("0") ||
                ((AppCompatButton)view).getText().equals(".") )
        {
            if(!tv.getText().equals("0"))
            {
                String update = tv.getText() + ((AppCompatButton)view).getText().toString();
                tv.setText(update);
            }
            else
            {
                String update = ((AppCompatButton)view).getText().toString();
                tv.setText(update);
            }
        }
        else if( ((AppCompatButton)view).getText().equals("C") )
        {
            tv.setText("0");
        }
        else if( ((AppCompatButton)view).getText().equals("+") ||
                ((AppCompatButton)view).getText().equals("-") ||
                ((AppCompatButton)view).getText().equals("x") ||
                ((AppCompatButton)view).getText().equals("/") )
        {
            first_val = Double.parseDouble(tv.getText().toString());
            operation = ((AppCompatButton)view).getText().toString();
            tv.setText("0");
            v = view;
            v.setEnabled(false);
        }
        else if( ((AppCompatButton)view).getText().equals("=") )
        {
            double second_val = Double.parseDouble(tv.getText().toString());
            v.setEnabled(true);

            switch (operation)
            {
                case "+":
                    tv.setText(Double.toString(first_val + second_val));
                    break;

                case "-":
                    tv.setText(Double.toString(first_val - second_val));
                    break;

                case "x":
                    tv.setText(Double.toString(first_val * second_val));
                    break;

                case "/":
                    tv.setText(Double.toString(first_val / second_val));
                    break;
            }
        }
    }

    public int getRandomElement()
    {
        Random rand = new Random();
        return (rand.nextInt(questionModelArraylist.size()));
    }
}
