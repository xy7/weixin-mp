package com.seasun.weixinmp.mapper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.seasun.weixinmp.ServerApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServerApplication.class)
public class ReportMapperTest {

	@Autowired
	private ReportMapper reportMapper;

	@Test
	public void testTest() {
		System.out.println(reportMapper.test());
	}
	
	@Test
	public void getCardInfoByIdTest(){
		System.out.println(reportMapper.getCardInfoById(1));
		System.out.println(reportMapper.getCardInfoById(2));
	}

}
