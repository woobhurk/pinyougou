package com.pinyougou.mapper;

import com.pinyougou.pojo.TbBrand;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface TbBrandMapper extends Mapper<TbBrand> {

    List<TbBrand> findAll();
}