package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;


    /**
     * 根据分类id查询参数组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> groups=this.specificationService.queryGroupsByCid(cid);
        if(CollectionUtils.isEmpty(groups)){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
    }
//    @GetMapping("groups/{cid}")
//    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable("cid")Long cid){
//        List<SpecGroup> groups = this.specificationService.queryGroupsByCid(cid);
//        if (CollectionUtils.isEmpty(groups)){
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(groups);
//    }

    /**
     * 根据条件查询规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParams(@RequestParam("gid")Long gid){
        List<SpecParam> params=this.specificationService.queryParams(gid);
        if (CollectionUtils.isEmpty(params)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }

    /**
     * 新增规格模板分组
     * @param group
     * @return
     */
    @PostMapping("group")
    public ResponseEntity<Void> addGroup(@RequestBody SpecGroup group){
        System.out.println(group.getCid()+","+group.getName());
        this.specificationService.addGroup(group);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新规格模板分组
     * * @param group
     * @return
     */
    @PutMapping("group")
    public ResponseEntity<Void> updtaeGroup(@RequestBody SpecGroup group){
        this.specificationService.updateGroup(group);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 删除规格分组
     * @param id
     * @return
     */
    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id){
        this.specificationService.delectGroup(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 新增规格模板下的规格参数
     * @param param
     * @return
     */
    @PostMapping("param")
    public ResponseEntity<Void> saveParam(@RequestBody SpecParam param){
        this.specificationService.saveParam(param);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 更新规格模板下的规格参数
     * @param param
     * @return
     */
    @PutMapping("param")
    public ResponseEntity<Void> updateParam(@RequestBody SpecParam param){
        this.specificationService.updateParam(param);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 删除规格模板下的规格参数
     * @param id
     * @return
     */
    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteParam(@PathVariable Long id){
        this.specificationService.deleteParam(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}