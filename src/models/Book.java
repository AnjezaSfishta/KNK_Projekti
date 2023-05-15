package models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {

    private final SimpleStringProperty bookID;
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleStringProperty publisher;
    //private final SimpleIntegerProperty quantity;
    private final SimpleBooleanProperty availability;

    public Book(String bookId, String title, String author, String publisher, Boolean availability) {
        this.title = new SimpleStringProperty(title);
        this.bookID = new SimpleStringProperty(bookId);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        //this.quantity = new SimpleIntegerProperty(quantity);
        this.availability = new SimpleBooleanProperty(availability);
    }

    public String getBookID() {
        return bookID.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getPublisher() {
        return publisher.get();
    }

//    public Integer getQuantity() {
//        return quantity.get();
//    }

    public Boolean getAvailability() {
        return availability.get();
    }

}
