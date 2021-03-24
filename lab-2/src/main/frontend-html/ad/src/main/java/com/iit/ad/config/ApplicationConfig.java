package com.iit.ad.config;

import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@EnableWebMvc
public class ApplicationConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
        gsonConverter.setGson(gson());
        converters.add(gsonConverter);
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (localDate, type, context)
                                -> new JsonPrimitive(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .registerTypeAdapter(LocalDate.class,
                        (JsonDeserializer<LocalDate>) (jsonElement, type, context)
                                -> LocalDate.parse(jsonElement.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .create();
    }
}
