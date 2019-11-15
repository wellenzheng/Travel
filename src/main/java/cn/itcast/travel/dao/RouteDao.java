package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {

    //根据cid来查询总数量
    int getTotalCount(int cid, String rname);

    //根据cid，start，pageSize
    //查询当前页码的内容
    List<Route> getCurrentPage(int cid, int start, int pageSize, String rname);

    Route findRouteByRid(int rid);
}
