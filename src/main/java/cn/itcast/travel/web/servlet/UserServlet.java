package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;


//匹配所有/user/下的路径
@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    private UserService userService=new UserServiceImpl();

    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //校验验证码
        String check=request.getParameter("check");
        //从session中获取验证码
        HttpSession session=request.getSession();
        String checkCode=(String)session.getAttribute("CHECKCODE_SERVER");
        //保证验证码只能使用一次
        session.removeAttribute("CHECKCODE_SERVER");
        //比对验证码
        if(checkCode==null||!checkCode.equalsIgnoreCase(check)){
            ResultInfo info=new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            writeValue(info,response);
            return;
        }

        //获取数据
        Map<String, String[]> map=request.getParameterMap();
        //封装对象
        User user=new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //调用service完成注册
        boolean flag=userService.register(user);
        ResultInfo info=new ResultInfo();
        if(flag) {
            info.setFlag(true);
        } else {
            info.setFlag(false);
            info.setErrorMsg("注册失败！");
        }

        writeValue(info,response);
    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户名和密码
        Map<String, String[]> parameterMap = request.getParameterMap();
        User user=new User();

        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        User u=userService.login(user);
        ResultInfo info=new ResultInfo();

        if(u==null){
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        if(u!=null && !u.getStatus().equals("Y")){
            info.setFlag(false);
            info.setErrorMsg("该用户未激活");
        }
        if(u!=null && u.getStatus().equals("Y")){
            request.getSession().setAttribute("user",u);
            info.setFlag(true);
        }
        writeValue(info,response);
    }

    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Object user=request.getSession().getAttribute("user");
        writeValue(user,response);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //销毁session
        request.getSession().invalidate();
        //重定向到login页面
        response.sendRedirect(request.getContextPath()+"/login.html");
    }

    public void activate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取激活码
        String code = request.getParameter("code");
        if(code!=null){
            //调用service完成激活
            boolean flag=userService.activate(code);

            String msg=null;
            //判断激活码
            if(flag){
                //激活成功
                msg="激活成功，请【<a href='login.html'>登录</a>】";
            } else {
                //激活失败
                msg="激活失败，请联系管理员";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }
}
