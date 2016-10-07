package com.seasun.weixinmp.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.seasun.weixinmp.ServerApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServerApplication.class)
public class TableControllerTest {
	
	@Autowired
	private JdbcTemplate jdbc;

	@Test
	public void test() {
		System.out.println(jdbc.queryForMap(
				"select TABLE_NAME tableName, TABLE_COMMENT tableComment from information_schema.tables "
				+ "where table_type = 'BASE TABLE' and TABLE_SCHEMA in ( select database()) "));
	}
	
	@Autowired
	private TableController tc;
	
	@Test
	public void tcTest(){
		System.out.println(tc.tableForeignKeys);
	}

}
