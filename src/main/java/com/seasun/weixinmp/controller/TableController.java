package com.seasun.weixinmp.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TableController implements InitializingBean{
	
	private static final String _ID = "_id";
	private static final Log log = LogFactory.getLog(TableController.class);
	
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private NamedParameterJdbcTemplate nameJdbc;
	
	private Map<String, String> tables = null;
	private Map<String, Map<String, String>> tableColumns = null;
	public Map<String, List<KVCom>> tableForeignKeys = null; // table, col, foreign table, foreign col
	public Map<String, Map<String, KVCom>> tableForeignKVComs = new HashMap(){ // table, col, foreignKeyComment
		{
			Map<String, KVCom> colFks = new HashMap<>();
			colFks.put("card_id", new KVCom("card_info", null, "name"));
			colFks.put("class_id", new KVCom("class_info", null, "class_name"));
			put("card_class", colFks);
		}
	};
	
	private Map<String, String> transMap(List<Map<String, Object>> tables, String keyName, String valueName){
		Map<String, String> res = new HashMap<>();
		for(Map<String, Object> table:tables){
			String key = table.get(keyName).toString();
			String value = table.get(valueName).toString();
			res.put(key, value);
		}
		return res;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(tables == null){ //从配置表中查出所有的表
			tables = transMap(jdbc.queryForList(
				"select TABLE_NAME tableName, TABLE_COMMENT tableComment from information_schema.tables "
				+ "where table_type = 'BASE TABLE' and TABLE_SCHEMA in ( select database()) ")
					, "tableName", "tableComment");
		}
		
		if(tableColumns == null){
			tableColumns = new HashMap<>();
			for(String tableName:tables.keySet()){
				List<Map<String, Object>> columnList = jdbc.queryForList(
						"select COLUMN_NAME columnName, if(COLUMN_COMMENT='', COLUMN_NAME, COLUMN_COMMENT) columnComment from information_schema.columns "
						+ "where table_name='" + tableName + "'" );
				
				tableColumns.put(tableName, transMap(columnList, "columnName", "columnComment") );
			}
		}
		
		//外健查询
		if(tableForeignKeys == null){
			tableForeignKeys = new HashMap<>();
			for(String tableName:tables.keySet()){
				String sql = "select COLUMN_NAME `key`, REFERENCED_TABLE_NAME `value`, REFERENCED_COLUMN_NAME `comment` "
						+ "from information_schema.KEY_COLUMN_USAGE where TABLE_NAME = '"+tableName+"' and REFERENCED_TABLE_NAME is not null";
				
				List<KVCom> list = nameJdbc.query(sql, new RowMapper<KVCom>(){

					@Override
					public KVCom mapRow(ResultSet rs, int rowNum) throws SQLException {
						// TODO Auto-generated method stub
						return new KVCom(rs.getString(1), rs.getString(2), rs.getString(3));
					}
					
				});
				
				if(list.size() > 0)
					tableForeignKeys.put(tableName, list);
			}
		}
		
		log.info("init table finish.");
	}
	

	@RequestMapping("/showTables")
	public String showTables(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", "数据库中有如下表：");
	
		model.put("list", tables);
		
		return "showTables";
    }
	
	@RequestMapping(value="/editTableData/{tableName}")
	public String editTableData(Map<String, Object> model, @PathVariable("tableName") String tableName, HttpServletRequest request) {
		model.put("tableName", tableName);
		//从表中查出数据
		Map<String, String> columnList = tableColumns.get(tableName);
		
		model.put("columnList", columnList);
		
		//从表中查出数据
		Map<String, String> searchList = columnList;
//				jdbc.queryForList(
//				"select COLUMN_NAME columnName, if(COLUMN_COMMENT='', COLUMN_NAME, COLUMN_COMMENT) columnComment from information_schema.columns "
//				+ "where table_name='" + tableName + "'" + " and COLUMN_NAME != '" + _ID + "' and ORDINAL_POSITION in (2,3,4)");
		
		model.put("searchList", searchList);
		
		System.out.println(searchList);
		
		StringBuilder sb = new StringBuilder();
		for(String col:searchList.keySet()){
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
	
	public static class Tuple<T1, T2, T3>{
		public T1 c1; public T2 c2; public T3 c3;

		@Override
		public String toString() {
			return "Tuple [c1=" + c1 + ", c2=" + c2 + ", c3=" + c3 + "]";
		}

		public Tuple(T1 c1, T2 c2, T3 c3) {
			this.c1 = c1;this.c2 = c2;this.c3 = c3;
		}
		
		public static List<Tuple<String, Object, String>> parse(Map<String, String> columnList, Map<String, Object> map
				, Map<String, List<String>> fkValues){
			List<Tuple<String, Object, String>> res = new ArrayList<>();
			for(String key:columnList.keySet()){
				String comment = columnList.get(key);
				Object value = map==null?null:map.get(key);
				if(fkValues.containsKey(key)){
					value = fkValues.get(key);
				}
				res.add(new Tuple<String, Object, String>(key, value, comment));
			}
			return res;
		}
		
	}
	
	public static class KVCom {
		public String key;
		public Object value;
		public String comment;
		
		public KVCom(String key, Object value, String comment) {
			this.key = key;
			this.value = value;
			this.comment = comment;
		}

		@Override
		public String toString() {
			return "KVCom [key=" + key + ", value=" + value + ", comment=" + comment + "]";
		}

		static List<KVCom> parse(Map<String, String> columnList, Map<String, Object> map){
			List<KVCom> res = new ArrayList<>();
			for(String key:columnList.keySet()){
				String comment = columnList.get(key);
				String value = map==null?null:map.get(key).toString();
				res.add(new KVCom(key, value, comment));
			}
			return res;
		}
		
		public static List<KVCom> parse(Map<String, String> columnList, Map<String, Object> map
				, Map<String, List<KVCom>> fkValues){
			List<KVCom> res = new ArrayList<>();
			for(String key:columnList.keySet()){
				String comment = columnList.get(key);
				Object value = map==null?null:map.get(key);
				if(fkValues.containsKey(key)){
					value = fkValues.get(key);
				}
				res.add(new KVCom(key, value, comment));
			}
			return res;
		}
	}

	@RequestMapping(value="/editTableData/showTableRow", method = RequestMethod.POST)
	public String showTableRow(Map<String, Object> model, HttpServletRequest request) {
		
		String tableName = request.getParameter("tableName");
		model.put("tableName", tableName);
		
		Map<String, List<KVCom>> fkValues = new HashMap<>();
		Map<String, KVCom> fkList = tableForeignKVComs.get(tableName);
		for(Map.Entry<String, KVCom> e:fkList.entrySet()){
			String fkTable = e.getValue().key;
			String fkComm = e.getValue().comment;
			String fkSql = String.format("select _id `value`, %s `comment` from %s", fkComm, fkTable);
			
			List<KVCom> list = nameJdbc.query(fkSql, new RowMapper<KVCom>(){

				@Override
				public KVCom mapRow(ResultSet rs, int rowNum) throws SQLException {
					// TODO Auto-generated method stub
					return new KVCom(rs.getString(1), null, rs.getString(2));
				}

			});
			
			String col = e.getKey();

			fkValues.put(col, list);
		}
		model.put("fks", fkValues.keySet());
		
		Map<String, String> columnComms = tableColumns.get(tableName);
		System.out.println(columnComms);

		int id = Integer.parseInt(request.getParameter(_ID) );
		Map<String, Object> map = null;
		
		List<KVCom> kvcs = new ArrayList<>();
		
		if(id == 0){
			model.put("isInsert", 1);
		} else{
			String sql = "select * from " + tableName + " where " + _ID + " = " + id + " limit 1";
			System.out.println(sql);
			map = jdbc.queryForMap(sql);
			log.info("show table row: " + map);
		}
		
		kvcs = KVCom.parse(columnComms, map, fkValues);
		model.put("kvcs", kvcs);
		
		return "tableRow";
	}

	@RequestMapping(value="/editTableData/editTableRow/update", method = RequestMethod.POST)
	public String updateTableRow(HttpServletRequest request) {
		
		String tableName = request.getParameter("tableName");

		Map<String, String> columnList = tableColumns.get(tableName);
		
		StringBuilder sb = new StringBuilder();

		for(String col:columnList.keySet()){
			if(col.equals(_ID))
				continue;
			
			String value = request.getParameter(col);
			if(value != null && !value.isEmpty() && !value.equalsIgnoreCase("null")){
				sb.append(",`").append(col).append("`=").append("'").append(value).append("'");
			}
		}
		
		int id = Integer.parseInt(request.getParameter(_ID) );
		
		String sql = "update " + tableName + " set " + sb.toString().substring(1) + " where " + _ID + "=" + id;
		System.out.println(sql);
		int res = jdbc.update(sql);
		log.info("update res: " + res);
		return "redirect:/editTableData/" + tableName;
	}
	
	@RequestMapping(value="/editTableData/editTableRow/insert", method = RequestMethod.POST)
	public String insertTableRow(HttpServletRequest request) {
		
		String tableName = request.getParameter("tableName");

		
		StringBuilder sbCol = new StringBuilder();
		StringBuilder sbValue = new StringBuilder();
		
		Map<String, String> columnList = tableColumns.get(tableName);

		for(String col:columnList.keySet()){
			if(col.equals(_ID))
				continue;
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
