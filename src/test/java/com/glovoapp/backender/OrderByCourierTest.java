package com.glovoapp.backender;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import com.glovoapp.backender.domain.Courier;
import com.glovoapp.backender.domain.Location;
import com.glovoapp.backender.domain.Order;
import com.glovoapp.backender.dto.OrderVM;
import com.glovoapp.backender.enums.Vehicle;
import com.glovoapp.backender.repository.CourierRepository;
import com.glovoapp.backender.repository.OrderRepository;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ComponentScan(basePackages = "com.glovoapp.backender")
@SpringBootTest(classes = { GlovoApplication.class })
class OrderByCourierTest {

	@InjectMocks
	private CourierRepository courierRepository;

	@Mock
	private OrderRepository orderRepository;

	@BeforeAll
	public void setUp() {
		String [] boxOrder = {"pizza","cake","flamingo"};
		String [] sortingPriorities = {"DISTANCE","VIP","FOOD"};
		ReflectionTestUtils.setField(courierRepository, "maxKmDistance", 5);
		ReflectionTestUtils.setField(courierRepository, "distance", 500);
		ReflectionTestUtils.setField(courierRepository, "boxOrder", boxOrder);
		ReflectionTestUtils.setField(courierRepository, "sortingPriorities", sortingPriorities);
	}

	@Test
	void testFilterOrderByBox() {
		List<Order> orders = new ArrayList<>();
		orders.add(Order.builder()
						.id("order-1")
						.description("2x Hot dog with Salad\\n2x Pork bao with Fries\\n1x Pizza with Fries")
						.delivery(new Location(41.37845745887056, 2.1673693037972392))
						.pickup(new Location(41.39416677396895, 2.1675665724526363))
						.food(false)
						.vip(false)
						.build());
		
		orders.add(Order.builder()
						.id("order-2")
						.description("1x Tuna poke with Fries\\n1x Kebab with Fries\\n2x Hot dog with Salad")
						.delivery(new Location(41.37845745887056, 2.1673693037972392))
						.pickup(new Location(41.39416677396895, 2.1675665724526363))
						.food(false)
						.vip(false)
						.build());

		Courier courier1 = Courier.builder()
								.id("test-1")
								.box(false)
								.location(new Location(41.39416677396895, 2.1675665724526363))
								.name("test")
								.vehicle(Vehicle.ELECTRIC_SCOOTER)
								.build();

		when(orderRepository.findAll()).thenReturn(orders);
		List<OrderVM> ordersList = courierRepository.findOrdersByCourierById(courier1);
		Assertions.assertTrue(ordersList.size() == 1);
	}

	@Test
	void testFilterOrderByDistance() {
		List<Order> orders = new ArrayList<>();
		String [] sortingPriorities = {"DISTANCE","VIP","FOOD"};
		ReflectionTestUtils.setField(courierRepository, "sortingPriorities", sortingPriorities);
		orders.add(Order.builder()
						.id("order-1")
						.description("1x Tuna poke with Fries\\\\n1x Kebab with Fries\\\\n2x Hot dog with Salad")
						.delivery(new Location(41.37845745887056, 2.1673693037972392))
						.pickup(new Location(41.39416677396895, 2.1675665724526363))
						.food(false)
						.vip(false)
						.build());
		
		orders.add(Order.builder()
						.id("order-2")
						.description("1x Tuna poke with Fries\\n1x Kebab with Fries\\n2x Hot dog with Salad")
						.delivery(new Location(1.0, 2.0))
						.pickup(new Location(1.0, 2.0))
						.food(false)
						.vip(false)
						.build());

		Courier courier = Courier.builder()
								.id("test-1")
								.box(false)
								.location(new Location(1.0, 2.0))
								.name("test")
								.vehicle(Vehicle.BICYCLE)
								.build();

		when(orderRepository.findAll()).thenReturn(orders);
		List<OrderVM> ordersList = courierRepository.findOrdersByCourierById(courier);
		Assertions.assertTrue(ordersList.size() == 1);
	}

	@Test
	void testgetAllOders() {
		List<Order> orders = new ArrayList<>();
		String [] sortingPriorities = {"DISTANCE","VIP","FOOD"};
		ReflectionTestUtils.setField(courierRepository, "sortingPriorities", sortingPriorities);
		orders.add(Order.builder()
						.id("order-1")
						.description("1x Tuna poke with Fries\\\\n1x Kebab with Fries\\\\n2x Hot dog with Salad")
						.delivery(new Location(41.37845745887056, 2.1673693037972392))
						.pickup(new Location(41.39416677396895, 2.1675665724526363))
						.food(false)
						.vip(false)
						.build());
		
		orders.add(Order.builder()
						.id("order-2")
						.description("1x Tuna poke with Fries\\n1x Kebab with Fries\\n2x Hot dog with Salad")
						.delivery(new Location(1.0, 2.0))
						.pickup(new Location(1.0, 2.0))
						.food(false)
						.vip(false)
						.build());

		Courier courier = courierRepository.findById("courier-1");

		when(orderRepository.findAll()).thenReturn(orders);
		List<OrderVM> ordersList = courierRepository.findOrdersByCourierById(courier);
		Assertions.assertTrue(ordersList.size() == 2);
	}

