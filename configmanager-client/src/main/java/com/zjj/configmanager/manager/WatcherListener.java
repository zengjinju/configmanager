package com.zjj.configmanager.manager;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;

import java.util.List;

/**
 * Created by admin on 2017/12/2.
 */
public class WatcherListener implements IZkDataListener,IZkChildListener {
	public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {

	}

	public void handleDataChange(String dataPath, Object data) throws Exception {

	}

	public void handleDataDeleted(String dataPath) throws Exception {

	}
}
