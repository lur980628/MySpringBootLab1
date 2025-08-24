package com.rookies4.MySpringBootLab.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "book") // 순환 참조 방지
public class BookDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String language;
    private Integer pageCount;
    private String publisher;
    private String coverImageUrl;
    private String edition;

    // Book과의 1:1 관계 설정. BookDetail이 관계의 주인
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", unique = true)
    private Book book;
    
    // 생성자
    public BookDetail(String description, String language, Integer pageCount, String publisher, String coverImageUrl, String edition) {
        this.description = description;
        this.language = language;
        this.pageCount = pageCount;
        this.publisher = publisher;
        this.coverImageUrl = coverImageUrl;
        this.edition = edition;
    }
}