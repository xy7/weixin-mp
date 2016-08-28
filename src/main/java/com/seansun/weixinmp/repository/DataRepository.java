package com.seansun.weixinmp.repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DataRepository implements InitializingBean {

	private static final Log log = LogFactory.getLog(DataRepository.class);
	
	@Value("${result.datasource.url}")
	private String url;
	
	@Value("${result.datasource.username}")
	private String username;
	
	@Value("${result.datasource.password}")
	private String password;
	
	@Value("${result.datasource.driver-class-name}")
	private String driverClassName;
	
	@Value("${result.useXgData:false}")
	private boolean useXgData;
	
	private JdbcTemplate jdbcTemplate;
	
	private NamedParameterJdbcTemplate namejdbc;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public NamedParameterJdbcTemplate getNamejdbc() {
		return namejdbc;
	}
	
	public boolean getUseXgData(){
		return useXgData;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setDriverClassName(driverClassName);
		jdbcTemplate = new JdbcTemplate(ds);
		namejdbc = new NamedParameterJdbcTemplate(ds);
	}

	public List<Map<String, Object>> queryAppName(){
		String selectSql = "select app_id, max(app_name) app_name from game_publish_info group by app_id";
		return jdbcTemplate.queryForList(selectSql);
	}
	
	public Map<String, Object> queryIndustryAvg(){
		String selectSql = "select arpu_avg, remain_rate_avg, odu_avg from weixin_report_industry_avg";
		Map<String, Object> res = jdbcTemplate.queryForMap(selectSql);
		return res;
	}
	
	private static final String accountFilterSql = " > 700 ";
	private static final String rechargeFilterSql = " > 200 ";
	
	//鍥�2
	public List<Map<String, Object>> queryArpuRetain(int startDay, int endDay, int topN, String appid){

		String formatSql = "select  @rn := @rn + 1  rn, a.* "
				+ " from ("
				+ " select app_id"
				+ "   , arpu"
				+ "   , retain_rate"
				+ " from st_app_week"
				+ " where tdate = %d "
				+ " and active_account_idcnt " + accountFilterSql
				+ " order by arpu desc"
				+ " ) a, (select  @rn:=0 ) b where @rn < %d ";
		
		String selectSql = null;
		
		if(null == appid)
			selectSql = String.format(formatSql, startDay, topN);
		else
			selectSql = String.format(formatSql+" or app_id = '%s' ", startDay, topN, appid);

		List<Map<String, Object>> res = jdbcTemplate.queryForList(selectSql);

		return res;
	}
	
	//鍥�3:闇�瑕佹坊鍔犱竷鏃ョ暀瀛�
	public List<Map<String, Object>> queryRetain(int startDay, int endDay, int topN, String appid){
		
		String formatSql = "select  @rn := @rn + 1  rn, a.* "
				+ " from ("
				+ " select app_id"
				+ "   , retain_rate"
				+ "	  , retain_rate_7"
				+ " from st_app_week"
				+ " where tdate = %d "
				+ " and active_account_idcnt " + accountFilterSql
				+ " order by retain_rate desc"
				+ " ) a, (select  @rn:=0 ) b where @rn < %d ";
		
		String selectSql = null;
		
		if(null == appid)
			selectSql = String.format(formatSql, startDay, topN);
		else
			selectSql = String.format(formatSql+" or app_id = '%s' ", startDay, topN, appid);

		List<Map<String, Object>> res = jdbcTemplate.queryForList(selectSql);
		return res;
	}

	//鍥�4
	public List<Map<String, Object>> queryAu(int startDay, int endDay, int topN, String appid){
		
		String formatSql = "select  @rn := @rn + 1  rn, a.* "
				+ " from ("
				+ " select app_id"
				+ "   , active_account_idcnt wau"
				+ "	  , active_device_idcnt wad"
				+ " from st_app_week"
				+ " where tdate = %d "
				+ " and active_account_idcnt " + accountFilterSql
				+ " order by wau desc"
				+ " ) a, (select  @rn:=0 ) b where @rn < %d ";
		
		String selectSql = null;
		
		if(null == appid)
			selectSql = String.format(formatSql, startDay, topN);
		else
			selectSql = String.format(formatSql+" or app_id = '%s' ", startDay, topN, appid);

		List<Map<String, Object>> res = jdbcTemplate.queryForList(selectSql);
		return res;
	}
	
	//鍥�5
	public List<Map<String, Object>> queryRecharge(int startDay, int endDay, int topN, String appid){
		
		String formatSql = "select  @rn := @rn + 1  rn, a.* "
				+ " from ("
				+ " select app_id, sum(recharge) recharge, sum(recharge_rate) recharge_rate, sum(arpu) arpu from ("
				+ " select app_id"
				+ "   , 0 recharge"
				+ "   , recharge_account_idcnt/active_account_idcnt recharge_rate"
				+ "   , arpu"
				+ " from st_app_week"
				+ " where tdate = :startDay "
				+ " and active_account_idcnt " + accountFilterSql
				+ " union all "
				+ " select app_id"
				+ "   , sum(recharge) recharge"
				+ "   , 0 recharge_rate"
				+ "   , 0 arpu"
				+ " from st_app_os_day where tdate >= :startDay and tdate <= :endDay"
				+ " and app_id in (select distinct app_id from st_app_week where tdate = :startDay)"
				+ " group by app_id "
				+ " having avg(recharge) " + rechargeFilterSql
				+ ") a"
				+ " group by app_id having sum(arpu) > 0 "
				+ " order by arpu desc"
				+ " ) a, (select  @rn:=0 ) b where @rn < :topN ";
		
		String selectSql = null;
		
		if(null == appid)
			selectSql = formatSql;
		else
			selectSql = formatSql + " or app_id = :appId ";
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("startDay", startDay);
		paramMap.put("endDay", endDay);
		paramMap.put("topN", topN);
		paramMap.put("appId", appid);

		List<Map<String, Object>> res = namejdbc.queryForList(selectSql, paramMap);
		return res;
	}
	
	//鍥�6
	public List<Map<String, Object>> queryOnlineDur(int startDay, int endDay, int topN, String appid){
		
		String formatSql = "select  @rn := @rn + 1  rn, a.* "
				+ " from ("
				+ " select app_id"
				+ "   , avg(online_dur/active_account_idcnt) online_dur"
				+ " from st_app_os_day"
				+ " where tdate >= %d and tdate <= %d and online_dur > 0 "
				+ " group by app_id " 
				+ " having avg(active_account_idcnt) " + accountFilterSql
				+ " order by online_dur desc"
				+ " ) a, (select  @rn:=0 ) b where @rn < %d ";
		
		String selectSql = null;
		
		if(null == appid)
			selectSql = String.format(formatSql, startDay, endDay, topN);
		else
			selectSql = String.format(formatSql+" or app_id = '%s' ", startDay, endDay, topN, appid);

		List<Map<String, Object>> res = jdbcTemplate.queryForList(selectSql);
		return res;
	}
	
	//鍥�7
	public List<Map<String, Object>> queryOneDayUser(int startDay, int endDay, int topN, String appid){
		
		String formatSql = "select  @rn := @rn + 1  rn, a.* "
				+ " from ("
				+ " select app_id"
				+ "   , odu"
				+ " from st_app_week"
				+ " where tdate = %d "
				+ " and odu > 0 "
				+ " and active_account_idcnt " + accountFilterSql
				+ " order by odu desc"
				+ " ) a, (select  @rn:=0 ) b where @rn < %d ";
		
		String selectSql = null;
		
		if(null == appid)
			selectSql = String.format(formatSql, startDay, topN);
		else
			selectSql = String.format(formatSql+" or app_id = '%s' ", startDay, topN, appid);

		List<Map<String, Object>> res = jdbcTemplate.queryForList(selectSql);
		return res;
	}
	
	//鍒朵綔浜洪澶栫殑鍥捐〃锛氭寚鏍囧��
	public Map<String, Object> queryResultDataByAppid(int startDay, int endDay, String appid){
		
		String formatSql = " select a.app_id, recharge"
				+ "   , recharge_vc"
				+ "   , recharge_account_idcnt"
				+ "   , new_recharge_account_idcnt"
				+ "   , recharge_account_idcnt/active_account_idcnt recharge_rate"
				+ "   , online_dur"
				+ "   , retain_rate retain_rate"
				+ "   , odu"
				+ "   , active_account_idcnt-new_account_idcnt old_account_idcnt"
				+ "   , active_account_idcnt"
				+ "   , new_account_idcnt"
				+ "   , recharge/recharge_account_idcnt arppu"
				+ " from (select * from st_app_week a "
				+ " where tdate = %d "
				+ " and app_id = '%s' limit 1) a"
				+ " join ("
				+ "		select sum(recharge) recharge"
				+ "			, avg(online_dur/active_account_idcnt) online_dur"
				+ "			, sum(new_account_idcnt) new_account_idcnt "
				+ "		from st_app_os_day where tdate >= %d and tdate <= %d and app_id = '%s'"
				+ " ) b"
				;
		
		String selectSql = String.format(formatSql, startDay, appid, startDay, endDay, appid);

		Map<String, Object> res = jdbcTemplate.queryForMap(selectSql);
		return res;
	}
	
	//鍒朵綔浜洪澶栫殑鍥捐〃锛氫竷鏃ュ垎甯冨��
	public List<Map<String, Object>> queryDisDataByAppid(int startDay, int endDay, String appid){
		String selectSql = String.format("select datediff(tdate, %d) dayIdx "
				+ "   , sum(active_account_idcnt) active_account_idcnt" 
				+ "   , avg(online_dur/active_account_idcnt) online_dur " 
				+ " from st_app_os_day "
				+ " where tdate >= %d and tdate <= %d  "
				+ " and app_id = '%s' "
				+ " group by tdate "
				, startDay, startDay, endDay, appid);
		
		List<Map<String, Object>> res = jdbcTemplate.queryForList(selectSql);
		return res;
	}
	
	//杩愯惀闇�姹� ltv
	public List<Map<String, Object>> queryLtv(int startDay, int endDay, int topN, String appid){
		
		String formatSql = "select  @rn := @rn + 1  rn, a.* "
				+ " from ("
				+ " select app_id"
				+ "   , ltv_7 ltv"
				+ " from st_app_week"
				+ " where tdate = %d "
				+ " and ltv_7 > 0 "
				+ " and active_account_idcnt " + accountFilterSql
				+ " order by ltv desc "
				+ " ) a, (select  @rn:=0 ) b where @rn < %d ";
		
		String selectSql = null;
		
		if(null == appid)
			selectSql = String.format(formatSql, startDay, topN);
		else
			selectSql = String.format(formatSql+" or app_id = '%s' ", startDay, topN, appid);

		List<Map<String, Object>> res = jdbcTemplate.queryForList(selectSql);
		return res;
	}
	
	//杩愯惀闇�姹� appCompare
	public List<Map<String, Object>> queryAppCompare(int startDay, int endDay, int topN){

		String formatSql = "select  @rn := @rn + 1  rn, a.* "
				+ " from ("
				+ " select app_id"
				+ "   , sum(new_account_idcnt) new_account_idcnt"
				+ "   , sum(recharge) recharge"
				+ " from st_app_os_day"
				+ " where tdate >= %d and tdate <= %d"
				+ " group by app_id "
				+ " order by recharge desc "
				+ " ) a, (select  @rn:=0 ) b where @rn < %d ";
		
		String selectSql = null;
		
		selectSql = String.format(formatSql, startDay, endDay, topN);
		List<Map<String, Object>> res = jdbcTemplate.queryForList(selectSql);
		return res;
	}
	
	//杩愯惀闇�姹� appCompare 鎸塷s鍖哄垎
	public List<Map<String, Object>> queryAppCompareDetail(int startDay, int endDay, int topN, List<String> appids){
		
		String sql = "select  @rn := @rn + 1  rn, a.app_id, a.os, a.new_account_idcnt, a.recharge"
				+ "	  , (a.new_account_idcnt-a.last_new_account_idcnt)/(case when a.last_new_account_idcnt>0 then a.last_new_account_idcnt else null end) new_account_mom"
				+ "   , (a.recharge-a.last_recharge)/(case when a.last_recharge>0 then a.last_recharge else null end) recharge_mom"
				+ " from ("
				+ " select app_id, os"
				+ "   , sum(case when tdate>=:startDay then new_account_idcnt else null end) new_account_idcnt"
				+ "   , sum(case when tdate>=:startDay then recharge else null end) recharge"
				+ "   , sum(case when tdate<:startDay then new_account_idcnt else null end) last_new_account_idcnt"
				+ "   , sum(case when tdate<:startDay then recharge else null end) last_recharge"
				+ " from st_app_os_day"
				+ " where tdate >= DATE_SUB(:startDay, INTERVAL 1+TIMESTAMPDIFF(DAY, :startDay, :endDay) DAY)"
				+ " and tdate <= :endDay group by app_id, os"
				+ " ) a, (select  @rn:=0 ) b where @rn < :topN and recharge > 0 and new_account_idcnt > 0";

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("startDay", startDay);
		paramMap.put("endDay", endDay);
		paramMap.put("topN", topN);
		paramMap.put("appids", appids);
		
		return namejdbc.queryForList(sql, paramMap);
	}
	
	//杩愯惀闇�姹� appAll
	public Map<String, Object> queryAppAll(int startDay, int endDay){
		String formatSql = " select new_device_idcnt, active_device_idcnt, recharge_account_idcnt, recharge from ("
				+ " select sum(active_device_idcnt) active_device_idcnt"
				+ "   , sum(recharge_account_idcnt) recharge_account_idcnt"
				+ " from st_app_week"
				+ " where tdate = %d ) a join ("
				+ " select sum(new_account_idcnt) new_device_idcnt, sum(recharge) recharge "
				+ " from st_app_os_day where tdate>=%d and tdate <=%d"
				+ " ) b"
				;
		
		String selectSql = null;

		selectSql = String.format(formatSql, startDay, startDay, endDay);
		
		Map<String, Object> res = jdbcTemplate.queryForMap(selectSql);
		return res;
	}
	
	public List<Map<String, Object>> queryAppAllDis(int startDay, int endDay){
		String selectSql = String.format("select datediff(tdate, %d) dayIdx "
				+ "   , sum(new_account_idcnt) new_device_idcnt"
				+ "   , sum(recharge) recharge"
				+ " from st_app_os_day "
				+ " where tdate >= %d and tdate <= %d "
				+ " group by tdate "
				, startDay, startDay, endDay);
		
		List<Map<String, Object>> res = jdbcTemplate.queryForList(selectSql);
		return res;
	}
	
	@Transactional
	public int syncStAppOsDayIndex(int startDay, int endDay){
		
		String sql = "insert into st_app_os_day(tdate, app_id, app_name, os, new_device_idcnt, active_device_idcnt"
				+ "   , new_account_idcnt, active_account_idcnt, recharge, online_dur)"
				+ " select tdate, a.app_id, app_name, os"
				+ "   , sum(new_device_idcnt) new_device_idcnt"
				+ "   , sum(active_device_idcnt) active_device_idcnt"
				+ "   , sum(new_account_idcnt) new_account_idcnt"
				+ "   , sum(active_account_idcnt) active_account_idcnt"
				+ "   , sum(recharge) recharge"
				+ "   , sum(online_dur) online_dur"
				+ " from ("
				+ " select tdate, substr(app_id, :beginIndex) app_id, (case when channel_id in (:iosChannelIds) then 'ios' else 'android' end) os"
				+ "   , sum(new_device_idcnt) new_device_idcnt"
				+ "   , sum(active_device_idcnt) active_device_idcnt"
				+ "   , sum(new_account_idcnt) new_account_idcnt"
				+ "   , sum(active_account_idcnt) active_account_idcnt"
				+ "   , sum(recharge) recharge"
				+ "   , 0 online_dur"
				+ " from aas.st_app_channel"
				+ " where tdate >= :startDay"
				+ " and tdate <= :endDay"
				+ " and tdate_type = 'day' "
				+ " and app_id in (select distinct concat(:appidPrefix, app_id) from game_publish_info)"
				+ "	and app_id not in ('1', '1098') "
				+ " and channel_id != '*'"
				+ " group by tdate, app_id, os "
				+ " union all "
				+ " select tdate, app_id, (case when channel_id in (:iosChannelIds) then 'ios' else 'android' end) os"
				+ "   , 0 new_device_idcnt"
				+ "   , 0 active_device_idcnt"
				+ "   , 0 new_account_idcnt"
				+ "   , 0 active_account_idcnt"
				+ "   , 0 recharge"
				+ "   , sum(online_dur) online_dur"
				+ " from aas.st_app_channel"
				+ " where tdate >= :startDay"
				+ " and tdate <= :endDay"
				+ " and tdate_type = 'day' "
				+ " and app_id in (select distinct app_id from game_publish_info)"
				+ "	and app_id not in ('1', '1098') "
				+ " and channel_id != '*'"
				+ " group by tdate, app_id, os"
				+ " ) a join(select distinct app_id, app_name from game_publish_info) b"
				+ " on a.app_id = b.app_id"
				+ " group by a.tdate, a.app_id, a.os";
		
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("startDay", startDay);
		paramMap.put("endDay", endDay);
		putParamsMap(paramMap);
		List<String> iosChannelIds = Arrays.asList(new String[]{"ios_jinshan"
				, "ios_jinshanApple"
				, "ios_xgApple"
				, "ios_xiaomiapple"
				, "ios_xoyoApple"
				, "apple"
				, "jinshanApple"
				, "xiaomiapple"
				, "ios_xiaomi"});
		
		paramMap.put("iosChannelIds", iosChannelIds);

		int delCnt = namejdbc.update("delete from st_app_os_day where tdate>=:startDay and tdate<=:endDay and app_id not in ('1', '1098')", paramMap);
		return namejdbc.update(sql, paramMap);					
	}
	
	private void putParamsMap(Map<String, Object> paramMap){
		String appidPrefix = "";
		int beginIndex = 1;
		if(useXgData){
			appidPrefix = "xg_";
			beginIndex = 4;
		}
		paramMap.put("beginIndex", beginIndex);
		paramMap.put("appidPrefix", appidPrefix);
	}
	
	@Transactional
	public int syncStAppWeekIndex(int startDay, int endDay){
		
		String sql = "insert into st_app_week(tdate, app_id, app_name, active_device_idcnt, active_account_idcnt"
				+ "   , recharge_vc, recharge_account_idcnt, new_recharge_account_idcnt, arpu, arppu"
				+ "   , odu, retain_rate, retain_rate_7, ltv_7)"
				+ " select tdate, a.app_id, app_name"
				+ "   , sum(active_device_idcnt) active_device_idcnt"
				+ "   , sum(active_account_idcnt) active_account_idcnt"
				+ "   , sum(recharge_vc) recharge_vc"
				+ "   , sum(recharge_account_idcnt) recharge_account_idcnt"
				+ "   , sum(new_recharge_account_idcnt) new_recharge_account_idcnt"
				+ "   , sum(arpu) arpu"
				+ "   , sum(arppu) arppu"
				+ "   , sum(odu) odu"
				+ "   , sum(retain_rate) retain_rate"
				+ "   , sum(retain_rate_7) retain_rate_7"
				+ "	  , sum(ltv_7) ltv_7"
				+ " from ("
				+ "   select tdate, substr(app_id, :beginIndex) app_id, active_device_idcnt, active_account_idcnt"
				+ "     , recharge_vc, recharge_account_idcnt, new_recharge_account_idcnt"
				+ "     , recharge/active_account_idcnt arpu"
				+ "     , recharge/recharge_account_idcnt arppu"
				+ "     , 0 odu"
				+ "     , 0 retain_rate"
				+ "		, 0 retain_rate_7, 0 ltv_7"
				+ "   from aas.st_app "
				+ "   where tdate = :startDay"
				+ "   and tdate_type = 'week' "
				+ "   and app_id in (select distinct concat(:appidPrefix,app_id) from game_publish_info)"
				+ "	  and app_id not in ('1', '1098') "
				+ "   group by tdate, app_id"
				+ "   union all "
				+ "   select tdate, substr(app_id, :beginIndex) app_id"
				+ "     , 0 active_device_idcnt, 0 active_account_idcnt"
				+ "     , 0 recharge_vc, 0 recharge_account_idcnt, 0 new_recharge_account_idcnt"
				+ "     , 0 arpu"
				+ "     , 0 arppu"
				+ "     , 0 odu"
				+ "     , 0 retain_rate"
				+ "     , avg(new_device_retained_idcnt/new_device_idcnt) retain_rate_7"
				+ "		, avg(new_device_recharge/new_device_idcnt) ltv_7"
				+ "   from aas.st_app_channel_difftype"
				+ "   where tdate = :startDay"
				+ "   and app_id in (select distinct concat(:appidPrefix,app_id) from game_publish_info)"
				+ "	  and app_id not in ('1', '1098') "
				+ "   and  channel_id = '*'"
				+ "   and difftype = 6"
				+ "   group by tdate, app_id"
				+ "   union all "	
				+ "   select min(tdate) tdate, substr(app_id, :beginIndex) app_id"
				+ "     , 0 active_device_idcnt, 0 active_account_idcnt"
				+ "     , 0 recharge_vc, 0 recharge_account_idcnt, 0 new_recharge_account_idcnt"
				+ "     , 0 arpu"
				+ "     , 0 arppu"
				+ "     , 0 odu"
				+ "     , avg(new_device_retained_idcnt/new_device_idcnt) retain_rate"
				+ "     , 0 retain_rate_7"
				+ "		, 0 ltv_7"
				+ "   from aas.st_app_channel_difftype"
				+ "   where tdate >= :startDay"
				+ "   and tdate < :endDay"
				+ "   and app_id in (select distinct concat(:appidPrefix,app_id) from game_publish_info)"
				+ "	  and app_id not in ('1', '1098') "
				+ "   and  channel_id = '*'"
				+ "   and difftype = 1"
				+ "   group by app_id"
				+ "   union all"
				+ "   select tdate, substr(app_id, :beginIndex) app_id, 0 active_device_idcnt, 0 active_account_idcnt"
				+ "     , 0 recharge_vc, 0 recharge_account_idcnt, 0 new_recharge_account_idcnt"
				+ "     , 0 arpu"
				+ "     , 0 arppu"
				+ "     , onlyoneday_account_idcnt/new_account_idcnt odu"
				+ "     , 0 retain_rate"
				+ "		, 0 retain_rate_7, 0 ltv_7"
				+ "   from aas.st_app "
				+ "   where tdate = :startDay"
				+ "   and tdate_type = 'day' "
				+ "   and app_id in (select distinct concat(:appidPrefix,app_id) from game_publish_info)"
				+ "	  and app_id not in ('1', '1098') "
				+ "   group by tdate, app_id"
				+ " ) a join(select distinct app_id, app_name from game_publish_info) b"
				+ " on a.app_id = b.app_id"
				+ " group by a.tdate, a.app_id";
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("startDay", startDay);
		paramMap.put("endDay", endDay);
		putParamsMap(paramMap);
		
		int delCnt = namejdbc.update("delete from st_app_week where tdate>=:startDay and tdate<=:endDay and app_id not in ('1', '1098')", paramMap);

		return namejdbc.update(sql, paramMap);
	}

}
