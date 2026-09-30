package com.inkrecords.service;

import com.inkrecords.dto.RecordRequest;
import com.inkrecords.model.RecordCategory;
import com.inkrecords.model.RecordEntry;
import com.inkrecords.repository.RecordRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RecordService {

    private final RecordRepository repository;
    private final Safelist contentSafelist = Safelist.relaxed()
            .preserveRelativeLinks(true)
            .addTags("h1", "h2", "h3", "hr")
            .addAttributes("img", "class")
            .addAttributes("p", "class")
            .addAttributes("ul", "class")
            .addAttributes("ol", "class")
            .addProtocols("img", "src", "http", "https");

    public RecordService(RecordRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<RecordEntry> findAll(RecordCategory category) {
        return category == null
                ? repository.findAllByOrderByUpdatedAtDesc()
                : repository.findAllByCategoryOrderByUpdatedAtDesc(category);
    }

    @Transactional(readOnly = true)
    public RecordEntry findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "记录不存在"));
    }

    @Transactional
    public RecordEntry create(RecordRequest request) {
        return repository.save(apply(new RecordEntry(), request));
    }

    @Transactional
    public RecordEntry update(Long id, RecordRequest request) {
        return repository.save(apply(findById(id), request));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "记录不存在");
        }
        repository.deleteById(id);
    }

    private RecordEntry apply(RecordEntry entry, RecordRequest request) {
        entry.setTitle(request.title().trim());
        entry.setCategory(request.category());
        entry.setTemplateKey(request.templateKey());
        entry.setContent(Jsoup.clean(request.content(), contentSafelist));
        return entry;
    }
}
