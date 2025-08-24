package com.rookies4.MySpringBootLab.service;

import com.rookies4.MySpringBootLab.dto.BookDTO;
import com.rookies4.MySpringBootLab.entity.Book;
import com.rookies4.MySpringBootLab.entity.BookDetail;
import com.rookies4.MySpringBootLab.exception.BusinessException;
import com.rookies4.MySpringBootLab.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Transactional // 쓰기 작업에만 별도로 트랜잭션 설정
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {
        // ISBN 중복 검증 로직
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("이미 등록된 ISBN입니다: " + request.getIsbn(), HttpStatus.CONFLICT);
        }
        Book newBook = request.toEntity();
        BookDetail newBookDetail = request.getDetailRequest().toEntity();
        newBook.addBookDetail(newBookDetail); // 연관관계 설정

        Book savedBook = bookRepository.save(newBook);
        return BookDTO.BookResponse.fromEntity(savedBook);
    }

    public List<BookDTO.BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public BookDTO.BookResponse getBookById(Long id) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("ID가 " + id + "인 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.fromEntity(book);
    }

    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException("ISBN이 " + isbn + "인 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.fromEntity(book);
    }

    public List<BookDTO.BookResponse> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContaining(author).stream()
                .map(BookDTO.BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<BookDTO.BookResponse> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContaining(title).stream()
                .map(BookDTO.BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        Book existBook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ID가 " + id + "인 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // ISBN 중복 검증
        if (!existBook.getIsbn().equals(request.getIsbn()) && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("이미 사용 중인 ISBN입니다.", HttpStatus.CONFLICT);
        }

        existBook.setTitle(request.getTitle());
        existBook.setAuthor(request.getAuthor());
        existBook.setIsbn(request.getIsbn());
        existBook.setPrice(request.getPrice());
        existBook.setPublishDate(request.getPublishDate());

        // BookDetail 업데이트 (전체 업데이트)
        BookDetail detail = existBook.getBookDetail();
        if (detail != null && request.getDetailRequest() != null) {
            BookDTO.BookDetailRequest detailRequest = request.getDetailRequest();
            detail.setDescription(detailRequest.getDescription());
            detail.setLanguage(detailRequest.getLanguage());
            detail.setPageCount(detailRequest.getPageCount());
            detail.setPublisher(detailRequest.getPublisher());
            detail.setCoverImageUrl(detailRequest.getCoverImageUrl());
            detail.setEdition(detailRequest.getEdition());
        }

        Book updatedBook = bookRepository.save(existBook);
        return BookDTO.BookResponse.fromEntity(updatedBook);
    }

    @Transactional
    public BookDTO.BookResponse patchBook(Long id, BookDTO.BookPatchRequest request) {
        Book existBook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ID가 " + id + "인 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // Book 필드 부분 업데이트
        if (request.getTitle() != null) {
            existBook.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            existBook.setAuthor(request.getAuthor());
        }
        if (request.getIsbn() != null) {
            // ISBN 변경 시 중복 검증
            if (!existBook.getIsbn().equals(request.getIsbn()) && bookRepository.existsByIsbn(request.getIsbn())) {
                throw new BusinessException("이미 사용 중인 ISBN입니다.", HttpStatus.CONFLICT);
            }
            existBook.setIsbn(request.getIsbn());
        }
        if (request.getPrice() != null) {
            existBook.setPrice(request.getPrice());
        }
        if (request.getPublishDate() != null) {
            existBook.setPublishDate(request.getPublishDate());
        }

        // BookDetail 필드 부분 업데이트
        BookDetail detail = existBook.getBookDetail();
        if (detail != null && request.getDetailRequest() != null) {
            BookDTO.BookDetailPatchRequest detailRequest = request.getDetailRequest();
            if (detailRequest.getDescription() != null) {
                detail.setDescription(detailRequest.getDescription());
            }
            if (detailRequest.getLanguage() != null) {
                detail.setLanguage(detailRequest.getLanguage());
            }
            if (detailRequest.getPageCount() != null) {
                detail.setPageCount(detailRequest.getPageCount());
            }
            if (detailRequest.getPublisher() != null) {
                detail.setPublisher(detailRequest.getPublisher());
            }
            if (detailRequest.getCoverImageUrl() != null) {
                detail.setCoverImageUrl(detailRequest.getCoverImageUrl());
            }
            if (detailRequest.getEdition() != null) {
                detail.setEdition(detailRequest.getEdition());
            }
        }

        Book updatedBook = bookRepository.save(existBook);
        return BookDTO.BookResponse.fromEntity(updatedBook);
    }

    @Transactional
    public BookDTO.BookDetailResponse patchBookDetail(Long id, BookDTO.BookDetailPatchRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ID가 " + id + "인 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        BookDetail detail = book.getBookDetail();
        if (detail == null) {
            throw new BusinessException("ID가 " + id + "인 도서의 상세 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        // BookDetail 필드 부분 업데이트
        if (request.getDescription() != null) {
            detail.setDescription(request.getDescription());
        }
        if (request.getLanguage() != null) {
            detail.setLanguage(request.getLanguage());
        }
        if (request.getPageCount() != null) {
            detail.setPageCount(request.getPageCount());
        }
        if (request.getPublisher() != null) {
            detail.setPublisher(request.getPublisher());
        }
        if (request.getCoverImageUrl() != null) {
            detail.setCoverImageUrl(request.getCoverImageUrl());
        }
        if (request.getEdition() != null) {
            detail.setEdition(request.getEdition());
        }

        return BookDTO.BookDetailResponse.fromEntity(detail);
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException("ID가 " + id + "인 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
    }
}