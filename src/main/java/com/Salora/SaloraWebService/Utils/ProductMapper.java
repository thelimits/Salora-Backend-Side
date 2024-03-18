package com.Salora.SaloraWebService.Utils;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    public <T> T mapObject(Object source, Class<T> target) {
        if (source == null || target == null) {
            return null;
        }

        try {
            T targetInstance = target.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, targetInstance);
            return targetInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> mapObjects(List<?> sourceList, Class<T> targetType) {
        if (sourceList == null) {
            return null;
        }

        return sourceList.stream()
                .map(source -> mapObject(source, targetType))
                .collect(Collectors.toList());
    }
}
