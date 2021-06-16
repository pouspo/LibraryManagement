package models;

public class Book {
    int bookID;
    String bookName;

    public Book(int bookID, String bookName) {
        this.bookID = bookID;
        this.bookName = bookName;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return ("[" + this.bookID + "] " + this.bookName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.bookID == ((Book) obj).bookID && this.bookName == ((Book) obj).bookName;
    }
}