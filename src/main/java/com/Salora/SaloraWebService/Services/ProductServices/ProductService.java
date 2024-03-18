package com.Salora.SaloraWebService.Services.ProductServices;

import com.Salora.SaloraWebService.AppConfiguration;
import com.Salora.SaloraWebService.DTO.RequestDTO.AddProductDTO;
import com.Salora.SaloraWebService.DTO.ResponseDTO.AddProductResponseDTO;
import com.Salora.SaloraWebService.DTO.ResponseDTO.GetProductDetailsResponseDTO;
import com.Salora.SaloraWebService.DTO.ResponseDTO.GetProductResponseDTO;
import com.Salora.SaloraWebService.Exception.BadRequest;
import com.Salora.SaloraWebService.Exception.CustomerNotFound;
import com.Salora.SaloraWebService.Model.Enums.CategoryType;
import com.Salora.SaloraWebService.Model.Product;
import com.Salora.SaloraWebService.Model.ProductCategory;
import com.Salora.SaloraWebService.Model.ProductCategoryType;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.*;
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
    private AppConfiguration configure;

    @Override
    public AddProductResponseDTO addProduct(AddProductDTO product, MultipartFile[] files) {
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

        productEntity = productRepository.save(productEntity);

        return productMapper.mapObject(productEntity, AddProductResponseDTO.class);
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
    public String getImageUrl(Bucket bucket, String filename) {
        Blob blob = bucket.get(configure.getImagePath() + filename);
        URL signedUrl = blob.signUrl(604800, TimeUnit.HOURS);
        return signedUrl.toString();
    }
}
