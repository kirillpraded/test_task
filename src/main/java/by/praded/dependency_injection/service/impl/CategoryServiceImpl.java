package by.praded.dependency_injection.service.impl;

import by.praded.dependency_injection.annotation.Inject;
import by.praded.dependency_injection.dao.CategoryDao;
import by.praded.dependency_injection.service.CategoryService;

/**
 * @author Kiryl Praded
 * 04.02.2021
 */
public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao;


    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }
}
