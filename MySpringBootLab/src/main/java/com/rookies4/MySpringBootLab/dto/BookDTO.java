package com.rookies4.MySpringBootLab.dto;

import com.rookies4.MySpringBootLab.entity.Book;
import com.rookies4.MySpringBootLab.entity.BookDetail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class BookDTO {

    // 도서 생성 요청을 위한 DTO (상세 정보 포함)
    @Getter
    @Setter
    public static class BookCreateRequest {
        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        private String title;

        @NotBlank(message = "저자는 필수 입력 항목입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수 입력 항목입니다.")
        @Pattern(regexp = "^(978|979)[0-9]{10}$", message = "유효하지 않은 ISBN 형식입니다. (예: 9781234567890)")
        private String isbn;

        @NotNull(message = "가격은 필수 입력 항목입니다.")
        private Integer price;

        @NotNull(message = "출판일은 필수 입력 항목입니다.")
        @PastOrPresent(message = "출판일은 현재 또는 과거 날짜여야 합니다.")
        private LocalDate publishDate;

        // @Valid를 사용하여 BookDetailRequest의 유효성 검증을 트리거합니다.
        @Valid
        @NotNull(message = "상세 정보는 필수 입력 항목입니다.")
        private BookDetailRequest detailRequest;

        public Book toEntity() {
            return new Book(this.title, this.author, this.isbn, this.price, this.publishDate);
        }
    }

    // 도서 정보 업데이트 요청을 위한 DTO (전체 업데이트)
    @Getter
    @Setter
    public static class BookUpdateRequest {
        private String title;
        private String author;
        private Integer price;
        private LocalDate publishDate;
    }

    // 도서 정보 부분 업데이트를 위한 DTO (Book 엔티티)
    @Getter
    @Setter
    public static class BookPatchRequest {
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailPatchRequest detailRequest;
    }

    // 도서 상세 정보 요청을 위한 DTO
    @Getter
    @Setter
    public static class BookDetailRequest {
        @NotBlank(message = "설명은 필수 입력입니다.")
        private String description;
        private String language;

        @NotNull(message = "페이지 수는 필수 입력입니다.")
        @Min(value = 1, message = "페이지 수는 1 이상이어야 합니다.")
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;

        public BookDetail toEntity() {
            return new BookDetail(this.description, this.language, this.pageCount, this.publisher, this.coverImageUrl, this.edition);
        }
    }

    // 도서 상세 정보 부분 업데이트를 위한 DTO
    @Getter
    @Setter
    public static class BookDetailPatchRequest {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    // 클라이언트에게 반환될 도서 응답 DTO
    @Getter
    @Setter
    @Builder
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailResponse detailResponse;

        public static BookResponse fromEntity(Book book) {
            BookResponseBuilder builder = BookResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate());

            // bookDetail이 존재할 경우에만 detailResponse를 추가
            if (book.getBookDetail() != null) {
                builder.detailResponse(BookDetailResponse.fromEntity(book.getBookDetail()));
            }
            return builder.build();
        }
    }

    // 클라이언트에게 반환될 도서 상세 정보 응답 DTO
    @Getter
    @Setter
    @Builder
    public static class BookDetailResponse {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;

        public static BookDetailResponse fromEntity(BookDetail detail) {
            return BookDetailResponse.builder()
                    .description(detail.getDescription())
                    .language(detail.getLanguage())
                    .pageCount(detail.getPageCount())
                    .publisher(detail.getPublisher())
                    .coverImageUrl(detail.getCoverImageUrl())
                    .edition(detail.getEdition())
                    .build();
        }
    }
}