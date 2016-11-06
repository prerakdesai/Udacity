package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.AsyncTask;

import com.example.Jokes;

/**
 * Created by Prerak on 11/6/2016.
 */

public class BackendAsycTask extends AsyncTask<Void, Void, String> {
    private Context context;
    Jokes jokes=new Jokes();


    public BackendAsycTask(Context context) {
        this.context = context;
    }

    public BackendAsycTask() {

    }

    @Override
    protected String doInBackground(Void... params) {
        return jokes.getJokes();
    }

    @Override
    protected void onPostExecute(String result) {
        ((MainActivity) context).displayJoke(result);
    }
}

