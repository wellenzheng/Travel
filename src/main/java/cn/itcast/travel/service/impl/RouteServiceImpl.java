package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;

public class RouteServiceImpl implements RouteService {

    private RouteDao routeDao=new RouteDaoImpl();
    private RouteImgDao routeImgDao=new RouteImgDaoImpl();
    private SellerDao sellerDao=new SellerDaoImpl();
    private FavoriteDao favoriteDao=new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        //封装PageBean对象
        PageBean<Route> pageBean=new PageBean<>();
        //设置当前页码以及每页的大小
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        //设置总的记录数
        int totalCount=routeDao.getTotalCount(cid,rname);
        pageBean.setTotalCount(totalCount);
        //设置记录的内容
        int start=(currentPage - 1)*pageSize;
        pageBean.setList(routeDao.getCurrentPage(cid,start,pageSize,rname));
        //设置总页数
        int totalPage=(int)Math.ceil(totalCount/pageSize)+1;
        pageBean.setTotalPage(totalPage);

        return pageBean;
    }

    @Override
    public Route findRouteByRid(String rid) {
        Route route = routeDao.findRouteByRid(Integer.parseInt(rid));
        route.setRouteImgList(routeImgDao.findByRid(Integer.parseInt(rid)));
        route.setSeller(sellerDao.findBySid(route.getSid()));
        route.setCount(favoriteDao.getCount(Integer.parseInt(rid)));
        return route;
    }
}
