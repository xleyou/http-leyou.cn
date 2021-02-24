package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.reflection.ArrayUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;
    /**
     * 根据条件查询分页
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {

        Example example=new Example(Spu.class);
        Example.Criteria criteria=example.createCriteria();
        //添加查询条件
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }

        //添加上下架的过滤条件
        if(saleable!=null){
            criteria.andEqualTo("saleable",saleable);
        }

        //添加分页
        PageHelper.startPage(page,rows);

        //执行查询，获取spu集合
        List<Spu> spus = this.spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo=new PageInfo<>(spus);
        //spu转换为spubo集合
        List<SpuBo> spuBos = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            //查询品牌名称
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());

            //查询分类名称
            List<String> names = this.categoryService.queryBYBrandIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(names, "-"));
            return spuBo;
        }).collect(Collectors.toList());

        //返回pageResult<SpuBo>
        return new PageResult<>(pageInfo.getTotal(),spuBos);

    }

    /**
     * 新增商品
     * @param spuBo
     * @return
     */
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        //先新增
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insertSelective(spuBo);

        //再去新增spuDetail
        SpuDetail spuDetail=spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(spuDetail);

        saveSkuAndStock(spuBo);

    }

    private void saveSkuAndStock(SpuBo spuBo) {
        spuBo.getSkus().forEach(sku -> {
            //新增sku
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);
            //新增stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }

    /**
     * 根据spuid查询spudetail
     * @param spuId
     * @return
     */
    public SpuDetail querySpuDateilBYSpuId(Long spuId) {
       return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }
    /**
     * 根据spuid查询sku
     * @param spuId
     * @return
     */
    public List<Sku> querySkuBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus=this.skuMapper.select(sku);
        skus.forEach(sku1 -> {
            Stock stock=this.stockMapper.selectByPrimaryKey(sku1.getId());
            sku1.setStock(stock.getStock());
        });
        return skus;
    }
    /**
     * 更新商品
     * @param spuBo
     * @return
     */
    @Transactional
    public void updateGoods(SpuBo spuBo) {
        //根据spuId查询要删除的suk
        Sku sku=new Sku();
        sku.setSpuId(spuBo.getId());
        List<Sku> skus=this.skuMapper.select(sku);
        skus.forEach(sku1 -> {
            //删除stock
            this.stockMapper.deleteByPrimaryKey(sku.getId());
        });

        //删除sku
        Sku sku1=new Sku();
        sku1.setSpuId(spuBo.getId());
        this.skuMapper.delete(sku1);
        //新增sku和stock
        this.saveSkuAndStock(spuBo);
        //更新spu和spuDetail
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spuBo);
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());


    }

    /**
     * 通过spu_id删除商品goods
     *
     * @param spuId
     * @return
     */
    @Transactional
    public void deleteGoods(Long spuId) {
        // 先删除sku和库存信息
        this.deleteSkuAndStock(spuId);
        // 再删除spu和spu_detail
        this.spuMapper.deleteByPrimaryKey(spuId);
        this.spuDetailMapper.deleteByPrimaryKey(spuId);
    }

    /**
     * 通过spu_id删除tb_sku
     * 通过sku_id删除tb_stock
     *
     * @param spuId
     */
    private void deleteSkuAndStock(Long spuId) {
        // 通过spu_id查询sku
        Sku querySku = new Sku();
        querySku.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(querySku);
        // 删除sku
        if (!CollectionUtils.isEmpty(skus)) {
            // 获得sku_id集合
            List<Long> ids = skus.stream().map(sku -> sku.getId()).collect(Collectors.toList());
            // 通过sku_id删除tb_stock
            Example example = new Example(Stock.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("skuId", ids);
            this.stockMapper.deleteByExample(example);
        }
        // 删除sku
        this.skuMapper.delete(querySku);
    }

    /**
     * 通过spu_id修改商品上下架状态
     *
     * @param spuId
     * @return
     */
    public void changeSaleable(Long spuId) {
        // 先查后更新
        Spu dbSpu = this.spuMapper.selectByPrimaryKey(spuId);
        if (null != dbSpu) {
            dbSpu.setSaleable(!dbSpu.getSaleable());
            this.spuMapper.updateByPrimaryKey(dbSpu);
        }
    }
}
