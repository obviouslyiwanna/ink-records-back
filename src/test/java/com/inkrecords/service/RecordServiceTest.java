package com.inkrecords.service;

import com.inkrecords.dto.RecordRequest;
import com.inkrecords.model.RecordCategory;
import com.inkrecords.model.RecordEntry;
import com.inkrecords.repository.RecordRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecordServiceTest {

    private final RecordRepository repository = mock(RecordRepository.class);
    private final RecordService service = new RecordService(repository);

    @Test
    void createTrimsTitleAndSanitizesContent() {
        when(repository.save(any(RecordEntry.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RecordEntry saved = service.create(new RecordRequest(
                "  今日工作  ",
                RecordCategory.WORK,
                "work-daily",
                "<h2>完成</h2><script>alert('x')</script><p>发布功能</p>"
        ));

        assertThat(saved.getTitle()).isEqualTo("今日工作");
        assertThat(saved.getContent())
                .contains("<h2>完成</h2>")
                .contains("<p>发布功能</p>")
                .doesNotContain("<script>");
    }
}
