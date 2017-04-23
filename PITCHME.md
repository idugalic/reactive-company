### Reactive Programming
### And
### Reactive Systems

<span style="color:gray">Lab</span>

---

### Reactive Programming

is about non-blocking applications that are:

  - asynchronous
  - message-driven
  - require a small number of threads to scale vertically
  - use backpressure - which is a mechanism to ensure producers don’t overwhelm consumers

+++

### Reactive Programming
#### From imperative to declarative async composition of logic

<span style="color:gray">Service</span>

```java
@GetMapping("/blogposts")
Flux<BlogPost> list() {
	LOG.info("Received request: BlogPost - List");
	try {
		return this.blogPostRepository.findAll().log();
	} finally {
		LOG.info("Request pocessed: BlogPost - List");
	}
}
```

+++

### Reactive Programming
#### From imperative to declarative async composition of logic

<span style="color:gray">Log</span>

A possible log output we could see is:
![Log - Reactive](assets/logs-reactive.png?raw=true)

As we can see the output of the controller method is evaluated after its execution in a different thread too!

+++

### Reactive Programming

<span style="color:gray">Summary</span>

<ol>
	<li class="fragment" data-fragment-index="1">We can no longer think in terms of a linear execution model where one request is handled by one thread</li>
	<li class="fragment" data-fragment-index="2">The reactive streams will be handled by a lot of threads in their lifecycle</li>
	<li class="fragment" data-fragment-index="3">We no longer can rely on thread affinity for things like the security context or transaction handling</li>
</ol>

---

### Reactive Systems

are:

  - responsive
  - resilient
  - elastic
  - message driven
  
![Reactive Traits](assets/reactive-traits.png?raw=true)

+++

### Reactive Systems

<span style="color:gray">Responsive</span>

  - The system responds in a timely manner if at all possible.

+++

### Reactive Systems

<span style="color:gray">Resilient</span>

  - The system stays responsive in the face of failure.

+++

### Reactive Systems
 
<span style="color:gray">Elastic</span>

  - The system stays responsive under varying workload.

+++

### Reactive Systems

<span style="color:gray">Message Driven</span>

  - Reactive Systems rely on asynchronous message-passing to establish a boundary between components that ensures loose coupling, isolation and location transparency.

+++

### Reactive Systems

<span style="color:gray">Summary</span>

<ol>
	<li class="fragment" data-fragment-index="1">Reactive programming offers productivity for developers—through performance and resource efficiency—at the component level for internal logic and dataflow transformation</li>
	<li class="fragment" data-fragment-index="2">Reactive systems offer productivity for architects and DevOps practitioners—through resilience and elasticity—at the system level</li>
</ol>

---

### Why now?

The promise of Reactive is that you can do more with less, specifically you can process higher loads with fewer threads. This days we are dealing with more data!

  - For the right problem, the effects are dramatic. 
  - For the wrong problem, the effects might go into reverse (you actually make things worse).

---

### Load And Performance testing

Is your web application responsive? <span style="color:gray">There is only one way to know this: test your web application!</span>

```bash
$ ./mvnw gatling:execute
```




