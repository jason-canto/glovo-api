package com.glovoapp.backender.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * To be used for exposing order information through the API
 */

@Getter
@AllArgsConstructor
public class OrderVM {
    private String id;
    private String description;
}
