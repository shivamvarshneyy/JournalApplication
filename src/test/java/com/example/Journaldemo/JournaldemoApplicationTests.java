package com.example.Journaldemo;

import com.example.Journaldemo.entity.User;
import com.example.Journaldemo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class JournaldemoApplicationTests {

	@Test
	void contextLoads() {
	}

}


/*package com.example.Journaldemo;

import com.example.Journaldemo.repository.UserRepository;
import com.example.Journaldemo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JournaldemoApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void testFindByUsername() {
		User user = userRepository.findByusername("shivam");
		assertNotNull(user, "User should not be null");
		assertEquals("shivam", user.getUsername());
	}
}*/
