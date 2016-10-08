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
public class TableController implements InitializingBean {

	private static final String _ID = "_id";
	private static final String _NAME = "_name";
	private static final Log log = LogFactory.getLog(TableController.class);

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
		Map<String, String> res = new HashMap<>();
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
									+ "where table_type = 'BASE TABLE' and TABLE_SCHEMA in ( select database()) ")
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

	@RequestMapping("/showTables")
	public String showTables(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", "目录：");

		model.put("list", tables);

		return "showTables";
	}

	@RequestMapping(value = "/editTableData/{tableName}")
	public String editTableData(Map<String, Object> model, @PathVariable("tableName") String tableName,
			HttpServletRequest request) {
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

		StringBuilder sb = new StringBuilder();
		for (String col : tableColumnsMap.get(tableName).keySet()) {
			String value = request.getParameter(col);
			if (value != null && !value.isEmpty()) {
				sb.append(" and `").append(col).append("`=").append("'").append(value).append("'");
			}
		}

		String sql = "select * from " + tableName + " where 1=1 " + sb.toString();
		System.out.println(sql);
		List<Map<String, Object>> dataList = jdbc.queryForList(sql);

		if (tableForeignKeys.containsKey(tableName)) {
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

		return "editTableData";
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

	@RequestMapping(value = "/editTableData/showTableRow", method = RequestMethod.POST)
	public String showTableRow(Map<String, Object> model, HttpServletRequest request) {

		String tableName = request.getParameter("tableName");
		model.put("tableName", tableName);

		Map<String, List<KVCom>> fkValues = new HashMap<>();
		List<KVCom> fkList = tableForeignKeys.getOrDefault(tableName, new ArrayList<>());
		for (KVCom e : fkList) {
			String fkTable = e.value.toString();
			String fkSql = String.format("select %s `value`, %s `comment` from %s", _ID, _NAME, fkTable);
			List<KVCom> list = nameJdbc.query(fkSql, new RowMapper<KVCom>() {

				@Override
				public KVCom mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new KVCom(rs.getString(1), null, rs.getString(2));
				}

			});

			String col = e.key;
			fkValues.put(col, list);
		}
		model.put("fks", fkValues.keySet());

		Map<String, String> columnComms = tableColumnsMap.get(tableName);
		System.out.println(columnComms);

		int id = Integer.parseInt(request.getParameter(_ID));
		Map<String, Object> map = null;

		List<KVCom> kvcs = new ArrayList<>();

		if (id == 0) {
			model.put("isInsert", 1);
		} else {
			String sql = "select * from " + tableName + " where " + _ID + " = " + id + " limit 1";
			System.out.println(sql);
			map = jdbc.queryForMap(sql);
			log.info("show table row: " + map);
		}

		kvcs = KVCom.leftJoin(columnComms, map, fkValues);
		model.put("kvcs", kvcs);

		return "tableRow";
	}

	@RequestMapping(value = "/editTableData/editTableRow/update", method = RequestMethod.POST)
	public String updateTableRow(HttpServletRequest request) {

		String tableName = request.getParameter("tableName");

		Map<String, String> columnList = tableColumnsMap.get(tableName);

		StringBuilder sb = new StringBuilder();

		for (String col : columnList.keySet()) {
			if (col.equals(_ID))
				continue;

			String value = request.getParameter(col);
			if (value != null && !value.isEmpty() && !value.equalsIgnoreCase("null")) {
				sb.append(",`").append(col).append("`=").append("'").append(value).append("'");
			}
		}

		int id = Integer.parseInt(request.getParameter(_ID));

		String sql = "update " + tableName + " set " + sb.toString().substring(1) + " where " + _ID + "=" + id;
		System.out.println(sql);
		int res = jdbc.update(sql);
		log.info("update res: " + res);
		return "redirect:/editTableData/" + tableName;
	}

	@RequestMapping(value = "/editTableData/editTableRow/insert", method = RequestMethod.POST)
	public String insertTableRow(HttpServletRequest request) {

		String tableName = request.getParameter("tableName");

		StringBuilder sbCol = new StringBuilder();
		StringBuilder sbValue = new StringBuilder();

		Map<String, String> columnList = tableColumnsMap.get(tableName);

		for (String col : columnList.keySet()) {
			if (col.equals(_ID))
				continue;
			String value = request.getParameter(col);
			if (value != null && !value.isEmpty() && !value.equalsIgnoreCase("null")) {
				sbCol.append(",`").append(col).append("`");
				sbValue.append(",").append("'").append(value).append("'");
			}
		}

		String sql = "insert into " + tableName + " (" + sbCol.toString().substring(1) + ") values( "
				+ sbValue.toString().substring(1) + ")";
		System.out.println(sql);
		int res = jdbc.update(sql);
		log.info("insert res: " + res);

		return "redirect:/editTableData/" + tableName;
	}

	@RequestMapping(value = "/editTableData/editTableRow/delete", method = RequestMethod.POST)
	public String deleteTableRow(HttpServletRequest request) {

		String tableName = request.getParameter("tableName");
		int id = Integer.parseInt(request.getParameter(_ID));

		String sql = "delete from " + tableName + " where " + _ID + "=" + id;
		System.out.println(sql);
		int res = jdbc.update(sql);
		log.info("delete res: " + res);
		return "redirect:/editTableData/" + tableName;
	}

}
