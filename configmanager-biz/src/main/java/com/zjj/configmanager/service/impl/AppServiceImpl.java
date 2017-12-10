package com.zjj.configmanager.service.impl;

import com.zjj.configmanager.dao.entity.AppServer;
import com.zjj.configmanager.dao.mapper.AppServerMapper;
import com.zjj.configmanager.service.AppServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 2017/12/2.
 */
@Service
public class AppServiceImpl implements AppServerService {
	@Autowired
	private AppServerMapper appServerMapper;

	public List<AppServer> getAll() {
		return appServerMapper.queryAll();
	}

	public AppServer getAppServerByCode(String code) {
		return appServerMapper.getByCode(code);
	}
}
