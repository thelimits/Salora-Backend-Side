package com.Salora.SaloraWebService.Config.EnumConfig;

import com.Salora.SaloraWebService.Model.Enums.SizeProduct.ClothingSize;
import com.Salora.SaloraWebService.Model.Enums.SizeProduct.Size;
import com.Salora.SaloraWebService.Model.Enums.SizeProduct.SneakersSize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class GlobalControllerAdvice {

    SizeEnumConverter converter = new SizeEnumConverter(ClothingSize.class, SneakersSize.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Size.class, converter);
    }
}

