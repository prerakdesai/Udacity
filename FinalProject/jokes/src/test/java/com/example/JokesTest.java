package com.example;

import com.example.prerak.myapplication.backend.myApi.MyApi;
import com.example.prerak.myapplication.backend.myApi.model.MyBean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Prerak on 11/7/2016.
 */
@RunWith(JUnit4.class)
public class JokesTest {
    @Test
    public void getJokes() throws Exception {
        MyApi myApi=Mockito.mock(MyApi.class);
        MyApi.GetJokes getJokes=Mockito.mock(MyApi.GetJokes.class);
        Mockito.when(myApi.getJokes()).thenReturn(getJokes);
        Mockito.when(getJokes.execute()).thenReturn(new MyBean().setData("Joke"));
        Jokes jokes=new Jokes(myApi);
        assertEquals("Joke",jokes.getJokes());
    }
    @Test
    public void getJokesForException() throws Exception {
        MyApi myApi=Mockito.mock(MyApi.class);
        MyApi.GetJokes getJokes=Mockito.mock(MyApi.GetJokes.class);
        Mockito.when(myApi.getJokes()).thenReturn(getJokes);
        Mockito.when(getJokes.execute()).thenThrow(new IOException("Exception"));
        Jokes jokes=new Jokes(myApi);
        assertEquals("Exception",jokes.getJokes());
    }

}