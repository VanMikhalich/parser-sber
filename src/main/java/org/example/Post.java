package org.example;

public class Post {
    private String title;
    private String detailsLink;
    private String author;
    private String authorDetailsLink;
    private String dateOfCreated;

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", detailsLink='" + detailsLink + '\'' +
                ", author='" + author + '\'' +
                ", getDetailsLink='" + authorDetailsLink + '\'' +
                ", dateOfCreated='" + dateOfCreated + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailsLink() {
        return detailsLink;
    }

    public void setDetailsLink(String detailsLink) {
        this.detailsLink = detailsLink;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorDetailsLink() {
        return authorDetailsLink;
    }

    public void setAuthorDetailsLink(String authorDetailsLink) {
        this.authorDetailsLink = authorDetailsLink;
    }

    public String getDateOfCreated() {
        return dateOfCreated;
    }

    public void setDateOfCreated(String dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }
}
