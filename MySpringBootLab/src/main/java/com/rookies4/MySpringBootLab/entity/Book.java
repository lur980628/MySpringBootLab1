package com.rookies4.MySpringBootLab.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "bookDetail") // 순환 참조를 방지하기 위해 bookDetail 필드를 제외합니다.
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private Integer price;
    private LocalDate publishDate;

    // BookDetail과의 1:1 관계 설정
    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookDetail bookDetail;

    public Book(String title, String author, String isbn, Integer price, LocalDate publishDate) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.publishDate = publishDate;
    }

    /**
     * BookDetail 엔티티와의 연관관계를 설정하는 편의 메서드입니다.
     * 양쪽 엔티티의 관계를 모두 설정하여 데이터 일관성을 유지합니다.
     * @param bookDetail Book과 연결할 BookDetail 엔티티
     */
    public void addBookDetail(BookDetail bookDetail) {
        this.bookDetail = bookDetail;
        if (bookDetail != null && bookDetail.getBook() != this) {
            bookDetail.setBook(this);
        }
    }
}
