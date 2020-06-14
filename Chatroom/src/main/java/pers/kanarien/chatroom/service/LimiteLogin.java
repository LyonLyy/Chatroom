package pers.kanarien.chatroom.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public interface LimiteLogin {
    public String loginLimite(HttpServletRequest request, String userName) ;
}
