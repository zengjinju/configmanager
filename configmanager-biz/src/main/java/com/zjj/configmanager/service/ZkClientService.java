package com.zjj.configmanager.service;

/**
 * Created by admin on 2017/12/2.
 */
public interface ZkClientService {

	void createNode(String parentPath,String path,String value);

	void deleteNode(String parentPath,String path);

	void updateNode(String parentPath,String path);
}
