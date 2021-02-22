package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<Brand> {
    @Insert("INSERT INTO tb_category_brand (category_id,brand_id) VALUES(#{cid},#{bid}) ")
    void insertCategoryAndBrand(@Param("cid") Long cid, @Param("bid") Long bid);
    @Update("update tb_category_brand set category_id=#{cid} where brand_id=#{bid}")
    void updateCategoryAndBrand(@Param("cid") Long cid, @Param("bid") Long bid);


}
