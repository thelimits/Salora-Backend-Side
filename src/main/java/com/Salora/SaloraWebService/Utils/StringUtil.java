package com.Salora.SaloraWebService.Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class StringUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static Map convertJsonStringToMap(String jsonString) throws Exception{
        return objectMapper.readValue(jsonString, new TypeReference<Map<String,Object>>(){});
    }
}
