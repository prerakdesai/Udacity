package app.prerak.popularmovies.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Prerak on 5/9/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrailerDetails implements Serializable {
    String key;
    String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
