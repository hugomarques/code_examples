package com.hugomarques.unreliable;


import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ServerApp {

	private Logger logger = LoggerFactory.getLogger(ServerApp.class);

	@RequestMapping("/")
	public ResponseEntity<String> home() throws InterruptedException {
		final var now = LocalDateTime.now();
		logger.info("Failure flag value: " + now);
		// Sempre envie um erro durante os primeiros 5s de cada minuto.
		if (now.getSecond() >= 0 && now.getSecond() <= 5)
			throw new IllegalStateException();
		return ResponseEntity.status(200).body("Current time: " + now);
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApp.class, args);
	}

}
