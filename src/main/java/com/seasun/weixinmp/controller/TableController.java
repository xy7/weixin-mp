package com.seasun.weixinmp.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TableController {
	
	private static final String _ID = "_id";
	private static final Log log = LogFactory.getLog(TableController.class);
	
	@Autowired
	private JdbcTemplate jdbc;

	@RequestMapping("/showTables")
	public String showTables(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", "数据库中有如下表：");
		//从配置表中查出所有的表
		List<Map<String, Object>> list = jdbc.queryForList(
				"select TABLE_NAME tableName, TABLE_COMMENT tableComment from information_schema.tables "
				+ "where table_type = 'BASE TABLE' and TABLE_SCHEMA in ( select database()) ");
	
		model.put("list", list);
		
		return "showTables";
    }
	
	@RequestMapping(value="/editTableData/{tableName}")
	public String editTableData(Map<String, Object> model, @PathVariable("tableName") String tableName, HttpServletRequest request) {
		model.put("tableName", tableName);
		//从表中查出数据
		List<Map<String, Object>> columnList = jdbc.queryForList(
				"select COLUMN_NAME columnName, if(COLUMN_COMMENT='', COLUMN_NAME, COLUMN_COMMENT) columnComment from information_schema.columns "
				+ "where table_name='" + tableName + "'" + " and COLUMN_NAME != '" + _ID + "'"); 
		
		model.put("columnList", columnList);
		
		//从表中查出数据
		List<Map<String, Object>> searchList = jdbc.queryForList(
				"select COLUMN_NAME columnName, if(COLUMN_COMMENT='', COLUMN_NAME, COLUMN_COMMENT) columnComment from information_schema.columns "
				+ "where table_name='" + tableName + "'" + " and COLUMN_NAME != '" + _ID + "' and ORDINAL_POSITION in (2,3,4)");
		
		model.put("searchList", searchList);
		
		System.out.println(searchList);
		
		StringBuilder sb = new StringBuilder();
		for(Map<String, Object> m:searchList){
			String col = m.get("columnName").toString();
			String value = request.getParameter(col);
			if(value != null && !value.isEmpty()){
				sb.append(" and ").append(col).append("=").append("'").append(value).append("'");
			}
		}

		String sql = "select * from " + tableName + " where 1=1 " + sb.toString();
		System.out.println(sql);
		List<Map<String, Object>> dataList = jdbc.queryForList(sql);

		model.put("dataList", dataList);
		System.out.println(dataList);

		return "editTableData";
	}
	
	static class KVCom {
		public String key;
		public Object value;
		public String comment;
		
		public KVCom(String key, Object value, String comment) {
			this.key = key;
			this.value = value;
			this.comment = comment;
		}

		static List<KVCom> parse(Map<String, String> columnList, Map<String, Object> map){
			List<KVCom> res = new ArrayList<>();
			for(String key:columnList.keySet()){
				String comment = columnList.get(key);
				Object value = map==null?null:map.get(key);
				res.add(new KVCom(key, value, comment));
			}
			return res;
		}
	}
	
	@RequestMapping(value="/editTableData/showTableRow", method = RequestMethod.POST)
	public String showTableRow(Map<String, Object> model, HttpServletRequest request) {
		
		String tableName = request.getParameter("tableName");
		model.put("tableName", tableName);
		
		List<Map<String, Object>> columnList = jdbc.queryForList(
				"select COLUMN_NAME columnName, if(COLUMN_COMMENT='', COLUMN_NAME, COLUMN_COMMENT) columnComment from information_schema.columns "
				+ "where table_name='" + tableName + "'" ); 
		
		Map<String, String> columnComms = new HashMap<>();
		for(Map<String, Object> map:columnList){
			columnComms.put(map.get("columnName").toString(), map.get("columnComment").toString());
		}
		
		System.out.println(columnComms);
		
		int id = Integer.parseInt(request.getParameter(_ID) );
		
		if(id == 0){
			model.put("kvcs", KVCom.parse(columnComms, null));
			model.put("isInsert", 1);
			return "tableRow";
		}
		
		String sql = "select * from " + tableName + " where " + _ID + " = " + id + " limit 1";

		System.out.println(sql);
		Map<String, Object> map = jdbc.queryForMap(sql);
		log.info("show table row: " + map);
		
		model.put("kvcs", KVCom.parse(columnComms, map));
		
		return "tableRow";
	}

	@RequestMapping(value="/editTableData/editTableRow/update", method = RequestMethod.POST)
	public String updateTableRow(HttpServletRequest request) {
		
		String tableName = request.getParameter("tableName");
		int id = Integer.parseInt(request.getParameter(_ID) );
		
		List<String> columnList = jdbc.queryForList(
				"select COLUMN_NAME columnName from information_schema.columns "
				+ "where table_name='"+tableName+"' and COLUMN_NAME != '" + _ID + "'", String.class);
		
		StringBuilder sb = new StringBuilder();

		for(String col:columnList){
			String value = request.getParameter(col);
			if(value != null && !value.isEmpty() && !value.equalsIgnoreCase("null")){
				sb.append(",`").append(col).append("`=").append("'").append(value).append("'");
			}
		}
		
		String sql = "update " + tableName + " set " + sb.toString().substring(1) + " where " + _ID + "=" + id;
		System.out.println(sql);
		int res = jdbc.update(sql);
		log.info("update res: " + res);
		return "redirect:/editTableData/" + tableName;
	}
	
	@RequestMapping(value="/editTableData/editTableRow/insert", method = RequestMethod.POST)
	public String insertTableRow(HttpServletRequest request) {
		
		String tableName = request.getParameter("tableName");
		
		List<String> columnList = jdbc.queryForList(
				"select COLUMN_NAME columnName from information_schema.columns "
				+ "where table_name='"+tableName+"' and COLUMN_NAME != '" + _ID + "'", String.class);
		
		StringBuilder sbCol = new StringBuilder();
		StringBuilder sbValue = new StringBuilder();

		for(String col:columnList){
			String value = request.getParameter(col);
			if(value != null && !value.isEmpty() && !value.equalsIgnoreCase("null")){
				sbCol.append(",`").append(col).append("`");
				sbValue.append(",").append("'").append(value).append("'");
			}
		}
		
		String sql = "insert into " + tableName + " (" + sbCol.toString().substring(1) 
				+ ") values( " + sbValue.toString().substring(1) + ")" ;
		System.out.println(sql);
		int res = jdbc.update(sql);
		log.info("insert res: " + res);
		
		return "redirect:/editTableData/" + tableName;
	}
	
	@RequestMapping(value="/editTableData/editTableRow/delete", method = RequestMethod.POST)
	public String deleteTableRow(HttpServletRequest request) {
		
		String tableName = request.getParameter("tableName");
		int id = Integer.parseInt(request.getParameter(_ID) );
		
		String sql = "delete from " + tableName + " where " + _ID + "=" + id;
		System.out.println(sql);
		int res = jdbc.update(sql);
		log.info("delete res: " + res);
		return "redirect:/editTableData/" + tableName;
	}

}
