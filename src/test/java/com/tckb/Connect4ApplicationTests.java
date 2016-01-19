package com.tckb;

import com.tckb.c4.ws.Connect4Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Connect4Application.class)
@WebAppConfiguration
public class Connect4ApplicationTests {

	@Test
	public void contextLoads() {
	}

}
