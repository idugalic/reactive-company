package com.idugalic;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

import com.idugalic.domain.blog.BlogPost;
import com.idugalic.domain.blog.BlogPostRepository;

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableWebFlux
@EnableMongoAuditing
public class ReactiveComapnyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveComapnyApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(BlogPostRepository blogPostRepository) {
		return (p) -> {

			blogPostRepository.deleteAll().block();
			blogPostRepository.save(new BlogPost("authorId1", "title1", "content1", "tagString1")).block();
			blogPostRepository.save(new BlogPost("authorId2", "title2", "content2", "tagString2")).block();
			blogPostRepository.save(new BlogPost("authorId3", "title3", "content3", "tagString3")).block();
			blogPostRepository.save(new BlogPost("authorId4", "title4", "content4", "tagString4")).block();
		};
	}
}
