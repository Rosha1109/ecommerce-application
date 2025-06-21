package com.example.ecommerceapplication.solution.thing.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ThingRepository extends CrudRepository<Thing, UUID> {
}
