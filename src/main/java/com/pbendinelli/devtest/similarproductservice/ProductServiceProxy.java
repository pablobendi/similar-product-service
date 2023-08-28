package com.pbendinelli.devtest.similarproductservice;


import com.pbendinelli.devtest.similarproductservice.exception.InternalServerException;
import com.pbendinelli.devtest.similarproductservice.exception.NotFoundException;
import com.pbendinelli.devtest.similarproductservice.exception.RestTemplateResponseErrorHandler;
import com.pbendinelli.devtest.similarproductservice.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Proxy to handle the external Product services
 */
@Component
public class ProductServiceProxy {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;

    @Autowired
    public ProductServiceProxy(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @Value("${root.url}")
    private String rootUrl;

    /**
     * Get similar products Ids from the external product service
     *
     * @param productId, id of the product that we are looking similar products
     * @return a list of the similar product IDs
     *
     * @throws NotFoundException if a product with the given id doesn't exist
     * @throws InternalServerException if a server side error occurs
     */
    public List<String> getSimilarProductIds(final String productId) {
        List<String> similarProductIds = new ArrayList<>();

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("productId", productId);

        ResponseEntity<String[]> responseEntity = restTemplate.getForEntity(
                rootUrl + "/product/{productId}/similarids", String[].class,
                uriVariables);

        similarProductIds.addAll(Arrays.asList(responseEntity.getBody()));

        return similarProductIds;
    }


    /**
     * Get the product details from a given ID
     *
     * @param productId, id of the product that we need the details
     * @return an optional with the details of the requested product if the product exist.
     */
    public Optional<ProductDetail> getProductDetails(final String productId) {

        ProductDetail productDetail = null;

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("productId", productId);

        try {

            ResponseEntity<ProductDetail> responseEntity = restTemplate.getForEntity(
                    rootUrl + "/product/{productId}", ProductDetail.class,
                    uriVariables);
            productDetail = responseEntity.getBody();
        } catch (NotFoundException nfe) {
            logger.error(String.format("Product not found for the given id: {productId}", productId), nfe);
        } catch (ValidationException ve) {
            logger.error(String.format("A validation error occurred with the given id: {productId}", productId), ve);
        } catch (InternalServerException ise) {
            logger.error(String.format("An internal server error occurred with the given id: {productId}", productId), ise);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }

        return Optional.ofNullable(productDetail);
    }
}
