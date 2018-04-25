package com.rocketmq.spring.boot;

import com.rocketmq.spring.boot.service.RocketMQProviderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRocketmqProviderApplicationTests {

	@Autowired
	private RocketMQProviderService rocketMQProviderService;

	@Test
	public void contextLoads() {
		rocketMQProviderService.send("topicTest", "tag", "key", ("测试").getBytes());
	}

}
