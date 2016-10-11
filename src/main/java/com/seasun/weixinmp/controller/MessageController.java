package com.seasun.weixinmp.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
public class MessageController implements InitializingBean {

	private static final String _ID = "_id";
	private static final String _NAME = "_name";
	private static final Log log = LogFactory.getLog(MessageController.class);

	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private NamedParameterJdbcTemplate nameJdbc;

	private Map<String, String> tables = null;
	// table, col, null, comment
	private Map<String, List<KVCom>> tableColumns = null;
	// table, col, null, comment
	private Map<String, Map<String, String>> tableColumnsMap = new HashMap<>();
	// table, col, foreign, table, foreign col
	public Map<String, List<KVCom>> tableForeignKeys = null;

	private Map<String, String> transMap(List<Map<String, Object>> tables) {
		Map<String, String> res = new TreeMap<>();
		for (Map<String, Object> table : tables) {
			String key = table.get("keyName").toString();
			String value = table.get("valueName").toString();
			res.put(key, value);
		}
		return res;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (tables == null) { // 从配置表中查出所有的表
			tables = transMap(
					jdbc.queryForList(
							"select TABLE_NAME keyName, TABLE_COMMENT valueName from information_schema.tables "
									+ " where table_type = 'BASE TABLE' and TABLE_SCHEMA in ( select database()) "
									+ " and TABLE_NAME like '\\_%' order by TABLE_COMMENT")
					);
		}

		System.out.println(tables);

		if (tableColumns == null) {
			tableColumns = new HashMap<>();
			for (String tableName : tables.keySet()) {
				String sql = "select COLUMN_NAME columnName, if(COLUMN_COMMENT='', COLUMN_NAME, COLUMN_COMMENT) columnComment "
						+ " from information_schema.columns " + " where table_name='" + tableName + "'"
						+ " order by ordinal_position ";

				List<KVCom> list = nameJdbc.query(sql, new RowMapper<KVCom>() {

					@Override
					public KVCom mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new KVCom(rs.getString(1), null, rs.getString(2));
					}

				});

				tableColumns.put(tableName, list);
				Map<String, String> map = new HashMap<>();
				for (KVCom kvc : list) {
					map.put(kvc.key, kvc.comment);
				}
				tableColumnsMap.put(tableName, map);
			}
		}

		System.out.println(tableColumnsMap);

