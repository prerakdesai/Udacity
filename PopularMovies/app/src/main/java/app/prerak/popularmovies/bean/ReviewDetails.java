package app.prerak.popularmovies.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Prerak on 5/9/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewDetails implements Serializable {
    String author;
    String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
