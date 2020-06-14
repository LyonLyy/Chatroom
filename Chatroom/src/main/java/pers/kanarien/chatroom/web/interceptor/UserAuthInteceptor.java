package pers.kanarien.chatroom.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import pers.kanarien.chatroom.model.po.UserInfo;
import pers.kanarien.chatroom.model.vo.ResponseJson;
import pers.kanarien.chatroom.service.UserInfoService;
import pers.kanarien.chatroom.util.Constant;

import java.util.HashMap;
import java.util.Map;

public class UserAuthInteceptor implements HandlerInterceptor {
    @Autowired
    UserInfoService userInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        Object userToken = session.getAttribute(Constant.USER_TOKEN);
        ResponseJson userInfo_json = userInfoService.getByUserId((String) userToken);
        HashMap data = (HashMap)userInfo_json.get("data");

        UserInfo userInfo = (UserInfo) data.get("userInfo");

        if (userToken == null) {
            /*JsonMsgHelper.writeJson(response, new ResponseJson(HttpStatus.FORBIDDEN).setMsg("请登录"),
                    HttpStatus.FORBIDDEN);*/
            response.sendRedirect("login");
            return false;
        }


        //多用户登录限制判断，并给出提示
        boolean isLogin = false;
        if(userToken!=null) {
            Map<String, String> loginUserMap = (Map<String, String>) session.getServletContext().getAttribute("loginUserMap");
            String sessionId = session.getId();
            for (String key : loginUserMap.keySet()) {
                //用户已在另一处登录
                if (key.equals(userInfo.getUsername()) && !loginUserMap.containsValue(sessionId)) {
                    isLogin = true;
                    break;
                }
            }
        }
        if(isLogin){
            Map<String, String> loginOutTime = (Map<String, String>) session.getServletContext().getAttribute("loginOutTime");
            session.setAttribute("mess", "用户：" + userInfo.getUsername() + "，于 " + loginOutTime.get(userInfo.getUsername()) + " 已在别处登录!");
            loginOutTime.remove(userInfo.getUsername());
            session.getServletContext().setAttribute("loginOutTime", loginOutTime);
            response.sendRedirect(request.getContextPath() + "/");
            return false;

        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials","true");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

}
