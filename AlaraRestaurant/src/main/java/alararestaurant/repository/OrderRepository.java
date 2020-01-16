package alararestaurant.repository;

import alararestaurant.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "select o from Order as o join Employee as e on o.employee.id = e.id where e.position.name = :name order by e.name asc, o.id asc")
    List<Order> findAllBy(@Param(value = "name")String name);

}
