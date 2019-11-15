package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService=new RouteServiceImpl();
    private FavoriteService favoriteService=new FavoriteServiceImpl();

    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收参数
        String currentPageStr=request.getParameter("currentPage");
        String pageSizeStr=request.getParameter("pageSize");
        String cidStr=request.getParameter("cid");

        //接收线路名称rname
        String rname = request.getParameter("rname");
        if(rname!=null&&rname.length()!=0)
            rname=new String(rname.getBytes("iso-8859-1"),"utf-8");

        int cid=0;
        if(cidStr!=null&&cidStr.length()>0&&!"null".equals(cidStr)){
            cid=Integer.parseInt(cidStr);
        }
        int currentPage=1;
        if(currentPageStr!=null&&currentPageStr.length()>0){
            currentPage=Integer.parseInt(currentPageStr);
        }
        int pageSize=5;
        if(pageSizeStr!=null&&pageSizeStr.length()>0){
            pageSize=Integer.parseInt(pageSizeStr);
        }

        PageBean pageBean=routeService.pageQuery(cid, currentPage, pageSize, rname);

        writeValue(pageBean, response);

    }

    public void findRoute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取参数rid
        String rid=request.getParameter("rid");
        //根据rid查询Route对象
        Route route=routeService.findRouteByRid(rid);
        //将数据写回客户端
        writeValue(route,response);
    }

    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String rid = request.getParameter("rid");
        User user= (User) request.getSession().getAttribute("user");
        int uid=0;
        if(user!=null){
            uid=user.getUid();
        }
        boolean flag = favoriteService.isFavorite(rid, uid);
        writeValue(flag,response);
    }

    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String rid=request.getParameter("rid");
        User user= (User) request.getSession().getAttribute("user");
        ResultInfo info=new ResultInfo();
        int uid=0;
        if(user!=null){
            uid=user.getUid();
            favoriteService.addFavorite(rid,uid);
        } else {
            info.setFlag(false);
            info.setErrorMsg("尚未登录，请登录");
            writeValue(info,response);
            return;
        }

    }
}
