package com.seansun.weixinmp.mapper;

import java.util.List;

import com.seansun.weixinmp.service.model.WechatPushContent;
import com.seansun.weixinmp.service.model.WechatPushUser;
import com.seansun.weixinmp.service.vo.ProductNameAndXgAppId;

public interface ReportMapper {
    
    void add(WechatPushContent wechatPushContent);
    
    List<WechatPushContent> getAllReports();
    
    WechatPushContent getReportById(int reportId);
    
    void updateReportById(WechatPushContent wechatPushContent);
    
    void addUser(WechatPushUser wechatPushUser);
    
    int updateUser(WechatPushUser wechatPushUser);
    
    int deleteUser(int userId);
    
    WechatPushUser getUserByOpenId(String openId);
    
    WechatPushUser getUserByNickname(String nickname);
    
    List<WechatPushUser> queryAllUser();
    
    List<ProductNameAndXgAppId> queryProductNameAndXgAppIds();
    
    List<ProductNameAndXgAppId> queryProductByAppids(List<String> appids);
    List<ProductNameAndXgAppId> queryAllProduct();
}
