package ru.productstar.outcoming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.productstar.outcoming.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class Real_Test {
	@Autowired
	private UserService userService;

	@Test
	void testSaveAndGetUser() {
		User saved = userService.saveUser("Test");
		User found = userService.getUser(saved.getId());
		assertEquals("Test", found.getName());
	}
}