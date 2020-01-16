package alararestaurant.service;

import alararestaurant.domain.dtos.xmlDtos.ItemDto;
import alararestaurant.domain.dtos.xmlDtos.OrderDto;
import alararestaurant.domain.dtos.xmlDtos.OrderRootDto;
import alararestaurant.domain.entities.Order;
import alararestaurant.domain.entities.OrderItem;
import alararestaurant.parsers.XmlParser;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.repository.OrderItemRepository;
import alararestaurant.repository.OrderRepository;
import alararestaurant.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_INPUT = "C:\\Users\\Vasil\\Desktop\\SoftUni\\Java DB\\Hibernate\\12.EXAM_PREP\\AlaraRestaurant-skeleton\\AlaraRestaurant\\src\\main\\resources\\files\\orders.xml";

    private final OrderRepository orderRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;
    private final ValidationUtil validationUtil;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, XmlParser xmlParser, ModelMapper modelMapper, EmployeeRepository employeeRepository, ValidationUtil validationUtil, OrderItemRepository orderItemRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
        this.validationUtil = validationUtil;
        this.orderItemRepository = orderItemRepository;

        this.itemRepository = itemRepository;
    }

    @Override
    public Boolean ordersAreImported() {
        return this.orderRepository.count() > 0;
    }

    @Override
    public String readOrdersXmlFile() throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        File file = new File(ORDER_INPUT);
        Reader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = bufferedReader.readLine();

        while (line != null) {
            stringBuilder.append(line).append(System.lineSeparator());
            line = bufferedReader.readLine();
        }
        return stringBuilder.toString().trim();
    }

    @Override
    public String importOrders() throws IOException, JAXBException {
        StringBuilder stringBuilder = new StringBuilder();

        OrderRootDto list = this.xmlParser.read(OrderRootDto.class, ORDER_INPUT);

        for (OrderDto orderDto : list.getOrderDtos()) {

            List<ItemDto> items = orderDto.getItemRootDto().getItems();
            orderDto.setItems(items);
            Order order = this.modelMapper.map(orderDto, Order.class);
            order.setEmployee(employeeRepository.findByName(orderDto.getEmployee()));

            if (!this.validationUtil.isValid(order)) {
                System.out.println("Invalid data format");
                continue;
            }

            List<OrderItem> orderItems = new ArrayList<>();
            for (ItemDto item : orderDto.getItems()) {

                if (this.itemRepository.findByName(item.getName()) == null) {
                    continue;
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setItem(this.itemRepository.findByName(item.getName()));
                orderItem.setQuantity(item.getQuantity());
                orderItems.add(orderItem);
                this.orderItemRepository.saveAndFlush(orderItem);
            }
            order.setOrderItems(orderItems);


            stringBuilder.append(String.format("Order for %s on %s added", order.getCustomer(), order.getDateTime())).append(System.lineSeparator());

            orderRepository.saveAndFlush(order);
        }

        return stringBuilder.toString().trim();
    }

    @Override
    public String exportOrdersFinishedByTheBurgerFlippers() {
        StringBuilder stringBuilder = new StringBuilder();

        String name = "Burger Flipper";

        List<Order> orders = this.orderRepository.findAllBy(name);

        for (Order order : orders) {

            stringBuilder.append(String.format("Name: %s", order.getEmployee().getName())).append(System.lineSeparator());
            stringBuilder.append("Orders:").append(System.lineSeparator());
            stringBuilder.append(String.format("   Customer: %s", order.getCustomer())).append(System.lineSeparator());
            stringBuilder.append("   Items:").append(System.lineSeparator());

            for (OrderItem orderItem : order.getOrderItems()) {
                stringBuilder.append(String.format("     Name: %s", orderItem.getItem().getName())).append(System.lineSeparator());
                stringBuilder.append(String.format("     Price: %s", orderItem.getItem().getPrice())).append(System.lineSeparator());
                stringBuilder.append(String.format("     Quantity: %s\n", orderItem.getQuantity())).append(System.lineSeparator());
            }


        }

        return stringBuilder.toString().trim();
    }
}
