package com.Salora.SaloraWebService.Model.Enums.SizeProduct;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ClothingSize implements Size{
    XS("XS"), S("S"), M("M"), L("L"), XL("XL"), XXL("XXL"), XXXL("XXXL");

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
