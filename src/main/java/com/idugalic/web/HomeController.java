package com.idugalic.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.idugalic.domain.blog.BlogPost;
import com.idugalic.domain.blog.BlogPostRepository;
import com.idugalic.domain.project.Project;
import com.idugalic.domain.project.ProjectRepository;

import reactor.core.publisher.Flux;
/**
 * 
 * @author idugalic
 * 
 * An index/home controller.
 *
 */
@Controller
public class HomeController {

    private final BlogPostRepository blogPostRepository;
    private final ProjectRepository projectRepository;

    public HomeController(final BlogPostRepository blogPostRepository, final ProjectRepository projectRepository) {
        super();
        this.blogPostRepository = blogPostRepository;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/")
    public String home(final Model model) {

        // Get the stream of BlogPost objects. In this case this works as the reactive
        // equivalent to getting a List<BlogPost>. Being reactive, it won't be resolved
        // until really needed (just before rendering the HTML)
        final Flux<BlogPost> blogPostStream = this.blogPostRepository.findAll().log();

        // By adding this Flux directly to the model (without wrapping) we are indicating that
        // we want this variable to be completely resolved by Spring WebFlux (without blocking)
        // before Thymeleaf starts the rendering of the HTML template. That way, this variable
        // will have for the Thymeleaf engine the exact same appearance as a List<BlogPost>.
        model.addAttribute("blogPosts", blogPostStream);
        
        final Flux<Project> projectStream = this.projectRepository.findAll().log();
        model.addAttribute("projects", projectStream);

        // Return the template name (templates/home.html)
        return "home";

    }

}
