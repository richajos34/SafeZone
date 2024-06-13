package com.berkeley.irms.warnme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ComponentScan(basePackages = "com.berkeley.irms")
public class WarnmeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarnmeApplication.class, args);
	}

}
