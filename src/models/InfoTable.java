package models;

public class InfoTable {
    int bookId;
    String name;
    String date;
    int fine;

    public InfoTable(int bookId, String name, String date, Integer fine) {
        this.bookId = bookId;
        this.name = name;
        this.date = date;
        this.fine = fine;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFine() {return fine;}

    public void setFine(int fine) {
        this.fine = fine;
    }
}
