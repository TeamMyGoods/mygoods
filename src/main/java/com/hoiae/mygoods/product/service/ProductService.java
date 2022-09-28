package com.hoiae.mygoods.product.service;

import com.hoiae.mygoods.product.dao.ProductMapper;
import com.hoiae.mygoods.product.dto.CharacterDTO;
import com.hoiae.mygoods.product.dto.ProductDTO;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductMapper mapper;

    public ProductService(ProductMapper mapper) {
        this.mapper = mapper;
    }


    public void registProduct(ProductDTO product) {
        int result = mapper.registProduct(product);

        if(result<=0) {
//            throw new ProductRegistException("상품 등록에 실패하였습니다.");
        }
    }

    public void registCharacter(CharacterDTO character) {

        int result = mapper.registCharacter(character);

        if(result<=0) {
//            throw new ProductRegistException("상품 등록에 실패하였습니다.");
        }
    }
}
