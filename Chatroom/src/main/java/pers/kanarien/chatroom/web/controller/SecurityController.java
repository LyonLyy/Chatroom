package pers.kanarien.chatroom.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pers.kanarien.chatroom.model.vo.ResponseJson;
import pers.kanarien.chatroom.service.LimiteLogin;
import pers.kanarien.chatroom.service.SecurityService;
import pers.kanarien.chatroom.util.Constant;

@Controller
public class SecurityController {

    @Autowired
    SecurityService securityService;
    @Autowired
    LimiteLogin limiteLogin;

    @GetMapping(value = "/")
    public String toLogin() {
        return "redirect:login";
    }
    @GetMapping(value = "/login")
    public String login_url(){

        return "login";
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJson login(HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes,
            @RequestParam String username, @RequestParam String password) {

        System.out.println("LoginControl =====> execute ");


        String loginLimite = limiteLogin.loginLimite(request, username);
        ResponseJson result = securityService.login(username, password,session);


        if(result.get("msg").equals("一切正常")){

            Object url = request.getSession().getAttribute("redirect_link");
            if (url != null) {
                request.getSession().removeAttribute("redirect_link");
//                return "redirect:" + url.toString();
            }
            return result;
        }

        redirectAttributes.addFlashAttribute("message", result);
//        return "/";
        return result;

    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJson logout(HttpSession session) {
        return securityService.logout(session);
    }

    /**
     * 多用户登录限制,清除session信息(登录信息、提示信息)
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/clearUserSession")
    public String clearUserSession(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        //httpSession.invalidate();
        httpSession.removeAttribute(Constant.USER_TOKEN);
        httpSession.removeAttribute("mess");
        return "success";
    }


}
