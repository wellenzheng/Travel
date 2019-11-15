package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {

    private JdbcTemplate jdbcTemplate=new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite=null;
        String sql="select * from tab_favorite where rid = ? and uid = ?";
        try {
            favorite=jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Favorite.class),rid,uid);
        } catch (DataAccessException e) {
            return null;
        }
        return favorite;
    }

    @Override
    public int getCount(int rid) {
        String sql="select count(*) from tab_favorite where rid = ?";
        return jdbcTemplate.queryForObject(sql,Integer.class, rid);
    }

    @Override
    public void addFavorite(int rid, int uid) {
        String sql="insert into tab_favorite(rid,date,uid) values(?,?,?)";
        jdbcTemplate.update(sql,rid,new Date(),uid);
    }
}
