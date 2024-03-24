package com.Salora.SaloraWebService.Services.ProductServices;

import com.Salora.SaloraWebService.AppConfiguration;
import com.Salora.SaloraWebService.DTO.RequestDTO.AddProductDTO;
import com.Salora.SaloraWebService.DTO.RequestDTO.RequestUpdateProductAttribute;
import com.Salora.SaloraWebService.DTO.ResponseDTO.AddProductResponseDTO;
import com.Salora.SaloraWebService.DTO.ResponseDTO.GetProductDetailsResponseDTO;
import com.Salora.SaloraWebService.DTO.ResponseDTO.GetProductResponseDTO;
import com.Salora.SaloraWebService.Exception.BadRequest;
import com.Salora.SaloraWebService.Exception.CustomerNotFound;
import com.Salora.SaloraWebService.Model.Enums.CategoryType;
import com.Salora.SaloraWebService.Model.Enums.SizeProduct.Size;
import com.Salora.SaloraWebService.Model.Product;
import com.Salora.SaloraWebService.Model.ProductAttribute;
import com.Salora.SaloraWebService.Model.ProductCategory;
import com.Salora.SaloraWebService.Model.ProductCategoryType;
import com.Salora.SaloraWebService.Repository.ProductAttributeRepository;
import com.Salora.SaloraWebService.Repository.ProductCategoryRepository;
import com.Salora.SaloraWebService.Repository.ProductCategoryTypeRepository;
import com.Salora.SaloraWebService.Repository.ProductRepository;
import com.Salora.SaloraWebService.Utils.ProductMapper;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService implements IProductService {
    Logger logger = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductCategoryTypeRepository productCategoryTypeRepository;

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    @Autowired
    private AppConfiguration configure;

    @Override
    @Async
    public CompletableFuture<AddProductResponseDTO> addProduct(AddProductDTO product, MultipartFile[] files) {
        Bucket bucket = StorageClient.getInstance().bucket();
        String filename = null;
        for (MultipartFile file : files) {
            try {
                filename = generateFileName(Objects.requireNonNull(file.getOriginalFilename()));
                bucket.create(configure.getImagePath() + filename, file.getBytes(), file.getContentType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Product productEntity = productMapper.mapObject(product, Product.class);
        productEntity.setImageName(filename);
        productEntity.setImageUrl(getImageUrl(bucket, filename));

//        Set Product Category
        ProductCategory productCategory = product.getProductCategory();
        if (productCategory != null && productCategory.getId() == null) {
            productCategory = productCategoryRepository.save(productCategory);
        }

        productEntity.setProductCategory(productCategory);

        if (productCategory != null && productCategory.getCategoryTypes() != null) {
            for (ProductCategoryType categoryType : productCategory.getCategoryTypes()) {
                categoryType.setProductCategory(productCategory);
            }
        }

//        Set Product Attributes
        Set<ProductAttribute> productAttributes = product.getAttributes();
//        jika produk atributnya di set atau ada di request
        if (productAttributes != null) {
//            memasukan produk ke produk atribut dan set produk atributnya
            for (ProductAttribute attribute : productEntity.getAttributes()) {
                attribute.setProduct(productEntity);
                productAttributes.add(attribute);
            }
        }else {
//            productAttributes = null atau tidak diisi
            productAttributes = new HashSet<>(productEntity.getAttributes());
        }

        productEntity.setAttributes(productAttributes);


        productEntity = productRepository.save(productEntity);

        return CompletableFuture.completedFuture(productMapper.mapObject(productEntity, AddProductResponseDTO.class));
    }

    @Override
    public ResponseEntity<?> deleteProduct(String id) {
        Optional<Product> products = productRepository.findProductById(id);

        if (products.isEmpty()) {
            throw new CustomerNotFound("product not found by id");
        }

        Product product = products.get();

        productRepository.delete(product);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public GetProductResponseDTO getProducts(List<String> category, Integer page, Integer size, String orderBy) {
        Page<Product> productPage;
        Sort sort = orderBy.split(",")[0].equalsIgnoreCase("desc")
                ? Sort.by(Sort.Direction.DESC, orderBy.split(",")[1])
                : Sort.by(Sort.Direction.ASC, orderBy.split(",")[1]);
        Pageable pageable =  PageRequest.of(
                page,
                size,
                sort
        );

        if (category != null && !category.stream().map(
                    String::toUpperCase
                )
                .allMatch(IProductService::checkCategory)) {
            throw new BadRequest("Unexpected Category");
        }

        if (category != null) {
            productPage = productRepository.findByType(
                    category.stream()
                            .map(String::toUpperCase)
                            .map(CategoryType::valueOf)
                            .collect(Collectors.toList()),
                    pageable
            );
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return new GetProductResponseDTO(productPage, productMapper);
    }

    @Override
    public GetProductDetailsResponseDTO getProductDetails(String id) {
        Optional<Product> products = productRepository.findProductById(id);

        if (products.isEmpty()) {
            throw new CustomerNotFound("product not found by id");
        }

        Product product = products.get();

        return productMapper.mapObject(product, GetProductDetailsResponseDTO.class);
    }

    @Override
    public ResponseEntity<?> updateProductAttribute(String id, RequestUpdateProductAttribute requestUpdateProductAttribute) {
        Optional<Product> products = productRepository.findProductById(id);

        if (products.isEmpty()) {
            throw new CustomerNotFound("product not found by id");
        }

        Product product = products.get();

        Set<ProductAttribute> productAttributes = product.getAttributes();

        if (productAttributes != null) {
            for (ProductAttribute attribute : requestUpdateProductAttribute.getAttributes()) {
                attribute.setProduct(product);
                productAttributes.add(attribute);
            }
        } else {
            productAttributes = new HashSet<>(requestUpdateProductAttribute.getAttributes());
        }

        product.setAttributes(productAttributes);
        productRepository.save(product);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<?> updateQuantityProduct(String id, Size size, Integer quantity) {
        Optional<Product> productOptional = productRepository.findProductById(id);

        if (productOptional.isEmpty()) {
            throw new CustomerNotFound("Product not found by id");
        }

        Product product = productOptional.get();

        productRepository.updateStockByProductIdAndSize(id, quantity, size);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public String getImageUrl(Bucket bucket, String filename) {
        Blob blob = bucket.get(configure.getImagePath() + filename);
        URL signedUrl = blob.signUrl(604800, TimeUnit.HOURS);
        return signedUrl.toString();
    }
}
