package pers.kanarien.chatroom.service.impl;

import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.kanarien.chatroom.model.po.UserInfo;
import pers.kanarien.chatroom.model.vo.ResponseJson;
import pers.kanarien.chatroom.service.LimiteLogin;
import pers.kanarien.chatroom.service.UserInfoService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pers.kanarien.chatroom.web.interceptor.* ;
@Service
public class LimiteLofinimpl implements LimiteLogin {

    private  static Logger log= LoggerFactory.getLogger(SessionListener.class);
    private static Map<String, String> loginUserMap = new HashMap<>();//存储在线用户
    private static Map<String, String> loginOutTime = new HashMap<>();//存储剔除用户时间

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public String loginLimite(HttpServletRequest request, String userName) {

       ResponseJson userInfo_json =userInfoService.getByUserName(userName);

        HashMap data = (HashMap)userInfo_json.get("data");

        UserInfo userInfo = (UserInfo) data.get("userInfo");
        String sessionId=request.getSession().getId();
       for(String key:loginUserMap.keySet()){
           if(key.equals(userInfo.getUsername())&&!loginUserMap.containsValue(sessionId)){
               Date date=new Date();

               log.info("用户：" + userInfo.getUsername() + "，于" +date.toString() + "被剔除！");
               loginOutTime.put(userInfo.getUsername(),date.toString());
               loginUserMap.remove(userInfo.getUsername());
               break;
           }

       }

        loginUserMap.put(userInfo.getUsername(), sessionId);
        request.getSession().getServletContext().setAttribute("loginUserMap", loginUserMap);
        request.getSession().getServletContext().setAttribute("loginOutTime", loginOutTime);

        return "success";
    }




}
