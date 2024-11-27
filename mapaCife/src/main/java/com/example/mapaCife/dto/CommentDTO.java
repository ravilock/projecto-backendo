package com.example.mapaCife.dto;

import java.util.Date;
import java.util.UUID;

public record CommentDTO(UUID id, Date createdAt, String body, String author) {
}
