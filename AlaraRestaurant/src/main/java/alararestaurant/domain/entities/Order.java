package alararestaurant.domain.entities;

import alararestaurant.domain.entities.enums.OrderType;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity{

    @Column(nullable = false)
    private String customer;
    @Column(nullable = false, name = "date_time")
    private String dateTime;
    @Column(name = "type", columnDefinition = "varchar(255) default 'ForHere'", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderType orderType;
    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    public Order() {
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
