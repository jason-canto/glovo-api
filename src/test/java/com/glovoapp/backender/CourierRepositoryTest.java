package com.glovoapp.backender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.glovoapp.backender.domain.Courier;
import com.glovoapp.backender.domain.Location;
import com.glovoapp.backender.enums.Vehicle;
import com.glovoapp.backender.repository.CourierRepository;

class CourierRepositoryTest {

    @Test
    void findOneExisting() {
        Courier courier = new CourierRepository().findById("courier-1");
        Courier expected = Courier.builder()
                .id("courier-1")
                .box(true)
                .name("Manolo Escobar")
                .vehicle(Vehicle.MOTORCYCLE)
                .location(new Location(41.3965463, 2.1963997))
                .build();

        assertEquals(expected, courier);
    }

    @Test
    void findOneNotExisting() {
        Courier courier = new CourierRepository().findById("bad-courier-id");
        assertNull(courier);
    }

    @Test
    void findAll() {
        List<Courier> all = new CourierRepository().findAll();
        assertFalse(all.isEmpty());
    }

}