package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

import java.util.UUID;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl() {
        userDao=new UserDaoImpl();
    }

    @Override
    public boolean register(User user) {
        //根据用户名查询用户对象
        User u=userDao.findByUsername(user.getUsername());
        //这里的u不会是null的，因此需要判断它的属性是不是为null
        if(u!=null){
            //用户名存在，注册失败
            return false;
        }

        //设置激活码
        user.setCode(UuidUtil.getUuid());
        //设置激活状态
        user.setStatus("N");
        //保存用户信息
        userDao.save(user);
        //发送激活邮件
        String content="【黑马旅游网】欢迎您："+user.getName()+
                "\n您的用户名为："+user.getUsername()+
                "\n<a href='http://localhost/travel/user/activate?code="+user.getCode()+"'>点击【激活】</a>";

        MailUtils.sendMail(user.getEmail(),content,"激活邮件");

        return true;

    }

    @Override
    public boolean activate(String code) {

        //根据激活码查询用户对象
        User user=userDao.findByCode(code);
        if(user!=null){
            userDao.updateStatus(user);
            return true;
        }
        //调用dao修改激活状态

        return false;
    }

    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
