package com.zjj.configmanager;

import org.I0Itec.zkclient.ZkClient;

/**
 * Created by admin on 2017/12/2.
 */
public class ZkBean {
	private volatile static ZkBean zkBean;
	private ZkClient zkClient;
	private String serverString;
	private int sessionTimeout;
	private int connectionTimeout;
	private String parentPath;

	public ZkBean(String serverString) {
		this.serverString = serverString;
		zkClient = new ZkClient(serverString);
	}

	public ZkBean(String serverString, int connectionTimeout) {
		this.serverString = serverString;
		this.connectionTimeout = connectionTimeout;
		zkClient = new ZkClient(serverString, connectionTimeout);
	}

	public ZkBean(String serverString, int sessionTimeout, int connectionTimeout) {
		this.serverString = serverString;
		this.sessionTimeout = sessionTimeout;
		this.connectionTimeout = connectionTimeout;
		zkClient = new ZkClient(serverString, sessionTimeout, connectionTimeout);
	}

	public String concatPath(String path) {
		if (path.startsWith("/")) {
			return parentPath.concat(path);
		}
		return parentPath.concat("/").concat(path);
	}

	public void createPersistent(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		zkClient.createPersistent(path);
	}

	public void createPersistent(String path, String value) {
		zkClient.createPersistent(concatPath(path), value);
	}

	public void updateNode(String path, String value) {
		if (zkClient.exists(concatPath(path))) {
			zkClient.delete(concatPath(path));
			zkClient.createPersistent(concatPath(path), value);
		}
	}

	public Boolean isExit(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return zkClient.exists(path);
	}

	public String getServerString() {
		return serverString;
	}

	public void setServerString(String serverString) {
		this.serverString = serverString;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		if (parentPath.startsWith("/")) {
			this.parentPath = parentPath;
		} else {
			this.parentPath = "/" + parentPath;
		}
	}

	public static ZkBean getZkClient(String serverString) {
		if (zkBean == null) {
			synchronized (ZkBean.class) {
				if (zkBean == null) {
					zkBean = new ZkBean(serverString);
				}
			}
		}
		return zkBean;
	}
}
