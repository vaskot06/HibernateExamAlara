package alararestaurant.service;

import alararestaurant.domain.entities.Category;
import alararestaurant.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String exportCategoriesByCountOfItems() {

        StringBuilder stringBuilder = new StringBuilder();

        List<Category> categories = this.categoryRepository.findAllByOrderByItems();

        for (Category category : categories) {

            stringBuilder.append(String.format("Category: %s\n", category.getName()));

            category.getItems().forEach(a-> stringBuilder
                    .append(String.format("--- Item Name: %s\n--- Item Price: %s\n", a.getName(),a.getPrice()))
                .append(System.lineSeparator()));

        }
        return stringBuilder.toString().trim();
    }
}
