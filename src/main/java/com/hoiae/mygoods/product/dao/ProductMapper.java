package com.hoiae.mygoods.product.dao;

import com.hoiae.mygoods.product.dto.CharacterDTO;
import com.hoiae.mygoods.product.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    int registProduct(ProductDTO product);

    int registCharacter(CharacterDTO character);


}
