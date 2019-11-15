package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate jdbcTemplate;

    public UserDaoImpl(){
        jdbcTemplate=new JdbcTemplate(JDBCUtils.getDataSource());
    }

    @Override
    public User findByUsername(String username) {
        User user=null;
        try {
            //定义sql
            String sql="select * from tab_user where username = ?";
            //执行sql
            user=jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (Exception e){
            return null;
        }
        return user;
    }

    @Override
    public void save(User user) {
        //定义sql
        String sql="insert into tab_user(username, password, name, birthday, sex, telephone, email, status, code) " +
                "values(?,?,?,?,?,?,?,?,?)";
        //执行sql
        jdbcTemplate.update(sql,user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode());
    }

    @Override
    public User findByCode(String code) {
        User user=null;
        String sql="select * from tab_user where code = ?";
        try {
            user=jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),code);
        } catch (DataAccessException e) {
            return null;
        }
        return user;
    }

    @Override
    public void updateStatus(User user) {
        String sql="update tab_user set status='Y' where uid = ?";
        jdbcTemplate.update(sql,user.getUid());
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        String sql="select * from tab_user where username = ? and password = ?";
        User user=null;
        try {
            user=jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
        } catch (DataAccessException e) {
            return null;
        }
        return user;
    }
}
