package org.anonymous.global.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeansConfig {

    @Lazy
    @Bean
    public RestTemplate restTemplate() {

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        return restTemplate;
    }

    /**
     * 커맨드 객체 값 옮겨주는 (GET & SET)
     *
     * Reflection API 기능
     *
     * 매개변수 - Class 클래스 객체
     *
     * @return
     */
    @Lazy
    @Bean
    public ModelMapper modelMapper() {

        ModelMapper mapper = new ModelMapper();

        // 매칭 안되는 타입(자료형이 일치하지 않을 경우)은 PASS 하는 설정
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper;
    }

    /**
     * Rest Control 의 주 역할은 JSON 형태 응답 Return
     *
     * JSON </-> JAVA code
     *
     * 매개변수 - Class 클래스 객체
     *
     * @return
     */
    @Lazy
    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper om = new ObjectMapper();

        // java.time.package = java8 data & time api
        om.registerModule(new JavaTimeModule());

        return om;
    }
}