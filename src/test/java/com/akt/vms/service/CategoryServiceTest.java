package com.akt.vms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import com.akt.vms.dto.CategoryDTO;
import com.akt.vms.entity.Category;
import com.akt.vms.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setCategoryName("Test Category");
        category.setDescription("Test Description");

        categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("Test Category");
        categoryDTO.setDescription("Test Description");
    }

    @Test
    void createCategory_ShouldSaveAndReturnCategoryDTO() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        // Assume CategoryMapper maps correctly
        CategoryDTO result = categoryService.createCategory(categoryDTO);

        assertNotNull(result);
        assertEquals("Test Category", result.getCategoryName());
        assertEquals("Test Description", result.getDescription());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategoryDTOs() {
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Category", result.get(0).getCategoryName());
        assertEquals("Test Description", result.get(0).getDescription());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById_ShouldReturnCategoryDTO_WhenCategoryExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDTO result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals("Test Category", result.getCategoryName());
        assertEquals("Test Description", result.getDescription());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void getCategoryById_ShouldReturnNull_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CategoryDTO result = categoryService.getCategoryById(1L);

        assertNull(result);
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void updateCategory_ShouldUpdateAndReturnCategoryDTO_WhenCategoryExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO updatedDTO = new CategoryDTO();
        updatedDTO.setCategoryName("Updated Category");
        updatedDTO.setDescription("Updated Description");

        CategoryDTO result = categoryService.updateCategory(1L, updatedDTO);

        assertNotNull(result);
        assertEquals("Updated Category", result.getCategoryName());
        assertEquals("Updated Description", result.getDescription());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory_ShouldReturnNull_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CategoryDTO result = categoryService.updateCategory(1L, categoryDTO);

        assertNull(result);
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void deleteCategory_ShouldCallDeleteById() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void getPaginatedCategories_ShouldReturnPagedCategoryDTOs() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> page = new PageImpl<>(Arrays.asList(category), pageable, 1);
        when(categoryRepository.findAll(pageable)).thenReturn(page);

        Page<CategoryDTO> result = categoryService.getPaginatedCategories(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Category", result.getContent().get(0).getCategoryName());
        assertEquals("Test Description", result.getContent().get(0).getDescription());
        verify(categoryRepository, times(1)).findAll(pageable);
    }
}