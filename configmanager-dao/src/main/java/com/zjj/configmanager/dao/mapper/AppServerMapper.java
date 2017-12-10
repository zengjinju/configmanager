package com.zjj.configmanager.dao.mapper;

import com.zjj.configmanager.dao.entity.AppServer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppServerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppServer record);

    int insertSelective(AppServer record);

    AppServer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppServer record);

    int updateByPrimaryKey(AppServer record);

    List<AppServer> queryAll();

    AppServer getByCode(@Param("appCode")String appCode);
}