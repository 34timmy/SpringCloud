package com.example.gateway;

import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GatewayApplicationTests {

    TestRestTemplate testRestTemplate = new TestRestTemplate();
    String testUrl = "http://localhost:8080";

    @Test
    public void testAccess() {


        ResponseEntity<String> response = testRestTemplate
                .getForEntity(testUrl + "/book-service/books", String.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());


//
    }

    @Test
    public void response() {
        ResponseEntity<String> response;
        response = testRestTemplate
                .getForEntity(testUrl + "/book-service/books/1", String.class);
        Assert.assertEquals(HttpStatus.FOUND, response.getStatusCode());
        Assert.assertEquals("http://localhost:8080/login", response.getHeaders()
                .get("Location").get(0));

    }

    @Test
    public void auth() {
        ResponseEntity<String> response;

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", "user");
        form.add("password", "password");
        response = testRestTemplate
                .postForEntity(testUrl + "/login", form, String.class, 1);

        String sessionCookie = response.getHeaders().get("Set-Cookie")
                .get(0).split(";")[0];
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionCookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        response = testRestTemplate.exchange(testUrl + "/book-service/books/1",
                HttpMethod.GET, httpEntity, String.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }

}
