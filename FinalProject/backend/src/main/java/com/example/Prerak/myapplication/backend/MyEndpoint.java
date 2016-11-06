/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.Prerak.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.Prerak.example.com",
                ownerName = "backend.myapplication.Prerak.example.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    private static int i=0;

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("How are you, " + name);

        return response;
    }

    @ApiMethod(name = "getJokes")
    public MyBean getJokes() {
        MyBean response = new MyBean();
        response.setData("Joke " + i++);

        return response;
    }

}
