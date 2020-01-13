package com.galwaykart.testimonial;

public class TestimonialModel {

    String Id;
    String title;
    String testimonial_content;
    String rating;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTestimonial_content() {
        return testimonial_content;
    }

    public void setTestimonial_content(String testimonial_content) {
        this.testimonial_content = testimonial_content;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLast_page_number() {
        return last_page_number;
    }

    public void setLast_page_number(String last_page_number) {
        this.last_page_number = last_page_number;
    }

    public String getCur_page() {
        return cur_page;
    }

    public void setCur_page(String cur_page) {
        this.cur_page = cur_page;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    String creation_time;
    String author;
    String image;
    String last_page_number;
    String cur_page;
    String page_size;


}
