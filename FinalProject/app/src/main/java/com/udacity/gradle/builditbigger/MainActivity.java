package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import app.prerak.jokesdisplay.JokeActivity;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void tellJoke(View view) {
        new BackendAsycTask(this).execute();
    }


    public void displayJoke(String joke) {
        Intent display=new Intent(this,JokeActivity.class);
        display.putExtra(JokeActivity.JOKE_KEY,joke);
        Toast.makeText(this, joke, Toast.LENGTH_SHORT).show();
        startActivity(display);
    }


}
