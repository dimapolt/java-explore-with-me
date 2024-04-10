package ru.practicum.ewm.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.dto.StatDtoOut;

import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity<List<StatDtoOut>> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendStat(HttpMethod.GET, path, parameters);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, null, body);
    }

    private ResponseEntity<List<StatDtoOut>> makeAndSendStat(HttpMethod method, String path, @Nullable Map<String, Object> parameters) {
        HttpEntity<List<StatDtoOut>> requestEntity = new HttpEntity<>(null, getHeaders());
        ResponseEntity<List<StatDtoOut>> statsServiceResponse;
        try {
            if (parameters != null) {
                statsServiceResponse = rest.exchange(path, method, requestEntity,
                        new ParameterizedTypeReference<List<StatDtoOut>>() {
                        }, parameters);
            } else {
                statsServiceResponse = rest.exchange(path, method, requestEntity,
                        new ParameterizedTypeReference<List<StatDtoOut>>() {
                        });
            }
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(e.getMessage());
        }
        return prepareStatResponse(statsServiceResponse);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method,
                                                          String path,
                                                          @Nullable Map<String, Object> parameters,
                                                          @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, getHeaders());

        ResponseEntity<Object> shareitServerResponse;
        try {
            if (parameters != null) {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(shareitServerResponse);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    private static ResponseEntity<List<StatDtoOut>> prepareStatResponse(ResponseEntity<List<StatDtoOut>> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }
}
