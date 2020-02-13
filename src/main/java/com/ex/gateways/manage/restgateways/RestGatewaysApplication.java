package com.ex.gateways.manage.restgateways;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;

import javax.sql.DataSource;

@SpringBootApplication
public class RestGatewaysApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestGatewaysApplication.class, args);
	}

	@Bean(value = "datasource")
	@ConfigurationProperties("spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
