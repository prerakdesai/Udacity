package app.prerak.jokesdisplay;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class JokeActivity extends ActionBarActivity {

    public static String JOKE_KEY = "Joke key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
    }
}
