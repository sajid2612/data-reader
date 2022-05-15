package com.sajid.reader.datareader.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class DateTimeConfig {

	@Bean
	public Converter<String, LocalDateTime> stringDateConverter() {
		return new Converter<>() {
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			@Override
			public LocalDateTime convert(@NonNull String source) {
				return LocalDateTime.from(formatter.parse(source, LocalDateTime::from));
			}

		};
	}
}
