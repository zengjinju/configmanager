package com.zjj.configmanager.dao.mapper;

import com.zjj.configmanager.dao.entity.ConfigItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConfigItem record);

    int insertSelective(ConfigItem record);

    ConfigItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConfigItem record);

    int updateByPrimaryKey(ConfigItem record);

    List<ConfigItem> queryAll();

    int updateItemByNameAndAppId(ConfigItem item);
}