package com.roro.random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestRandomIdeaApplication {

	public static void main(String[] args) {
		SpringApplication.from(RandomPlaceApplication::main).with(TestRandomIdeaApplication.class).run(args);
	}

}
