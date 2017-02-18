package com.idugalic.domain.blog;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface BlogPostRepository extends ReactiveSortingRepository<BlogPost, String>{
    
   
}
