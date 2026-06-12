package com.ryuunomi.inmotech.dto;

import java.io.Serializable;
import java.util.Map;

public record FacetaDTO(
    Map<String, Long> ciudades,
    Map<String, Long> tipos
) implements Serializable {}