package alararestaurant.domain.dtos.jsonDtos;

import alararestaurant.domain.entities.Position;
import com.google.gson.annotations.Expose;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class EmployeeJsonImportDto {

    @Expose
    @Size(min = 3, max = 30, message = "Invalid data format.")
    private String name;
    @Expose
    @Min(value = 15, message = "Invalid data format.")
    @Max(value = 80, message = "Invalid data format.")
    private Integer age;
    @Expose
    @Size(min = 3, max = 30, message = "Invalid data format.")
    private String position;

    public EmployeeJsonImportDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
