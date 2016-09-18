package com.seansun.weixinmp.service.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.seasun.weixinmp.ServerApplication;
import com.seasun.weixinmp.mapper.ReportMapper;




@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServerApplication.class)
public class ClassInfoTest {
	
	@Autowired
	private ReportMapper reportMapper;

	@Test
	public void test() {
		System.out.println(reportMapper.test());
	}

}
