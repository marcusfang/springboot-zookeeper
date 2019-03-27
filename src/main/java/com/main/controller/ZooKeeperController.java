package com.main.controller;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ZooKeeperController {
	
	@Autowired 
	CuratorFramework client; 
	
	
	/**
	 * http://localhost:8080/createHello
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/createHelloNode")
	@ResponseBody
	public String createHelloNode() throws Exception {
		System.out.println("进入添加根节点方法");
		client.create().forPath("/hello",new byte[0]);
		return "创建Hello节点成功";
	}
	
	/**
	 * http://localhost:8080/isHelloNodeExist
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/isHelloNodeExist")
	@ResponseBody
	public String isHelloNodeExist() throws Exception {
		System.out.println("判断Hello节点是否存在");
		Stat stat = client.checkExists().watched().forPath("/hello");
		if(stat!=null) {
			return "Hello节点存在";
		}else {
			return "Hello节点不存在";
		}
		
	}
	
	
}
