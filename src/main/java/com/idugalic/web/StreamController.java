package com.idugalic.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.idugalic.domain.blog.BlogPost;
import com.idugalic.domain.blog.BlogPostRepository;
import com.idugalic.domain.project.Project;
import com.idugalic.domain.project.ProjectRepository;

import reactor.core.publisher.Flux;

/**
 * 
 * @author idugalic
 * 
 * An streaming / Server-Sent events controller.
 *
 */
@Controller
@RequestMapping("/stream")
public class StreamController {

	private final BlogPostRepository blogPostRepository;
	private final ProjectRepository projectRepository;

	public StreamController(final BlogPostRepository blogPostRepository, final ProjectRepository projectRepository) {
		super();
		this.blogPostRepository = blogPostRepository;
		this.projectRepository = projectRepository;
	}
	
	@GetMapping
	public String stream(final Model model) {
		return "sse";
	}

	@GetMapping(value = "/blog",  produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public String blog(final Model model) {

		// Get the stream of BlogPost objects.
		final Flux<BlogPost> blogPostStream = this.blogPostRepository.findAll().log();
		model.addAttribute("blogPosts", new ReactiveDataDriverContextVariable(blogPostStream, 1000));
		
		// Will use the same "sse" template, but only a fragment: #blogTableBody
		return "sse :: #blogTableBody";
	}
	
	@GetMapping(value = "/project",  produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public String project(final Model model) {

		// Get the stream of Project objects.
		final Flux<Project> projectStream = this.projectRepository.findAll().log();
		model.addAttribute("projects", new ReactiveDataDriverContextVariable(projectStream, 1000));

		// Will use the same "sse" template, but only a fragment: #projectTableBody
		return "sse :: #projectTableBody";
	}


}
