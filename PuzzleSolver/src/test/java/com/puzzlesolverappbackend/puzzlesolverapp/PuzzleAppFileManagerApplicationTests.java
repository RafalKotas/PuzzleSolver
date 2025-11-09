package com.puzzlesolverappbackend.puzzlesolverapp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class PuzzleAppFileManagerApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	@DisplayName("Should load context")
	void contextLoads() {
		assertThat(context).isNotNull();
	}

	@Test
	@DisplayName("Should run main method without exceptions")
	void mainMethodShouldRunWithoutExceptions() {
		// given
		String[] args = {};

		// when + then
		assertDoesNotThrow(() -> PuzzleAppFileManagerApplication.main(args));
	}
}
