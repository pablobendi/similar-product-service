package com.pbendinelli.devtest.similarproductservice;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.pbendinelli.devtest.similarproductservice.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SimilarProductServiceTest {

    @InjectMocks
    private SimilarProductService similarProductService;

    @Mock
    private ProductServiceProxy productServiceProxy;



    @Test
    public void getProductSimilar() {
        List<String> similarProductsIds = List.of("2", "3", "4");
        ProductDetail productDetail = new ProductDetail("2", "Dress", BigDecimal.TEN, true);

        when(productServiceProxy.getSimilarProductIds(anyString())).thenReturn(similarProductsIds);
        when(productServiceProxy.getProductDetails("2")).thenReturn(Optional.of(productDetail));
        when(productServiceProxy.getProductDetails("3")).thenReturn(Optional.empty());
        when(productServiceProxy.getProductDetails("4")).thenReturn(Optional.empty());

        List<ProductDetail> productDetails = similarProductService.getProductSimilar("1");

        assertThat(productDetails)
                .hasSize(1)
                .containsOnly(productDetail);
    }

    @Test
    public void getProductSimilar_empty_details(){

        List<String> similarProductsIds = List.of("2", "3", "4");
        ProductDetail productDetail = new ProductDetail("2", "Dress", BigDecimal.TEN, true);

        when(productServiceProxy.getSimilarProductIds(anyString())).thenReturn(similarProductsIds);
        when(productServiceProxy.getProductDetails("2")).thenReturn(Optional.empty());
        when(productServiceProxy.getProductDetails("3")).thenReturn(Optional.empty());
        when(productServiceProxy.getProductDetails("4")).thenReturn(Optional.empty());

        List<ProductDetail> productDetails = similarProductService.getProductSimilar("1");

        assertThat(productDetails)
                .isEmpty();
    }

    @Test
    public void getProductSimilar_product_not_found(){

        doThrow(NotFoundException.class).when(productServiceProxy).getSimilarProductIds("1");

        assertThatThrownBy(() -> {
            similarProductService.getProductSimilar("1");
        }).isInstanceOf(NotFoundException.class);

    }

}