package club.calabashbrothers.zkadmin.controller;

import club.calabashbrothers.zkadmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liaojiacan on 2017/6/28.
 */
@Controller
public class LoginController {


    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response){

        try {
            response.getWriter().write("Hello world");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
