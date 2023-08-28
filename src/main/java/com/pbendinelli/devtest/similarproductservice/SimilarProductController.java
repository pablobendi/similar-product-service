package com.pbendinelli.devtest.similarproductservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class SimilarProductController {

    @Autowired
    private SimilarProductService similarProductService;


    @GetMapping("/product/{productId}/similar")
    public List<ProductDetail> getProductSimilar(@PathVariable String productId) {
        return similarProductService.getProductSimilar(productId);
    }


}
