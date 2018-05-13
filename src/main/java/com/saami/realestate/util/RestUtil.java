package com.saami.realestate.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

/**
 * Created by sasiddi on 5/1/17.
 */
@Component
public class RestUtil {
    private static final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> doGet(final String url, final MultiValueMap<String, String> argument)
            throws Exception {
        try {
            final UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).queryParams(argument).build();
            return restTemplate.getForEntity(uri.toUriString(), String.class);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<String> doGet(final String url, final MultiValueMap<String, String> argument, MediaType mediaType)
            throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(mediaType));

            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            final UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).queryParams(argument).build();
            return restTemplate.getForEntity(uri.toUriString(), String.class);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<String> doPost(String url, Object request)
            throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        try {
            String jsonString = new ObjectMapper().writeValueAsString(request);
            return restTemplate.postForEntity(URI.create(url),
                    new HttpEntity<Object>(jsonString, header),
                    String.class);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
