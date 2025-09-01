package org.example.db.News;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {
    @Autowired
    NewsRepository newsRepository;

    @Transactional
    public void saveNews(News news) {
        newsRepository.save(news);
    }

    @Transactional(readOnly = true)
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<NewsDto> getLatestNews() {
        Pageable pageable = PageRequest.of(0,5, Sort.by("dateOfPublication").descending());
        List<News> list = newsRepository.findAll(pageable).getContent();
        return newsToDto(list);
    }

    private List<NewsDto> newsToDto(List<News> news){
        return news.stream().map(data -> new NewsDto(data)).collect(Collectors.toList());
    }
}
