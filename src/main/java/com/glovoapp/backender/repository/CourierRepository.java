package com.glovoapp.backender.repository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.glovoapp.backender.calculator.DistanceCalculator;
import com.glovoapp.backender.domain.Courier;
import com.glovoapp.backender.domain.Order;
import com.glovoapp.backender.dto.OrderVM;
import com.glovoapp.backender.enums.Priority;
import com.glovoapp.backender.enums.Vehicle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class CourierRepository {

	private static final String COURIERS_FILE = "/couriers.json";

	private static final List<Courier> couriers;

	@Autowired
	private OrderRepository orders;

	@Value("${backender.order.box}")
	private String[] boxOrder;

	@Value("${backender.order.sort}")
	private String[] sortingPriorities;

	@Value("${backender.order.distance}")
	private Integer distance;

	@Value("${backender.order.km}")
	private Integer maxKmDistance;

	static {
		try (Reader reader = new InputStreamReader(CourierRepository.class.getResourceAsStream(COURIERS_FILE))) {
			Type type = new TypeToken<List<Courier>>() {
			}.getType();
			couriers = new Gson().fromJson(reader, type);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Courier findById(String courierId) {
		return couriers.stream().filter(courier -> courierId.equals(courier.getId())).findFirst().orElse(null);
	}

	public List<Courier> findAll() {
		return new ArrayList<>(couriers);
	}

	public List<OrderVM> findOrdersByCourierById(String courierId) {
		Courier courier = findById(courierId);

		return orders.findAll().stream()
				.filter(o -> isLongDistanceVehicle(courier.getVehicle())
						|| DistanceCalculator.calculateDistance(o.getPickup(), courier.getLocation()) <= maxKmDistance)
				.filter(o -> courier.getBox()
						|| Arrays.asList(boxOrder).stream().noneMatch(o.getDescription()::contains))
				.sorted(Comparator.comparing(Order::getVip).reversed())
				.sorted(Comparator.comparing(Order::getFood).reversed())
				.map(o -> new OrderVM(o.getId(), o.getDescription()))
				.collect(Collectors.toList());
	}

	public boolean isLongDistanceVehicle(Vehicle vehicle) {
		return Vehicle.ELECTRIC_SCOOTER.equals(vehicle) || Vehicle.MOTORCYCLE.equals(vehicle);
	}

	public Comparator<Order> getSortingByType(Priority priority) {
		switch(priority) {
			case FOOD:
				return Comparator.comparing(Order::getFood).reversed();
			case VIP:
				return Comparator.comparing(Order::getVip).reversed();
			case DISTANCE:
				return null;
		default:
			break; 
				
		}
		return null;
	}
}
