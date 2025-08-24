// src/main/java/com/example/demo/exception/ErrorObject.java
package com.rookies4.MySpringBootLab.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorObject {

    private Integer statusCode;
    private String message;
    private LocalDateTime timestamp;
}