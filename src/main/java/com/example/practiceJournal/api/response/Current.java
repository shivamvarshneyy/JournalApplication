package com.example.practiceJournal.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Current {
    private int temperature;
    @JsonProperty("weather_descriptions")
    private List<String> weatherDescriptions;
}