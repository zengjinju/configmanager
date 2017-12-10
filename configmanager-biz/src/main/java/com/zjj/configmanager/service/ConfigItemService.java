package com.zjj.configmanager.service;

import com.zjj.configmanager.dao.entity.ConfigItem;

import java.util.List;

/**
 * Created by admin on 2017/12/2.
 */
public interface ConfigItemService {

	List<ConfigItem> getAllItem();

	void addConfigItem(String appCode,String itemName,String itemValue);

	Boolean updateConfigItem(String appCode,String itemName,String itemValue);
}
