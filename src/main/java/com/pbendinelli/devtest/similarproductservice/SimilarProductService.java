package com.pbendinelli.devtest.similarproductservice;

import com.pbendinelli.devtest.similarproductservice.exception.InternalServerException;
import com.pbendinelli.devtest.similarproductservice.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SimilarProductService {

    @Autowired
    private ProductServiceProxy productServiceProxy;


    /**
     * Get a list of similar products from a given product id.
     * This method will call the Product external service.
     *
     * @param productId, id of the product that we are looking similar products
     * @return a list with the details of the similar product
     *
     * @throws NotFoundException if a product with the given id doesn't exist
     * @throws InternalServerException if a server side error occurs
     */
    public List<ProductDetail> getProductSimilar(final String productId) {

        List<ProductDetail> similarProductDetails = new ArrayList<>();

        List<String> similarProductIds = productServiceProxy.getSimilarProductIds(productId);

        Optional<ProductDetail> productDetailOptional;
        for (String similarProductId : similarProductIds) {
            productDetailOptional = productServiceProxy.getProductDetails(similarProductId);
            productDetailOptional.ifPresent(similarProductDetails::add);
        }

        return similarProductDetails;
    }
}
