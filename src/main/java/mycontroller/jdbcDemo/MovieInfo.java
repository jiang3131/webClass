package mycontroller.jdbcDemo;

public class MovieInfo {
    Long movieId;
    String movieName;
    String MovieEnglisgName;
    String movieImage;
    Float movieRating;
    Integer movieLength;
    String moviePublishiTime;
    String movieYear;

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieEnglisgName() {
        return MovieEnglisgName;
    }

    public void setMovieEnglisgName(String movieEnglisgName) {
        MovieEnglisgName = movieEnglisgName;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public Float getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(Float movieRating) {
        this.movieRating = movieRating;
    }

    public Integer getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(Integer movieLength) {
        this.movieLength = movieLength;
    }

    public String getMoviePublishiTime() {
        return moviePublishiTime;
    }

    public void setMoviePublishiTime(String moviePublishiTime) {
        this.moviePublishiTime = moviePublishiTime;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }
}
