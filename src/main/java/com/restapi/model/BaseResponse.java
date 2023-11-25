package com.restapi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResponse<T> {
    private T data;
    private String errors;
    private boolean success;
}
