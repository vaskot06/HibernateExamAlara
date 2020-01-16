package alararestaurant.service;

import alararestaurant.domain.dtos.jsonDtos.EmployeeJsonImportDto;
import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Position;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.PositionRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    public static final String EMPLOYEE_PATH = "C:\\Users\\Vasil\\Desktop\\SoftUni\\Java DB\\Hibernate\\12.EXAM_PREP\\AlaraRestaurant-skeleton\\AlaraRestaurant\\src\\main\\resources\\files\\employees.json";

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final Gson gson;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;


    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PositionRepository positionRepository, Gson gson, FileUtil fileUtil, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public Boolean employeesAreImported() {

        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesJsonFile() throws IOException {

        return this.fileUtil.readFile(EMPLOYEE_PATH);
    }

    @Override
    public String importEmployees(String employees) {

        StringBuilder stringBuilder = new StringBuilder();

        EmployeeJsonImportDto[] employeeJsonImportDto = this.gson.fromJson(employees, EmployeeJsonImportDto[].class);

        for (EmployeeJsonImportDto jsonImportDto : employeeJsonImportDto) {
            Employee employee = this.modelMapper.map(jsonImportDto, Employee.class);
            if (!this.validationUtil.isValid(employee)) {
                this.validationUtil.violations(employee).forEach(v -> System.out.println(v.getMessage()));
                continue;
            }
            Position position = this.positionRepository.findByName(jsonImportDto.getPosition());

            if (position == null) {
                position = new Position();
                position.setName(jsonImportDto.getPosition());

                if (!this.validationUtil.isValid(position)) {
                    this.validationUtil.violations(position).forEach(v -> System.out.println(v.getMessage()));
                    continue;
                }

                position = this.positionRepository.saveAndFlush(position);
            }
            employee.setPosition(position);

            employeeRepository.saveAndFlush(employee);
            stringBuilder.append(String.format("Record %s successfully imported.", employee.getName())).append(System.lineSeparator());
        }

        return stringBuilder.toString().trim();
    }
}
