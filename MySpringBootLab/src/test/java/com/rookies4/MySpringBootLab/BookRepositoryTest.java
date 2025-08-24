// src/test/java/com/example/demo/BookRepositoryTest.java
package com.rookies4.MySpringBootLab;

import com.rookies4.MySpringBootLab.entity.Book;
import com.rookies4.MySpringBootLab.BookRepositoryTest;
import com.rookies4.MySpringBootLab.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book testBook1;
    private Book testBook2;

    @BeforeEach
    public void setUp() {
        // 테스트 데이터 정리 및 준비
        bookRepository.deleteAll(); // 각 테스트 실행 전에 기존 데이터 삭제
        
        testBook1 = new Book("스프링 부트 입문", "홍길동", "9788956746425", 30000, LocalDate.of(2025, 5, 7));
        testBook2 = new Book("JPA 프로그래밍", "박둘리", "9788956746432", 35000, LocalDate.of(2025, 4, 30));
    }

    @Test
    @DisplayName("도서 등록 테스트")
    public void testCreateBook() {
        // Given: 테스트 데이터 (setUp에서 준비)
        
        // When: 도서 저장
        Book savedBook = bookRepository.save(testBook1);

        // Then: 저장된 도서가 null이 아니고 ID가 있는지 확인
        Assertions.assertNotNull(savedBook);
        Assertions.assertNotNull(savedBook.getId());
        Assertions.assertEquals("스프링 부트 입문", savedBook.getTitle());
    }

    @Test
    @DisplayName("ISBN으로 도서 조회 테스트")
    public void testFindByIsbn() {
        // Given: 도서를 먼저 저장
        bookRepository.save(testBook1);

        // When: ISBN으로 도서 조회
        Optional<Book> foundBook = bookRepository.findByIsbn("9788956746425");

        // Then: 도서가 존재하며 제목이 일치하는지 확인
        Assertions.assertTrue(foundBook.isPresent());
        Assertions.assertEquals("스프링 부트 입문", foundBook.get().getTitle());
    }

    @Test
    @DisplayName("저자명으로 도서 목록 조회 테스트")
    public void testFindByAuthor() {
        // Given: 두 도서를 모두 저장
        bookRepository.save(testBook1);
        bookRepository.save(testBook2);

        // When: 저자명으로 도서 목록 조회
        List<Book> booksByAuthor = bookRepository.findByAuthor("홍길동");

        // Then: 결과가 하나이고 제목이 일치하는지 확인
        Assertions.assertEquals(1, booksByAuthor.size());
        Assertions.assertEquals("스프링 부트 입문", booksByAuthor.get(0).getTitle());
    }

    @Test
    @DisplayName("도서 정보 수정 테스트")
    public void testUpdateBook() {
        // Given: 도서 저장
        Book savedBook = bookRepository.save(testBook1);

        // When: 도서 정보 수정 (가격 변경)
        savedBook.setPrice(45000);
        Book updatedBook = bookRepository.save(savedBook);

        // Then: 변경된 정보가 올바르게 저장되었는지 확인
        Assertions.assertEquals(45000, updatedBook.getPrice());
    }

    @Test
    @DisplayName("도서 삭제 테스트")
    public void testDeleteBook() {
        // Given: 도서 저장
        Book savedBook = bookRepository.save(testBook1);

        // When: 도서 삭제
        bookRepository.delete(savedBook);

        // Then: 삭제 후 도서가 존재하지 않는지 확인
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());
        Assertions.assertFalse(foundBook.isPresent());
    }
}