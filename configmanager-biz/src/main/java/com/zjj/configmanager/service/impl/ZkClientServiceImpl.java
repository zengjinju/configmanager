package com.zjj.configmanager.service.impl;

import com.zjj.configmanager.ZkBean;
import com.zjj.configmanager.service.ZkClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by admin on 2017/12/2.
 */
@Service
public class ZkClientServiceImpl implements ZkClientService {

	private ZkBean zkClient;

	@Value("${zk_server}")
	private String serverString;

	@PostConstruct
	public void init() {
		zkClient = ZkBean.getZkClient(serverString);
	}

	public void createNode(String parentPath, String path, String value) {
		zkClient.setParentPath(parentPath);
		zkClient.createPersistent(zkClient.concatPath(path), value);
	}

	public void deleteNode(String parentPath, String path) {

	}

	public void updateNode(String parentPath, String path) {

	}
}
