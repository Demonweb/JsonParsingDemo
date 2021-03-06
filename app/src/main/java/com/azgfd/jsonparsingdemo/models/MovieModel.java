package com.azgfd.jsonparsingdemo.models;

import java.util.List;

/**
 * Created by mpliszka on 8/29/2016.
 */
public class MovieModel {

    private String movie;
    private int year;
    private float rating;
    private String duration;
    private String director;
    private String tagline;
    private String image;
    private String story;

    private List<Cast> castList;

    public List<Cast> getCastList() {
        return castList;
    }

    public void setCastList(List<Cast> castList) {
        this.castList = castList;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public static class Cast{
        private String name;

        public String getName(){
            return  name;
        }

        public void setName(String name){
            this.name = name;
        }



    }

}
