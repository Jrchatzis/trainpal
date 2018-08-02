package com.example.john.routing.api;

import org.immutables.value.Value;

@Value.Immutable
public interface Direction {
    String getMode();
    String getDescription();
}
