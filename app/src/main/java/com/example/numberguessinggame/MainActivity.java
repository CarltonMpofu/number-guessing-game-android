package com.example.numberguessinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView tvLevel, tvGuessesLeft;
    Button btnGuess, btnHint;
    EditText etNumber;
    GridView mySimpleGrid;

    ArrayList<String> myNumbers;
    ArrayList<Boolean> guessedNumbers ;

    int level = 1;
    int guessesLeft = 6;
    int generatedNumber;
    int lowest = 0;
    int highest = level * 10;

    final int NUMBER_OF_GUESSES = 7;
    final int NEXT_LEVEL_DELAY = 2000;

    boolean showHint = true;

    Handler myHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySimpleGrid = (GridView) findViewById(R.id.simpleGridview);
        tvLevel = (TextView) findViewById(R.id.tvLevel);
        tvGuessesLeft = (TextView) findViewById(R.id.tvGuesses);
        btnGuess = (Button) findViewById(R.id.btnGuess);
        btnHint = (Button) findViewById(R.id.btnHint);
        etNumber = (EditText) findViewById(R.id.etNumber);


        myNumbers = new ArrayList<>();
        guessedNumbers = new ArrayList<>();
        myHandler = new Handler();

        generatedNumber =  (int)(Math.random() * (level*10));
//        Log.d("qwerty", Integer.toString(generatedNumber));

        addNumbersToList();



        tvLevel.setText("LEVEL " + level);
        tvGuessesLeft.setText("Guesses left: " + guessesLeft);
        etNumber.setText("");

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), myNumbers,
                guessedNumbers, -1, false);

        mySimpleGrid.setAdapter(customAdapter);

        btnGuess.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String numberTxt = etNumber.getText().toString().trim();
                if(!numberTxt.isEmpty())
                {
                    if(guessesLeft > 0)
                    {
                        try
                        {
                            int number = Integer.parseInt(numberTxt);
                            if (number >= 0 && number < level * 10)
                            {

                                if (number == generatedNumber) // Guessed correctly
                                {

                                    tvGuessesLeft.setText("CORRECT!!");
                                    CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), myNumbers,
                                            guessedNumbers, -1, true);
                                    mySimpleGrid.setAdapter(customAdapter);

                                    hideSoftKeyboard();

                                    startRepeatingTask();

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            stopRepeatingTask();

                                            moveToNextLevel();

                                        }
                                    }, NEXT_LEVEL_DELAY);
                                }
                                else // Wrong Guess
                                {
                                    if (number < generatedNumber)
                                    {
                                        lowest = number + 1;
                                    } else
                                    {
                                        highest = number - 1;
                                    }

                                    // Change the background of guessed number
                                    guessedNumbers.remove(number);
                                    guessedNumbers.add(number, true);
                                    CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), myNumbers,
                                            guessedNumbers, -1, false);
                                    mySimpleGrid.setAdapter(customAdapter);

                                    decreaseNumberOfGuesses();
                                }

                                showHint = true;
                                etNumber.setText("");
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, String.format(
                                        "Please guess a number between %s and %s", 0, (level * 10) - 1),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (NumberFormatException exc)
                        {
                            Toast.makeText(MainActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

        btnHint.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(showHint)
                {
                    if(guessesLeft > 0)
                    {
                        int middleNumber = (lowest + highest) / 2;
                        CustomAdapter customAdapter =  new CustomAdapter(getApplicationContext(), myNumbers,
                                guessedNumbers, middleNumber, false);
                        mySimpleGrid.setAdapter(customAdapter);

                        showHint = false;

                        decreaseNumberOfGuesses();
                    }
                }


            }
        });


    }

    private void moveToNextLevel() {
        tvGuessesLeft.setVisibility(View.VISIBLE);
        guessesLeft += NUMBER_OF_GUESSES;
        tvGuessesLeft.setText("Guesses left: " + guessesLeft);
        level += 1;
        tvLevel.setText("LEVEL " + level);
        lowest = 0;
        highest = level * 10;
        generatedNumber = (int) (Math.random() * (level * 10));
//        Log.d("qwerty", Integer.toString(generatedNumber));
        addNumbersToList();

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), myNumbers,
                guessedNumbers, -1, false);

        mySimpleGrid.setAdapter(customAdapter);
    }

    private void decreaseNumberOfGuesses()
    {
        guessesLeft--;
        tvGuessesLeft.setText("Guesses left: " + guessesLeft);
        if(guessesLeft <= 0)
        {
            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), myNumbers,
                    guessedNumbers, -1, true);
            mySimpleGrid.setAdapter(customAdapter);

            tvGuessesLeft.setText("YOU LOSE!!");

            startRepeatingTask();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run()
                {

                    Intent intent = new Intent();
                    intent.putExtra("level", level);
                    setResult(RESULT_OK, intent);

                    MainActivity.this.finish();
                }
            }, NEXT_LEVEL_DELAY);
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(etNumber.getWindowToken(), 0);
    }

    Runnable changeTextviewVisibility = new Runnable() {
        @Override
        public void run()
        {
         if(tvGuessesLeft.getVisibility() == View.VISIBLE)
         {
             tvGuessesLeft.setVisibility(View.INVISIBLE);
         }
         else
         {
             tvGuessesLeft.setVisibility(View.VISIBLE);
         }

         myHandler.postDelayed(changeTextviewVisibility, NEXT_LEVEL_DELAY/10);
        }
    };

    private void startRepeatingTask()
    {
        changeTextviewVisibility.run();
    }

    private void stopRepeatingTask()
    {
        myHandler.removeCallbacks(changeTextviewVisibility);
    }

    private void addNumbersToList()
    {
        myNumbers = new ArrayList<>();
        guessedNumbers = new ArrayList<>();
        for(int i = 0; i < level *10; i++)
        {
            myNumbers.add(Integer.toString(i));
            guessedNumbers.add(false);
        }
    }

}