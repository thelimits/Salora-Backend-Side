package com.Salora.SaloraWebService.Controller;

import com.Salora.SaloraWebService.DTO.RequestDTO.AddProductDTO;
import com.Salora.SaloraWebService.DTO.RequestDTO.RequestUpdateProductAttribute;
import com.Salora.SaloraWebService.DTO.ResponseDTO.AddProductResponseDTO;
import com.Salora.SaloraWebService.Model.Enums.SizeProduct.Size;
import com.Salora.SaloraWebService.Services.ProductServices.IProductService;
import com.Salora.SaloraWebService.Services.RequestServiceAsync;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/products")
@Api(tags = "Product Controller")
public class ProductController {
    @Autowired
    private IProductService productService;

    @Autowired
    private RequestServiceAsync requestServiceAsync;

    @PostMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE
    })
    @ApiOperation(value = "Registrasi data produk", notes = "Endpoint untuk meregistrasi data produk.")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('admin:create')")
    public ResponseEntity<CompletableFuture<AddProductResponseDTO>> createProduct(
            @RequestPart("product") AddProductDTO productRequest,
            @RequestPart("files") MultipartFile[] files
    ) {
        return new ResponseEntity<>(productService.addProduct(productRequest, files), HttpStatus.CREATED);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('admin:delete')")
    public CompletableFuture<ResponseEntity<?>> DeleteProduct(
            @RequestParam String id
    ) {
        return requestServiceAsync.processRequest(() -> productService.deleteProduct(id));
    }

    @GetMapping()
    public CompletableFuture<ResponseEntity<?>> getProducts(
            @RequestParam(required = false) List<String> category,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "15") Integer size,
            @RequestParam(required = false, defaultValue = "desc,createdAt") String order_by
    ) {
        return requestServiceAsync.processRequest(() -> productService.getProducts(category, page, size, order_by));
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<?>> getProductsDetails(
            @PathVariable String id
    ) {
        return requestServiceAsync.processRequest(() -> productService.getProductDetails(id));
    }

    @PutMapping("/product-attributes/{id}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('admin:update')")
    public CompletableFuture<ResponseEntity<?>> updateProductAttribute(
            @PathVariable String id,
            @RequestBody RequestUpdateProductAttribute requestUpdateProductAttribute
            ) {
        return requestServiceAsync.processRequest(() -> productService.updateProductAttribute(id, requestUpdateProductAttribute));
    }

    @PutMapping("/product-attributes/quantity/{id}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('admin:update')")
    public CompletableFuture<ResponseEntity<?>> updateQuantityProduct(
            @PathVariable String id,
            @RequestParam Size size,
            @RequestParam Integer quantity
    ) {
        System.out.println(size);
        return requestServiceAsync.processRequest(() -> productService.updateQuantityProduct(id, size, quantity));
    }

}
