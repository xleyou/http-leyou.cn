package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /**
     * 根据父节点的id查询子节点
     * @param  pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(@RequestParam(value="pid",defaultValue = "0")Long pid){
//        try {
            if(pid==null||pid<0){
                //400：参数不合法
                //return ResponseEntity.status(Htt pStatus.BAD_REQUEST).build();
                //return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                return  ResponseEntity.badRequest().build();
            }
            List<Category> categories=this.categoryService.queryCategoriesByPid(pid);
            if(CollectionUtils.isEmpty(categories)){
                //404:资源服务器未找到
                //return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                return ResponseEntity.notFound().build();
            }
            //200：查询成功
            return ResponseEntity.ok(categories);
       /* } catch (Exception e) {
            e.printStackTrace();
        }
        //500 :服务器内部错误
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    */}
    //根据品牌id查询商品分类
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queyByBrandId(@PathVariable("bid") Long bid){
        List<Category> list=this.categoryService.queryByBrandId(bid);
        if(list==null||list.size()<1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

}
