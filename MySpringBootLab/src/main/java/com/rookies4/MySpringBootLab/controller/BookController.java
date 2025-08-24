package com.rookies4.MySpringBootLab.controller;

import com.rookies4.MySpringBootLab.dto.BookDTO;
import com.rookies4.MySpringBootLab.service.BookService;
import com.rookies4.MySpringBootLab.dto.BookDTO;
import com.rookies4.MySpringBootLab.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // POST /api/books : 새 도서 등록
    @PostMapping
    public ResponseEntity<BookDTO.BookResponse> createBook(@Valid @RequestBody BookDTO.BookCreateRequest request) {
        BookDTO.BookResponse response = bookService.createBook(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/books : 모든 도서 조회
    @GetMapping
    public ResponseEntity<List<BookDTO.BookResponse>> getAllBooks() {
        List<BookDTO.BookResponse> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // GET /api/books/{id} : ID로 특정 도서 조회
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> getBookById(@PathVariable Long id) {
        BookDTO.BookResponse book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    // GET /api/books/isbn/{isbn} : ISBN으로 도서 조회
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.BookResponse> getBookByIsbn(@PathVariable String isbn) {
        BookDTO.BookResponse book = bookService.getBookByIsbn(isbn);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    // 저자로 책 검색
    @GetMapping("/search/author")
    public ResponseEntity<List<BookDTO.BookResponse>> searchBooksByAuthor(@RequestParam String author) {
        List<BookDTO.BookResponse> books = bookService.searchBooksByAuthor(author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // 제목으로 책 검색
    @GetMapping("/search/title")
    public ResponseEntity<List<BookDTO.BookResponse>> searchBooksByTitle(@RequestParam String title) {
        List<BookDTO.BookResponse> books = bookService.searchBooksByTitle(title);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // PUT /api/books/{id} : 도서 전체 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO.BookUpdateRequest request) {
        BookDTO.BookResponse response = bookService.updateBook(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PATCH /api/books/{id} : 도서 일부 필드 수정
    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO.BookResponse> patchBook(@PathVariable Long id, @RequestBody BookDTO.BookPatchRequest request) {
        BookDTO.BookResponse response = bookService.patchBook(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // PATCH /api/books/{id}/detail : 도서 상세 정보 일부 필드 수정
    @PatchMapping("/{id}/detail")
    public ResponseEntity<BookDTO.BookDetailResponse> patchBookDetail(@PathVariable Long id, @RequestBody BookDTO.BookDetailPatchRequest request) {
        BookDTO.BookDetailResponse response = bookService.patchBookDetail(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE /api/books/{id} : 도서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}