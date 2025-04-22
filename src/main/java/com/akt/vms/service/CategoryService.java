package com.akt.vms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.akt.vms.dto.CategoryDTO;
import com.akt.vms.entity.Category;
import com.akt.vms.mapper.CategoryMapper;
import com.akt.vms.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 
     * 
     * @param categoryRepository
     */
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * add new category
     * @param categoryDTO
     * @return CategoryDTO
     */
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = CategoryMapper.INSTANCE.toEntity(categoryDTO);
        return CategoryMapper.INSTANCE.toDto(categoryRepository.save(category));
    }

    //@Cacheable("categories")
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    //@Cacheable("category")
    public CategoryDTO getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper.INSTANCE::toDto)
                .orElse(null);
    }

    //@CacheEvict(value = "categories", allEntries = true)
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        return categoryRepository.findById(id).map(category -> {
            category.setCategoryName(categoryDTO.getCategoryName());
            category.setDescription(categoryDTO.getDescription());
            return CategoryMapper.INSTANCE.toDto(categoryRepository.save(category));
        }).orElse(null);
    }

   //@CacheEvict(value = {"categories", "category"}, allEntries = true)
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Page<CategoryDTO> getPaginatedCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(CategoryMapper.INSTANCE::toDto);
    }
}

