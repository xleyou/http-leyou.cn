package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

//    public Specification queryById(Long id) {
//        return this.specParamMapper.selectByPrimaryKey(id);
//    }
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam record = new SpecParam();
        record.setGroupId(gid);
        record.setCid(cid);
        record.setGeneric(generic);
        record.setSearching(searching);
        return this.specParamMapper.select(record);
    }

    public List<SpecGroup> queryGroupsBycid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroupList = specGroupMapper.select(specGroup);
        return specGroupList;
    }

    public List<SpecGroup> queryGroupWithParam(Long cid) {
        List<SpecGroup> groups=this.queryGroupsBycid(cid);
        groups.forEach(group -> {
            List<SpecParam> params = this.queryParams(group.getId(), null, null, null);
            group.setParams(params);
        });
        return groups;
    }
}

    private SpecGroupMapper groupMapper;

    @Autowired
    private SpecParamMapper paramMapper;
    /**
     * 根据分类id查询分组
     * @param cid
     * @return
     */
    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return this.groupMapper.select(specGroup);
    }
    /**
     * 根据条件查询规格参数
     * @param gid
     * @return
     */
    public List<SpecParam> queryParams(Long gid,Long cid,Boolean generic,Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        return this.paramMapper.select(specParam);
    }

    /**
     * 新增规格模板分组
     * @param group
     * @return
     */
    public void addGroup(SpecGroup group) {

        this.groupMapper.insertSelective(group);
    }

    /**
     * 更新规格模板分组
     * * @param group
     * @return
     */
    public void updateGroup(SpecGroup group) {
        this.groupMapper.updateByPrimaryKey(group);
    }

    /**
     * 删除规格分组
     * @param id
     */
    public void delectGroup(Long id) {
        this.groupMapper.deleteByPrimaryKey(id);
    }
    /**
     * 新增规格模板下的规格参数
     * @param param
     * @return
     */
    public void saveParam(SpecParam param) {
        this.paramMapper.insertSelective(param);
    }

    /**
     * 更新规格模板下的规格参数
     * @param param
     * @return
     */
    public void updateParam(SpecParam param) {
        this.paramMapper.updateByPrimaryKey(param);
    }
    /**
     * 删除规格模板下的规格参数
     * @param id
     * @return
     */
    public void deleteParam(Long id) {
        this.paramMapper.deleteByPrimaryKey(id);
    }



}