package com.zjj.configmanager.service.impl;

import com.zjj.configmanager.ZkBean;
import com.zjj.configmanager.dao.entity.AppServer;
import com.zjj.configmanager.dao.entity.ConfigItem;
import com.zjj.configmanager.dao.mapper.ConfigItemMapper;
import com.zjj.configmanager.service.AppServerService;
import com.zjj.configmanager.service.ConfigItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 2017/12/2.
 */
@Service
public class ConfigItemServiceImpl implements ConfigItemService {

	private static final Logger logger= LoggerFactory.getLogger(ConfigItemServiceImpl.class);

	@Value("${zk_server}")
	private String serverString ;

	@Autowired
	private ConfigItemMapper configItemMapper;
	@Autowired
	private AppServerService appServerService;

	public List<ConfigItem> getAllItem() {
		return configItemMapper.queryAll();
	}

	public void addConfigItem(String appCode,String itemName,String itemValue) {
		AppServer appServer=appServerService.getAppServerByCode(appCode);
		if(appServer==null){
			logger.info("当前应用还没配置，请配置应用");
			return;
		}
		ConfigItem item=new ConfigItem();
		item.setAppServerId(appServer.getId());
		item.setItemName(itemName);
		item.setItemValue(itemValue);
		configItemMapper.insertSelective(item);
		ZkBean zkClient=ZkBean.getZkClient(serverString);
		zkClient.setParentPath(appServer.getAppCode());
		zkClient.createPersistent(itemName,itemValue);
	}

	public Boolean updateConfigItem(String appCode, String itemName, String itemValue) {
		AppServer appServer=appServerService.getAppServerByCode(appCode);
		if(appServer==null){
			logger.info("当前应用还没配置，请在配置中心配置应用");
			return Boolean.FALSE;
		}
		ConfigItem item=new ConfigItem();
		item.setAppServerId(appServer.getId());
		item.setItemName(itemName);
		item.setItemValue(itemValue);
		configItemMapper.updateItemByNameAndAppId(item);
		ZkBean zkClient=ZkBean.getZkClient(serverString);
		zkClient.setParentPath(appCode);
		zkClient.updateNode(itemName,itemValue);
		return Boolean.TRUE;
	}
}
