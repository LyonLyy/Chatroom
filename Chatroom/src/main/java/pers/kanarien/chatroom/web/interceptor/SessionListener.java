package pers.kanarien.chatroom.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pers.kanarien.chatroom.model.po.UserInfo;
import pers.kanarien.chatroom.model.vo.ResponseJson;
import pers.kanarien.chatroom.service.UserInfoService;
import pers.kanarien.chatroom.util.Constant;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.xml.registry.infomodel.User;

import java.util.HashMap;
import java.util.Map;

public class SessionListener implements HttpSessionListener {
    @Autowired
    UserInfoService userInfoService;
    private static Logger log = LoggerFactory.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent event) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        String sessionId = session.getId();
        //在session销毁的时候,把loginUserMap中保存的键值对清除
        Object userToken = session.getAttribute(Constant.USER_TOKEN);
        ResponseJson userInfo_json = userInfoService.getByUserId((String) userToken);
        HashMap data = (HashMap)userInfo_json.get("data");

        UserInfo userInfo = (UserInfo) data.get("userInfo");


        if (userInfo != null) {
            Map<String, String> loginUserMap = (Map<String, String>) event.getSession().getServletContext().getAttribute("loginUserMap");
            if(loginUserMap.get(userInfo.getUsername()).equals(sessionId)){
                log.info("clean user from application : " + userInfo.getUsername());
                loginUserMap.remove(userInfo.getUsername());
                event.getSession().getServletContext().setAttribute("loginUserMap", loginUserMap);
            }
        }

    }

}
