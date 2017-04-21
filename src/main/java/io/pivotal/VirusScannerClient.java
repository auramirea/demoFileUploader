package io.pivotal;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
//@Configuration
//@ConfigurationProperties(prefix="virusServiceConfig")
public class VirusScannerClient {
    RestTemplate restTemplate;
    private String serviceUri;
    private String username;
    private String password;

    @Autowired
    public VirusScannerClient(final Environment environment, final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
//        this.serviceUri = environment.getProperty("vcap.services.free-virusscanner.credentials.servicePath");
//        this.password = environment.getProperty("vcap.services.free-virusscanner.credentials.password");
//        this.username = environment.getProperty("vcap.services.free-virusscanner.credentials.username");
        this.serviceUri = "http://localhost:8000/scan";
        this.password = "asdkjhagsdjhbas-password-askduhsukeh";
        this.username = "asdlfasdlfh-user-asdouhasdui";
    }

    public boolean hasVirus(final MultipartFile file) {


        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        try {
            List<Object> files = new ArrayList<>();
            files.add(new ByteArrayResource(file.getBytes()));
            parameters.add("file", files);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parameters.add("filename", file.getOriginalFilename());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        addBasicAuthHeader(headers);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = restTemplate.exchange(serviceUri, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            return false;
        }
        return Boolean.parseBoolean(response.getBody());
    }

    private void addBasicAuthHeader(final HttpHeaders headers) {
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        headers.add("Authorization", "Basic " + base64Creds);
    }

    public String getServiceUri() {
        return serviceUri;
    }

    public void setServiceUri(String serviceUri) {
        this.serviceUri = serviceUri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
