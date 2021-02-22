package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired
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
    public List<SpecParam> queryParams(Long gid) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
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