		// 外健查询
		if (tableForeignKeys == null) {
			tableForeignKeys = new HashMap<>();
			for (String tableName : tables.keySet()) {
				String sql = "select COLUMN_NAME `key`, REFERENCED_TABLE_NAME `value`, REFERENCED_COLUMN_NAME `comment` "
						+ "from information_schema.KEY_COLUMN_USAGE where TABLE_NAME = '" + tableName
						+ "' and REFERENCED_TABLE_NAME is not null";

				List<KVCom> list = nameJdbc.query(sql, new RowMapper<KVCom>() {

					@Override
					public KVCom mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new KVCom(rs.getString(1), rs.getString(2), rs.getString(3));
					}

				});

				if (list.size() > 0)
					tableForeignKeys.put(tableName, list);
			}
		}

		System.out.println(tableForeignKeys);

		log.info("init table finish.");
	}
	
	@RequestMapping("/sendMessage")
	public @ResponseBody String sendMessage(Map<String, Object> model,
			HttpServletRequest request) {
		return "todo: call weixin mp api to send!";
	}
	
	@RequestMapping("/showHistoryMessage")
	public @ResponseBody String showHistoryMessage(Map<String, Object> model,
			HttpServletRequest request) {
		return "todo: show all History Message!";
	}

	@RequestMapping("/showMessage")
	public String showMessage(Map<String, Object> model,
			HttpServletRequest request) {
		String tableName = "_message_class";
		model.put("tableName", tableName);
		model.put("tableComment", tables.get(tableName) );
		// 从表中查出数据
		List<KVCom> columnList = tableColumns.get(tableName);

		model.put("columnList", columnList);

		// 从表中查出数据
		// List<KVCom> searchList = columnList;
		// jdbc.queryForList(
		// "select COLUMN_NAME columnName, if(COLUMN_COMMENT='', COLUMN_NAME,
		// COLUMN_COMMENT) columnComment from information_schema.columns "
		// + "where table_name='" + tableName + "'" + " and COLUMN_NAME != '" +
		// _ID + "' and ORDINAL_POSITION in (2,3,4)");

		model.put("searchList", columnList);

		String sql = "select * from " + tableName; 
		System.out.println(sql);
		
		List<Map<String, Object>> dataList = null;
		if (tableForeignKeys.containsKey(tableName)) {
			//先查出所有的数据，然后再过滤掉不满足查询条件的数据
			dataList = jdbc.queryForList(sql);
			replaceFkId2Name(tableName, dataList);
			
			Map<String, String> searchNames = new HashMap<>();
			for (String col : tableColumnsMap.get(tableName).keySet()) {
				String value = request.getParameter(col);
				if (value != null && !value.isEmpty()) {
					searchNames.put(col, value);
				}
			}
			
			int searchNum = searchNames.size();
			
			Iterator<Map<String, Object>> it = dataList.iterator();
			while(it.hasNext()){
				Map<String, Object> colMap = it.next();
				int hitNum = 0;
				for(Map.Entry<String, String> e:searchNames.entrySet()){
					if(colMap.get(e.getKey()).toString().equals(e.getValue())){
						hitNum ++;
					}
				}
				
				if(hitNum != searchNum)
					it.remove();
			}
			
		} else {
			StringBuilder sb = new StringBuilder();
			for (String col : tableColumnsMap.get(tableName).keySet()) {
				String value = request.getParameter(col);
				if (value != null && !value.isEmpty()) {
					sb.append(" and `").append(col).append("`=").append("'").append(value).append("'");
				}
			}
			sql += " where 1=1 " + sb.toString();
			dataList = jdbc.queryForList(sql);
		}

		
		System.out.println(dataList);
		
		int totalRows = dataList.size();
		int size = 5;
		int totalPages = (int)Math.ceil((double)totalRows/size);
		String curPage = request.getParameter("page");
		int number = Integer.parseInt( curPage == null ? "0" : curPage);
		
		int fromIndex = number * size;
		int toIndex = (number+1) * size;
		model.put("dataList", dataList.subList(fromIndex < totalRows ? fromIndex:0, toIndex < totalRows ? toIndex:totalRows ) );
		
		Map<String, Object> contactsPage = new HashMap<>();
		contactsPage.put("totalPages", totalPages);
		contactsPage.put("number", number);
		contactsPage.put("size", size);
		contactsPage.put("firstPage", number == 0);
		contactsPage.put("lastPage", number == totalPages - 1);
		model.put("contactsPage", contactsPage);

		return "showMessage";
	}

	private void replaceFkId2Name(String tableName, List<Map<String, Object>> dataList) {
		List<KVCom> kvcs = tableForeignKeys.get(tableName);
		for (KVCom colFktabFkCol : kvcs) {
			String sql2 = String.format("select %s keyName, %s valueName from %s", _ID, _NAME, colFktabFkCol.value);
			List<Map<String, Object>> fkNames = jdbc.queryForList(sql2);
			Map<String, String> fkNamesMap = transMap(fkNames);

			String col = colFktabFkCol.key;
			for (Map<String, Object> data : dataList) {
				String dataFkId = data.get(col).toString();
				if (fkNamesMap.containsKey(dataFkId)) {
					data.put(col, fkNamesMap.get(dataFkId));
				}
			}

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

		static List<KVCom> leftJoin(Map<String, String> columnList, Map<String, Object> map) {
			List<KVCom> res = new ArrayList<>();
			for (String key : columnList.keySet()) {
				String comment = columnList.get(key);
				String value = map == null ? null : map.get(key).toString();
				res.add(new KVCom(key, value, comment));
			}
			return res;
		}

		public static List<KVCom> leftJoin(Map<String, String> columnList, Map<String, Object> map,
				Map<String, List<KVCom>> fkValues) {
			List<KVCom> res = new ArrayList<>();
			for (String key : columnList.keySet()) {
				String comment = columnList.get(key);
				Object value = map == null ? null : map.get(key);
				if (fkValues.containsKey(key)) {
					value = fkValues.get(key);
				}
				res.add(new KVCom(key, value, comment));
			}
			return res;
		}
	}


}