	@Test
	void testSortOrdersByDistance() {
		List<Order> orders = new ArrayList<>();
		orders.add(Order.builder()
						.id("order-1")
						.description("1x Tuna poke with Fries\\\\n1x Kebab with Fries\\\\n2x Hot dog with Salad")
						.delivery(new Location(1.0, 2.0))
						.pickup(new Location(41.38352896665239, 2.1642780487689453))
						.food(false)
						.vip(false)
						.build());

		orders.add(Order.builder()
						.id("order-2")
						.description("1x Tuna poke with Fries\\\\n1x Kebab with Fries\\\\n2x Hot dog with Salad")
						.delivery(new Location(1.0, 2.0))
						.pickup(new Location(41.399999, 2.195))
						.food(false)
						.vip(false)
						.build());

		orders.add(Order.builder()
						.id("order-3")
						.description("1x Tuna poke with Fries\\n1x Kebab with Fries\\n2x Hot dog with Salad")
						.delivery(new Location(1.0, 2.0))
						.pickup(new Location(41.39352896665239, 2.1942780487689453))
						.food(false)
						.vip(true)
						.build());

		Courier courier = courierRepository.findById("courier-1");

		when(orderRepository.findAll()).thenReturn(orders);
		List<OrderVM> ordersList = courierRepository.findOrdersByCourierById(courier);
		Assertions.assertEquals(ordersList.get(0).getId(), orders.get(2).getId());
		Assertions.assertEquals(ordersList.get(1).getId(), orders.get(1).getId());
		Assertions.assertEquals(ordersList.get(2).getId(), orders.get(0).getId());
	}

	@Test
	void testSortOrdersByFood() {
		List<Order> orders = new ArrayList<>();
		String [] sortingPriorities = {"FOOD","DISTANCE","VIP"};
		ReflectionTestUtils.setField(courierRepository, "sortingPriorities", sortingPriorities);
		orders.add(Order.builder()
						.id("order-1")
						.description("1x Tuna poke with Fries\\\\n1x Kebab with Fries\\\\n2x Hot dog with Salad")
						.delivery(new Location(1.0, 2.0))
						.pickup(new Location(1.0, 2.0))
						.food(false)
						.vip(false)
						.build());
		
		orders.add(Order.builder()
						.id("order-2")
						.description("1x Tuna poke with Fries\\n1x Kebab with Fries\\n2x Hot dog with Salad")
						.delivery(new Location(1.0, 2.0))
						.pickup(new Location(1.0, 2.0))
						.food(true)
						.vip(false)
						.build());

		Courier courier = courierRepository.findById("courier-1");

		when(orderRepository.findAll()).thenReturn(orders);
		List<OrderVM> ordersList = courierRepository.findOrdersByCourierById(courier);
		Assertions.assertEquals(ordersList.get(0).getId(), orders.get(1).getId());
	}

	@Test
	void testSortOrdersByVip() {
		String [] sortingPriorities = {"VIP","FOOD","DISTANCE"};
		ReflectionTestUtils.setField(courierRepository, "sortingPriorities", sortingPriorities);
		List<Order> orders = new ArrayList<>();
		orders.add(Order.builder()
						.id("order-1")
						.description("1x Tuna poke with Fries\\\\n1x Kebab with Fries\\\\n2x Hot dog with Salad")
						.delivery(new Location(1.0, 2.0))
						.pickup(new Location(1.0, 2.0))
						.food(true)
						.vip(false)
						.build());
		
		orders.add(Order.builder()
						.id("order-2")
						.description("1x Tuna poke with Fries\\n1x Kebab with Fries\\n2x Hot dog with Salad")
						.delivery(new Location(1.0, 2.0))
						.pickup(new Location(1.0, 2.0))
						.food(false)
						.vip(true)
						.build());

		Courier courier = courierRepository.findById("courier-1");

		when(orderRepository.findAll()).thenReturn(orders);
		List<OrderVM> ordersList = courierRepository.findOrdersByCourierById(courier);
		Assertions.assertEquals(ordersList.get(0).getId(), orders.get(1).getId());
	}

}
