package com.etr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.etr.mapper")
public class EtrApplication {

	public static void main(String[] args) {
		SpringApplication.run(EtrApplication.class, args);
	}

}
