package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component()
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户输入的用户名查询是否有此用户
        //没有此用户返回null
        //有此用户,创建List<GrantedAuthority>集合用于添加授权
        //根据返回的user查询用户拥有的角色user.getRoles()
        //遍历roles获得permission集合
        //遍历permission集合获得用户的所有权限添加到GrantedAuthority集合中
        //最后返回UserDetails对象

        //1.根据用户输入的用户名查询是否有此用户
        User user = userService.findByUsername(username);
        //没有此用户返回null
        if (user == null) {
            return null;
        }
        //有此用户,创建List<GrantedAuthority>集合用于添加授权
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        //根据返回的user查询用户拥有的角色user.getRoles()
        Set<Role> roles = user.getRoles();
        //遍历roles获得permission集合
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                Set<Permission> permissions = role.getPermissions();
                if (permissions != null && permissions.size() > 0) {
                    //遍历permission集合获得用户的所有权限添加到GrantedAuthority集合中
                    for (Permission permission : permissions) {
                        String keyword = permission.getKeyword();
                        //new GrantedAuthority接口的实例华对象
                        list.add(new SimpleGrantedAuthority(keyword));
                    }
                }
            }
        }
//        System.out.println(list);
        //创建UserDetails对象
        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        //最后返回UserDetails对象
        return userDetail;
    }

    /**
     * 用于对传进来的密码进行加密
     * @param password
     * @return
     */
    public String encoder(String password) {
        if (password != null && !password.equals("")) {
            String encodedPssword = encoder.encode(password);
            return encodedPssword;
        }
        return null;
    }
}
