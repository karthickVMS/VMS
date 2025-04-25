package com.akt.vms.controller;

import com.akt.vms.dto.CategoryDTO;
import com.akt.vms.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest implements WebMvcConfigurer {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;
    private CategoryDTO categoryDTO;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setCategoryName("Test Category");
        categoryDTO.setDescription("Test Description");

        // Configure MockMvc with PageableHandlerMethodArgumentResolver
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void createCategory_ShouldReturnCreatedCategory() throws Exception {
        when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(categoryDTO);

        CategoryDTO requestDTO = new CategoryDTO();
        requestDTO.setCategoryName("Test Category");
        requestDTO.setDescription("Test Description");

        mockMvc.perform(post("/admin/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.categoryName").value("Test Category"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(categoryService, times(1)).createCategory(any(CategoryDTO.class));
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategory() throws Exception {
        when(categoryService.updateCategory(eq(1L), any(CategoryDTO.class))).thenReturn(categoryDTO);

        CategoryDTO requestDTO = new CategoryDTO();
        requestDTO.setCategoryName("Updated Category");
        requestDTO.setDescription("Updated Description");

        mockMvc.perform(put("/admin/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.categoryName").value("Test Category"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(categoryService, times(1)).updateCategory(eq(1L), any(CategoryDTO.class));
    }

    @Test
    void deleteCategory_ShouldReturnNoContent() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/admin/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(1L);
    }

    @Test
    void getCategoryById_ShouldReturnCategory() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(categoryDTO);

        mockMvc.perform(get("/admin/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.categoryName").value("Test Category"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void getAllCategories_ShouldReturnCategoryList() throws Exception {
        List<CategoryDTO> categories = Arrays.asList(categoryDTO);
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].categoryName").value("Test Category"))
                .andExpect(jsonPath("$[0].description").value("Test Description"));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getPaginatedCategories_ShouldReturnPagedCategories() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryDTO> page = new PageImpl<>(Arrays.asList(categoryDTO), pageable, 1);
        when(categoryService.getPaginatedCategories(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/admin/categories/paginated?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].categoryName").value("Test Category"))
                .andExpect(jsonPath("$.content[0].description").value("Test Description"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(categoryService, times(1)).getPaginatedCategories(any(Pageable.class));
    }
}