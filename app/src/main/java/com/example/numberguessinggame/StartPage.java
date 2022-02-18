package com.example.numberguessinggame;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartPage extends AppCompatActivity {

    TextView tvHighestLevel;
    Button btnPlay;

    public static final String MY_PREFS_FILE_NAME = "com.example.numberguessinggame.level";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        tvHighestLevel = (TextView) findViewById(R.id.tvHighestLevel);
        btnPlay = (Button) findViewById(R.id.btnPlay);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_FILE_NAME, MODE_PRIVATE);
        String highestLevelText = prefs.getString("level", "?");
        tvHighestLevel.setText("Highest Level: " + highestLevelText);

        ActivityResultLauncher<Intent> launchMainActivityForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK)
                        {
                            Intent intent = result.getData();

                            if (intent != null)
                            {
                                int level = intent.getIntExtra("level", 0);
                                if (!highestLevelText.equals("?")) {
                                    int highestLevel = Integer.parseInt(highestLevelText);

                                    if (level > highestLevel) {
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putString("level", Integer.toString(level));
                                        editor.apply();

                                        tvHighestLevel.setText("Highest Level: " + Integer.toString(level));
                                    }



                                }
                            }

                        }
                    }
                }
        );

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartPage.this, MainActivity.class);
                launchMainActivityForResult.launch(intent);
//                startActivity(intent);

            }
        });

        Log.d("levelTexter", String.format("highestLevelText: %s",
                highestLevelText));
    }
}