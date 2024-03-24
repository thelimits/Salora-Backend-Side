package com.Salora.SaloraWebService.Services.ProductServices;

import com.Salora.SaloraWebService.DTO.RequestDTO.AddProductDTO;
import com.Salora.SaloraWebService.DTO.RequestDTO.RequestUpdateProductAttribute;
import com.Salora.SaloraWebService.DTO.ResponseDTO.AddProductResponseDTO;
import com.Salora.SaloraWebService.DTO.ResponseDTO.GetProductDetailsResponseDTO;
import com.Salora.SaloraWebService.DTO.ResponseDTO.GetProductResponseDTO;
import com.Salora.SaloraWebService.Model.Enums.CategoryType;
import com.Salora.SaloraWebService.Model.Enums.SizeProduct.Size;
import com.Salora.SaloraWebService.Model.Product;
import com.google.cloud.storage.Bucket;
import org.aspectj.util.FileUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IProductService {
    CompletableFuture<AddProductResponseDTO> addProduct(AddProductDTO product, MultipartFile[] files);

    ResponseEntity<?> deleteProduct(String id);

    GetProductResponseDTO getProducts(List<String> category, Integer page, Integer size, String order_by);

    GetProductDetailsResponseDTO getProductDetails(String id);

    ResponseEntity<?> updateProductAttribute(String id, RequestUpdateProductAttribute requestUpdateProductAttribute);

    ResponseEntity<?> updateQuantityProduct(String id, Size size, Integer quantity);

    String getImageUrl(Bucket bucket, String filename);
    default String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

    default String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName.substring(0, originalFileName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + "." + getExtension(originalFileName);
    }
    static boolean checkCategory(String categoryString){
        boolean found = false;
        for (CategoryType statusType : CategoryType.values()) {
            if (categoryString.equalsIgnoreCase(statusType.toString())) {
                found = true;
                break;
            }
        }
        return found;
    }

}
