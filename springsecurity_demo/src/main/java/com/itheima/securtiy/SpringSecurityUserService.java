package com.itheima.securtiy;

import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 认证（根据用户名从数据库查询密码交给框架），密码是否正确是由框架进行对比的
 * 授权 ：根据用户查询角色和权限数据  授权给当前登录的用户
 *
 */
public class SpringSecurityUserService implements UserDetailsService{

    //注入密码加密对象  为明文密码加密
    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 根据用户名加载用户对象
     * @param username 页面用户输入用户名 传入此方法中
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询用户对象（自己的定义用户对象）
        User user = findUserByname(username);
        //2.用户对象不存在 return null
        if(user == null){
            return null;//验证失败
        }
        //3.进行授权 写死的，后续从数据库中查询
        String dbPassword = user.getPassword();
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));//角色表中查询 keyword
        list.add(new SimpleGrantedAuthority("add"));//权限表中查询 keyword
        list.add(new SimpleGrantedAuthority("delete"));//权限表中查询 keyword

        //4.用户对象存在 new User(xxxxx) 注意：此User是框架中的用户对象
        //String username:用户名username, String password：从数据库查询出来的密码
        // Collection<? extends GrantedAuthority> authorities 权限列表
        //return new org.springframework.security.core.userdetails.User(username,"{noop}"+dbPassword,list);
        return new org.springframework.security.core.userdetails.User(username,dbPassword,list);
    }

    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
    private User findUserByname(String username) {
        if("admin".equals(username)){
            //encoder.encode();//密码加密123456  encode框架内部方法会随机产生salt 最终加密的密码每一次都不一样，相当安全
            //encoder.matches();//密码匹配的
            User user = new User();
            user.setPassword(encoder.encode("123456"));//存入到数据的密码一定加密的
            user.setUsername(username);
            return user;
        }
        return null;
    }
}
