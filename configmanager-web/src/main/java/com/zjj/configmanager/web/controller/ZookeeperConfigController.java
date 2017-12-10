package com.zjj.configmanager.web.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.zjj.configmanager.dao.entity.AppServer;
import com.zjj.configmanager.dao.entity.ConfigItem;
import com.zjj.configmanager.service.AppServerService;
import com.zjj.configmanager.service.ConfigItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin on 2017/12/3.
 */
@Controller
@RequestMapping("zk")
public class ZookeeperConfigController {

	@Autowired
	private AppServerService appServerService;
	@Autowired
	private ConfigItemService configItemService;

	@RequestMapping("add")
	@ResponseBody
	public Boolean addItem(@RequestParam("appCode")String appCode,@RequestParam("itemName")String itemName,@RequestParam("itemValue")String itemValue){
		configItemService.addConfigItem(appCode,itemName,itemValue);
		return Boolean.TRUE;
	}

	@RequestMapping("update")
	@ResponseBody
	public Boolean updateItem(@RequestParam("appCode")String appCode,@RequestParam("itemName")String itemName,@RequestParam("itemValue")String itemValue){
		return configItemService.updateConfigItem(appCode,itemName,itemValue);
	}
}
