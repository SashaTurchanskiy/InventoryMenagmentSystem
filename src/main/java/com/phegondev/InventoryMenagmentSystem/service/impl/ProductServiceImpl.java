package com.phegondev.InventoryMenagmentSystem.service.impl;

import com.phegondev.InventoryMenagmentSystem.dto.ProductDto;
import com.phegondev.InventoryMenagmentSystem.dto.Response;
import com.phegondev.InventoryMenagmentSystem.entity.Category;
import com.phegondev.InventoryMenagmentSystem.entity.Product;
import com.phegondev.InventoryMenagmentSystem.exception.NotFoundException;
import com.phegondev.InventoryMenagmentSystem.repository.CategoryRepo;
import com.phegondev.InventoryMenagmentSystem.repository.ProductRepo;
import com.phegondev.InventoryMenagmentSystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;
    private final CategoryRepo categoryRepo;

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-image/";

    @Override
    public Response saveProduct(ProductDto productDto, MultipartFile imageFile) {

        Category category = categoryRepo.findById(productDto.getProductId())
                .orElseThrow(()-> new NotFoundException("Category not found with id: " + productDto.getProductId()));

        //convert the productDto to a product entity
        Product productToSave = Product.builder()
                .name(productDto.getName())
                .sku(productDto.getSku())
                .price(productDto.getPrice())
                .stockQuantity(productDto.getStockQuantity())
                .description(productDto.getDescription())
                .category(category)
                .build();

        //save the image file and get the image path
        if (imageFile != null){
            String imagePath = saveImage(imageFile);
            productToSave.setImageUrl(imagePath);
        }
        //save the product entity to the database
        productRepo.save(productToSave);

        log.info("Product saved successfully: {}", productToSave);
        return Response.builder()
                .status(200)
                .message("Product saved successfully")
                .build();


    }

    @Override
    public Response updateProduct(ProductDto productDto, MultipartFile imageFile) {

        Product existingProduct = productRepo.findById(productDto.getId())
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productDto.getId()));

        //check if image is associated with the product
        if (imageFile != null && !imageFile.isEmpty()){
            String imagePath = saveImage(imageFile);
            existingProduct.setImageUrl(imagePath);
        }
        //check if category is to be changed for the product
        if (productDto.getCategoryId() != null && productDto.getName().isBlank()){
            Category category = categoryRepo.findById(productDto.getProductId())
                    .orElseThrow(() -> new NotFoundException("Category not found with id: " + productDto.getProductId()));
        }
        //update the product entity with the new values
        if (productDto.getName() != null && !productDto.getName().isBlank()) {
            existingProduct.setName(productDto.getName());
        }
        if (productDto.getSku() != null && !productDto.getSku().isBlank()) {
            existingProduct.setSku(productDto.getSku());
        }
        if (productDto.getDescription() != null && !productDto.getDescription().isBlank()) {
            existingProduct.setDescription(productDto.getDescription());
        }
        if (productDto.getPrice() != null && productDto.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            existingProduct.setPrice(productDto.getPrice());
        }
        if (productDto.getStockQuantity() != null && productDto.getStockQuantity() >= 0) {
            existingProduct.setStockQuantity(productDto.getStockQuantity());
        }
        //save the updated product entity to the database
        productRepo.save(existingProduct);
        log.info("Product updated successfully: {}", existingProduct);
        return Response.builder()
                .status(200)
                .message("Product updated successfully")
                .build();
    }

    @Override
    public Response getAllProducts() {
        log.info("Retrieving all products");
        List<Product> products = productRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<ProductDto> productDTOS = modelMapper.map(products, new org.modelmapper.TypeToken<List<ProductDto>>(){}.getType());

        log.info("Retrieved {} products successfully", productDTOS.size());

        return Response.builder()
                .status(200)
                .message("Products retrieved successfully")
                .products(productDTOS)
                .build();
    }

    @Override
    public Response getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

        ProductDto productDto = modelMapper.map(product, ProductDto.class);

        log.info("Product with id {} retrieved successfully", id);

        return Response.builder()
                .status(200)
                .message("Product retrieved successfully")
                .product(productDto)
                .build();
    }

    @Override
    public Response deleteProduct(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
        productRepo.delete(product);
        log.info("Product with id {} deleted successfully", id);

        return Response.builder()
                .status(200)
                .message("Product deleted successfully")
                .build();
    }

    public String saveImage(MultipartFile imageFile) {
        //validate the image file
        if (!imageFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("File is not an image");
        }
        //create the directory if it does not exist
        File directory = new File(IMAGE_DIRECTORY);

        if(!directory.exists()){
            directory.mkdir();
            log.info("Image directory created at: {}", IMAGE_DIRECTORY);
        }
        //create a unique file name
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        //get the full path for the image file
        String imagePath = IMAGE_DIRECTORY + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to save image file: " + e.getMessage());
        }
        return imagePath;
    }
}
