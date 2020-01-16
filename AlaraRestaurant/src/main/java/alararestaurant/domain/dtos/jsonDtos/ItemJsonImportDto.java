package alararestaurant.domain.dtos.jsonDtos;

import alararestaurant.domain.entities.Category;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ItemJsonImportDto {

    @Column(nullable = false,unique = true)
    @Size(min = 3, max = 30)
    private String name;
    @Size(min = 3, max = 30)
    private String category;
    @Column(nullable = false)
    @DecimalMin("0.01")
    private BigDecimal price;

    public ItemJsonImportDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
