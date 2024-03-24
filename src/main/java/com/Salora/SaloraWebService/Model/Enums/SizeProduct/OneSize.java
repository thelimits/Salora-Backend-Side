package com.Salora.SaloraWebService.Model.Enums.SizeProduct;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OneSize implements Size{
    ONE_SIZE("One Size");

    private final String size;
    @Override
    @JsonValue
    public String getSize() {
        return this.size;
    }

    @Override
    public String toString() {
        return this.size;
    }
}
