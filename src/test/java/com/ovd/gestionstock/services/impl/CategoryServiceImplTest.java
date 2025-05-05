package com.ovd.gestionstock.services.impl;

import com.ovd.gestionstock.dto.CategoryDto;
import com.ovd.gestionstock.models.Category;
import com.ovd.gestionstock.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @InjectMocks
    CategoryServiceImpl categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCategory_ShouldReturnCategoryList_WhenCategoriesExist() {
        // Given
        Category category1 = new Category();
        category1.setId(1L);
        category1.setCode("CAT001");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setCode("CAT002");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        // When
        List<CategoryDto> result = categoryService.getAllCategory();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getAllCategory_ShouldLogErrorAndReturnEmptyList_WhenNoCategoriesExist() {
        // Given
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<CategoryDto> result = categoryService.getAllCategory();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void deleteCategory() {
    }

    @Test
    void getCategoryById() {
    }

    @Test
    void createCategory() {
    }
}