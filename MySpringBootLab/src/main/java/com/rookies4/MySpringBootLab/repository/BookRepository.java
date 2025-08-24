package com.rookies4.MySpringBootLab.repository;

import com.rookies4.MySpringBootLab.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // ISBN으로 도서 조회
    Optional<Book> findByIsbn(String isbn);

    // 저자명으로 도서 목록 조회
    List<Book> findByAuthor(String author);

    // ISBN으로 도서 존재 여부 확인
    boolean existsByIsbn(String isbn);

    // 저자명에 특정 문자열이 포함된 도서 목록 조회
    List<Book> findByAuthorContaining(String author);

    // 제목에 특정 문자열이 포함된 도서 목록 조회
    List<Book> findByTitleContaining(String title);

    // ID로 도서와 관련 BookDetail을 함께 조회 (FETCH JOIN)
    @Query("SELECT b FROM Book b JOIN FETCH b.bookDetail WHERE b.id = :id")
    Optional<Book> findByIdWithBookDetail(@Param("id") Long id);

    // ISBN으로 도서와 관련 BookDetail을 함께 조회 (FETCH JOIN)
    @Query("SELECT b FROM Book b JOIN FETCH b.bookDetail WHERE b.isbn = :isbn")
    Optional<Book> findByIsbnWithBookDetail(@Param("isbn") String isbn);
}