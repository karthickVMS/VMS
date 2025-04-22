package com.akt.vms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.akt.vms.dto.CategoryDTO;
import com.akt.vms.entity.Category;

@Mapper
public interface CategoryMapper {
    
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    
    CategoryDTO toDto(Category category);
    
    Category toEntity(CategoryDTO dto);
}

