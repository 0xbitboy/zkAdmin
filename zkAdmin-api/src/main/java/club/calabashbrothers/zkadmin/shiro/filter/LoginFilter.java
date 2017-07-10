package club.calabashbrothers.zkadmin.shiro.filter;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by liaojiacan on 2016/11/14.
 */
public class LoginFilter extends FormAuthenticationFilter{
    private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
        if (((HttpServletRequest)request).getRequestURL().toString().endsWith("login.json")&&((HttpServletRequest) request).getMethod().equals(HttpMethod.POST.name())){
            return true;
        }
        return super.isLoginRequest(request, response);
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        if (this.isAccessAllowed(request, response, mappedValue) && this.isLoginRequest(request, response)) {
            if (((HttpServletRequest)request).getRequestURL().toString().endsWith(".json")){
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("{\"code\":200,\"info\":\"already logined\"}");
                out.flush();
                out.close();
            }else {
                WebUtils.issueRedirect(request,response,this.getSuccessUrl());
            }
            return false;
        }
        return super.onPreHandle(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }
                // allow them to see the login page ;)
                return true;
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the "
                        + "Authentication url [" + getLoginUrl() + "]");
            }
            if (!((HttpServletRequest)request).getRequestURL().toString().endsWith(".json")) {
                saveRequestAndRedirectToLogin(request, response);
            } else {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("{\"code\":-203,\"info\":\"login\"}");
                out.flush();
                out.close();
            }
            return false;
        }
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (!httpServletRequest.getRequestURL().toString().endsWith(".json")) {
            issueSuccessRedirect(request, response);
        } else {

            httpServletResponse.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();
            out.println("{\"code\":200,\"info\":\"登入成功\"}");
            out.flush();
            out.close();
        }

        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        if (!((HttpServletRequest)request).getRequestURL().toString().endsWith(".json")) {
            setFailureAttribute(request, e);
            return true;
        }
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            String message = e.getClass().getSimpleName();
            if ("IncorrectCredentialsException".equals(message)
                    || "UnknownAccountException".equals(message)
                    ) {
                out.println("{\"code\":-100010,\"info\":\"账号或密码错误\"}");
            }else if("ExcessiveAttemptsException".equals(message)){
                out.println("{\"code\":-100020,\"info\":\"密码错误次数超过限制，请10分钟后重试！\"}");
            }else if("LockedAccountException".equals(message)){
                out.println("{\"code\":-100030,\"info\":\"账号已停用！\"}");
            } else {
                out.println("{\"code\":-100500,\"info\":\"未知错误\"}");
            }
            out.flush();
            out.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return false;
    }


    @Override
    protected String getHost(ServletRequest request) {
        return getRealIp((HttpServletRequest) request);

    }

    public static String getRealIp(HttpServletRequest request) {
        String  ip = request.getHeader("X-Real-IP");
        if (ip == null&& "".equals(ip) || "127.0.0.1".equals(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            //  System.out.println("2--"+ip);
        }
        if (ip == null || "".equals(ip)) {
            ip = request.getRemoteAddr();
            //  System.out.println("3--"+ip);
        } else {
            if(ip.indexOf(",")>0)
            {
                ip = ip.split(", ")[0].trim();
                if ("127.0.0.1".equals(ip)) {
                    ip = request.getRemoteAddr();
                    //  System.out.println("4--"+ip);
                }
            }
        }
        if (ip == null) {
            ip = "";
        }
        return ip;
    }

}
