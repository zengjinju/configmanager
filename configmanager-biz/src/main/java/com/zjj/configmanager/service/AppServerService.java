package com.zjj.configmanager.service;

import com.zjj.configmanager.dao.entity.AppServer;

import java.util.List;

/**
 * Created by admin on 2017/12/2.
 */
public interface AppServerService {

	List<AppServer> getAll();

	AppServer getAppServerByCode(String code);
}
