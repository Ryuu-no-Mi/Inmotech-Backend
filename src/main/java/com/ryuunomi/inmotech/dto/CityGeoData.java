package com.ryuunomi.inmotech.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public record CityGeoData(
    String ciudad,
    double latitud,
    double longitud,
    List<String> distritos,
    Map<String, List<String>> barriosPorDistrito
) implements Serializable {}