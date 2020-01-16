package alararestaurant.domain.dtos.xmlDtos;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderDto {


    @XmlElement(name = "customer")
    @NotNull
    private String customer;

    @XmlElement(name = "employee")
    @NotNull
    private String employee;

    @XmlElement(name = "date-time")
    @NotNull
    private String dateTime;

    @XmlElement(name = "type")
    @NotNull
    private String orderType;

    @XmlElement(name = "items")
    private ItemRootDto itemRootDto;

    @Transient
    private List<ItemDto> items;

    public OrderDto() {
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public ItemRootDto getItemRootDto() {
        return itemRootDto;
    }

    public void setItemRootDto(ItemRootDto itemRootDto) {
        this.itemRootDto = itemRootDto;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }
}
