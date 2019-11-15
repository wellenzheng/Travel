package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import com.sun.tools.javac.util.Pair;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {

    private JdbcTemplate jdbcTemplate=new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int getTotalCount(int cid, String rname) {
        String sql="select count(*) from tab_route where 1=1";
        Pair<StringBuilder, List> stringBuilderListPair = appendSQL(sql, cid, rname);
        StringBuilder stringBuilder = stringBuilderListPair.fst;
        List params = stringBuilderListPair.snd;
        sql=stringBuilder.toString();
        return jdbcTemplate.queryForObject(sql,Integer.class,params.toArray());
    }

    @Override
    public List<Route> getCurrentPage(int cid, int start, int pageSize, String rname) {
        String sql="select * from tab_route where 1=1";
        Pair<StringBuilder, List> stringBuilderListPair = appendSQL(sql, cid, rname);
        StringBuilder stringBuilder = stringBuilderListPair.fst;
        List params = stringBuilderListPair.snd;
        stringBuilder.append(" limit ? , ?");
        sql=stringBuilder.toString();
        params.add(start);
        params.add(pageSize);
        List<Route> routeList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class),params.toArray());
        return routeList;
    }

    @Override
    public Route findRouteByRid(int rid) {
        String sql="select * from tab_route where rid = ?";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Route.class),rid);
    }

    private Pair<StringBuilder,List> appendSQL(String sql, int cid, String rname){
        StringBuilder stringBuilder=new StringBuilder(sql);
        List params=new ArrayList();
        if(cid!=0){
            stringBuilder.append(" and cid = ?");
            params.add(cid);
        }
        if(rname!=null&&rname.length()!=0&&!rname.equals("null")){
            stringBuilder.append(" and rname like ?");
            params.add("%"+rname+"%");
        }
        return new Pair<>(stringBuilder,params);
    }


}
