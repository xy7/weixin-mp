package com.seasun.weixinmp.controller;

import static org.junit.Assert.*;

import org.junit.Test;

public class TableControllerTest2 {

	@Test
	public void test() {
		int totalRows = 1;
		int size = 3;
		int totalPages = (int)Math.ceil((double)totalRows/size);
		System.out.println(totalPages);
	}

}
