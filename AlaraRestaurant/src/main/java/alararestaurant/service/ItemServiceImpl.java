package alararestaurant.service;

import alararestaurant.domain.dtos.jsonDtos.ItemJsonImportDto;
import alararestaurant.domain.entities.Category;
import alararestaurant.domain.entities.Item;
import alararestaurant.repository.CategoryRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ItemServiceImpl implements ItemService {

    private static final String FILE_PATH = "C:\\Users\\Vasil\\Desktop\\SoftUni\\Java DB\\Hibernate\\12.EXAM_PREP\\AlaraRestaurant-skeleton\\AlaraRestaurant\\src\\main\\resources\\files\\items.json";

    private final ItemRepository itemRepository;
    private final FileUtil fileUtil;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, FileUtil fileUtil, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Boolean itemsAreImported() {

        return this.itemRepository.count() > 0;
    }

    @Override
    public String readItemsJsonFile() throws IOException {

        return this.fileUtil.readFile(FILE_PATH);
    }

    @Override
    public String importItems(String items) {

        StringBuilder sb = new StringBuilder();
        try {
            ItemJsonImportDto[] itemJsonImportDtos = this.gson.fromJson(readItemsJsonFile(), ItemJsonImportDto[].class);

            for (ItemJsonImportDto itemJsonImportDto : itemJsonImportDtos) {

                Item item = this.modelMapper.map(itemJsonImportDto, Item.class);

                if (this.itemRepository.existsByName(item.getName())) {
                    continue;
                }

                if (!this.validationUtil.isValid(item)) {
//                    this.validationUtil.violations(item).forEach(v -> System.out.println(v.getMessage()));
                    System.out.println("Invalid data format");
                    continue;
                }


                Category category = this.categoryRepository.findByName(itemJsonImportDto.getCategory());

                if (category == null) {

                    category = new Category();
                    category.setName(itemJsonImportDto.getCategory());

                    if (!this.validationUtil.isValid(category)) {
//                        this.validationUtil.violations(category).forEach(v -> System.out.println(v.getMessage()));
                        System.out.println("Invalid data format");
                        continue;
                    }


                    if (!this.categoryRepository.existsByName(category.getName())) {
                        category = this.categoryRepository.saveAndFlush(category);
                    }
                }
                item.setCategory(category);

                this.itemRepository.saveAndFlush(item);


                sb.append(String.format("%s successfully imported.", item.getName())).append(System.lineSeparator());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return sb.toString().trim();
    }
}
