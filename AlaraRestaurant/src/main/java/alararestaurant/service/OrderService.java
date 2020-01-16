package alararestaurant.service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface OrderService {

    Boolean ordersAreImported();

    String readOrdersXmlFile() throws IOException, JAXBException;

    String importOrders() throws IOException, JAXBException;

    String exportOrdersFinishedByTheBurgerFlippers();
}
