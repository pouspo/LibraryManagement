package models;

public class ModelTable {
    String name, author, pub_date;
    int bookID;

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public ModelTable(String name, String author, String pub_date, int bookID) {
        this.name = name;
        this.author = author;
        this.pub_date = pub_date;
        this.bookID = bookID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPub_date() {
        return pub_date;
    }

    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }
}