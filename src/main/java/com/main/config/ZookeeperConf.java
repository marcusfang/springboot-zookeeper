package com.main.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConf {

	@Bean 
	public CuratorFramework getCuratorFramework() { 
		RetryPolicy retryPolicy =new ExponentialBackoffRetry(1000,3); 
		CuratorFramework client= CuratorFrameworkFactory.newClient("10.168.1.39:2181",
		retryPolicy); 
		client.start(); 
		return client; 
	}
}
