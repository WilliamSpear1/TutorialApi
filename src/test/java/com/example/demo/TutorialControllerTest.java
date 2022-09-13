package com.example.demo;

import com.example.demo.model.Tutorial;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TutorialControllerTest {
  /*----------------------*/
  /* Arrange              */
  /*----------------------*/
  @LocalServerPort
   private int port;
   
   @Autowired
   private TestRestTemplate restTemplate;
    
    /*----------------------*/
    /* Act                  */
    /*----------------------*/
    @Test
    void given_Tutorial_Data_GetTutorial_List() {
        String baseUrl = "http://localhost:" + port + "/tutorials";
       // ResponseEntity<Tutorial> response = restTemplate.getForEntity(baseUrl, Tutorial[].class);
        
    }
    
    /*----------------------*/
    /* Assert               */
    /*----------------------*/
}