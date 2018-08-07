package com.example.john.routing.api;

import org.immutables.value.Value;

//Interface used for route directions
@Value.Immutable
public interface Direction {
    String getMode();
    String getDescription();
}
