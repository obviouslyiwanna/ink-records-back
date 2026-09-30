package com.inkrecords.dto;

import com.inkrecords.model.RecordCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecordRequest(
        @NotBlank @Size(max = 120) String title,
        @NotNull RecordCategory category,
        @Size(max = 40) String templateKey,
        @NotBlank String content
) {
}
