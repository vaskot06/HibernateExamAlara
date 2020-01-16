package alararestaurant.repository;

import alararestaurant.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findByName(String name);

    Boolean existsByName(String name);

    @Query(value = "select c.name, i.name, i.price\n" +
            "from Category as c \n" +
            "         join Item i on c.id = i.category.id\n" +
            "group by c.id, i.name\n" +
            "order by count(c.id) desc, sum(i.price) desc ")
    List<Object[]> findAllByItems();

    @Query(value = "select c from Category as c group by c.name order by size(c.items) desc ")
    List<Category> findAllByOrderByItems();

}
