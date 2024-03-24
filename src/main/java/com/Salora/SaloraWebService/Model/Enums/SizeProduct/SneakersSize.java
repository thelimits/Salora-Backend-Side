package com.Salora.SaloraWebService.Model.Enums.SizeProduct;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SneakersSize implements Size{
    SIZE_36("36"), SIZE_36_5("36.5"), SIZE_37("37"), SIZE_37_5("37.5"), SIZE_38("38"),
    SIZE_38_5("38.5"), SIZE_39("39"), SIZE_39_5("39.5"), SIZE_40("40"), SIZE_40_5("40.5"),
    SIZE_41("41"), SIZE_41_5("41.5"), SIZE_42("42"), SIZE_42_5("42.5"), SIZE_43("43"),
    SIZE_43_5("43.5"), SIZE_44("44"), SIZE_44_5("44.5"), SIZE_45("45"), SIZE_45_5("45.5");

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
