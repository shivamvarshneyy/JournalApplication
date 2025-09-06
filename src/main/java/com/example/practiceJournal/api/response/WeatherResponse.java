package com.example.practiceJournal.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherResponse {
    private final Current current;
    private final Location location;
}
