package com.example.demo;

import com.example.demo.controller.TutorialController;
import com.example.demo.model.Tutorial;
import com.example.demo.repository.TutorialRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TutorialController.class)
public class TutorialControllerTest {
    @MockBean
    private TutorialRepository tutorialRepository;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void should_create_tutorial() throws Exception {
        Tutorial tutorial = new Tutorial("Spring Boot @WebMvcTest", "Description", true);
        
        mockMvc.perform(post("/api/tutorials").contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(tutorial)))
          .andExpect(status().isCreated())
          .andDo(print());
    }
    
    @Test
    void should_return_tutorial() throws Exception {
        Tutorial tutorial = new Tutorial("Spring Boot @WebMvcTest", "Description", true);
        
        when(tutorialRepository.findById(tutorial.getId())).thenReturn(Optional.of(tutorial));
        
        mockMvc.perform(get("/api/tutorials/{id}", tutorial.getId()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(tutorial.getId()))
          .andExpect(jsonPath("$.title").value(tutorial.getTitle()))
          .andExpect(jsonPath("$.description").value(tutorial.getDescription()))
          .andExpect(jsonPath("$.published").value(tutorial.isPublished()))
          .andDo(print());
    }
    
    @Test
    void should_return_not_found_tutorial() throws Exception {
        long id = 1L;
        
        when(tutorialRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/tutorials/{id}", id))
          .andExpect(status().isNotFound())
          .andDo(print());
    }
    
    @Test
    void should_return_list_of_tutorials() throws Exception {
        List<Tutorial> tutorialList = new ArrayList<>(
          Arrays.asList(new Tutorial("SpringBoot", "Description",true),
                        new Tutorial("SpringBoot2", "Description2", true),
                        new Tutorial("SpringBoot3", "Description3", true)
          )
        );
        
        when(tutorialRepository.findAll()).thenReturn(tutorialList);
        mockMvc.perform(get("/api/tutorials"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.size()").value(tutorialList.size()))
          .andDo(print());
    }
    
    @Test
    void should_return_list_of_tutorials_with_filter() throws Exception {
        List<Tutorial> tutorialList = new ArrayList<>(
          Arrays.asList(new Tutorial("SpringBoot", "Description",true),
            new Tutorial("SpringBoot2", "Description2", true),
            new Tutorial("SpringBoot3", "Description3", true)
          )
        );
        
        String title = "Boot";
    
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("title", title);
        
        when(tutorialRepository.findByTitleContaining(title)).thenReturn(tutorialList);
        mockMvc.perform(get("/api/tutorials").params(paramsMap))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.size()").value(tutorialList.size()))
          .andDo(print());
        
        tutorialList = Collections.emptyList();
       
        when(tutorialRepository.findByTitleContaining(title)).thenReturn(tutorialList);
        mockMvc.perform(get("/api/tutorials").params(paramsMap))
          .andExpect(status().isNoContent())
          .andDo(print());
    }
    
    @Test
    void should_return_no_content_when_filter() throws Exception {
        String title = "Spearman";
    
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("title", title);
        
        List<Tutorial> tutorialList = Collections.emptyList();
        
        when(tutorialRepository.findByTitleContaining(title)).thenReturn(tutorialList);
        mockMvc.perform(get("/api/tutorials").params(paramsMap))
          .andExpect(status().isNoContent())
          .andDo(print());
    }
    
    @Test
    void should_update_tutorials() throws Exception {
        Tutorial tutorial        = new Tutorial("SpringBoot", "Description", false);
        Tutorial tutorialUpdated = new Tutorial("Updated", "Updated", true);
        
        when(tutorialRepository.findById(tutorial.getId())).thenReturn(Optional.of(tutorial));
        when(tutorialRepository.save(any(Tutorial.class))).thenReturn(tutorialUpdated);
        
        mockMvc.perform(get("/api/tutorials{id}", tutorialUpdated.getId()).contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(tutorialUpdated)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.title").value(tutorialUpdated.getTitle()))
          .andExpect(jsonPath("$.description").value(tutorialUpdated.getDescription()))
          .andExpect(jsonPath("$.published").value(tutorialUpdated.isPublished()))
          .andDo(print());
    }
    
    @Test
    void shoud_return_not_found_update_tutorial() throws Exception {
        Tutorial tutorialUpdated = new Tutorial("Updated", "Updated", true);
        
        when(tutorialRepository.findById(tutorialUpdated.getId())).thenReturn(Optional.empty());
        when(tutorialRepository.save(any(Tutorial.class))).thenReturn(tutorialUpdated);
        
        mockMvc.perform(put("/api/tutorials/{id}", tutorialUpdated.getId()).contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(tutorialUpdated)))
          .andExpect(status().isNotFound())
          .andDo(print());
    }
    
    @Test
    void should_delete_tutorial() throws Exception {
        long id = 1L;
        
        doNothing().when(tutorialRepository).deleteById(id);
        
        mockMvc.perform(delete("/api/tutorials/{id}", id))
          .andExpect(status().isNoContent())
          .andDo(print());
    }
    
    @Test
    void should_delete_all_tutorials() throws Exception {
        doNothing().when(tutorialRepository).deleteAll();
        
        mockMvc.perform(delete("/api/tutorials"))
          .andExpect(status().isNoContent())
          .andDo(print());
    }
}