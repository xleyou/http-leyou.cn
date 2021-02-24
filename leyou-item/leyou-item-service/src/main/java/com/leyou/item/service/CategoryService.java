package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据父节点查询子节点
     * @param pid
     * @return
     */
    public List<Category> queryCategoriesByPid(Long pid){
        Category category=new Category();
        category.setParentId(pid);
        return this.categoryMapper.select(category);
    }

    //根据id查询分类
    public List<Category> queryByBrandId(Long bid){
        return this.categoryMapper.queryByBrandId(bid);
    }

    public List<String> queryBYBrandIds(List<Long> ids){
        List<Category> categories = this.categoryMapper.selectByIdList(ids);
        return categories.stream().map(category -> category.getName()).collect(Collectors.toList());

    }
}
