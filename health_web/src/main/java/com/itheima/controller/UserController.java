package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constan.MessageConstant;
import com.itheima.constan.RedisConstant;
import com.itheima.constan.RedisMessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Reference
    private RoleService roleService;
    @Reference
    private UserService userService;

    /**
     * 获取用户名
     *
     * @return
     */
    @RequestMapping("/getUsername")
    public Result getUsername() {
        String username = null;
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            username = user.getUsername();
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.GET_USERNAME_FAIL);
        }
        return HandleResult.getResult(true, MessageConstant.GET_USERNAME_SUCCESS, username);
    }

    /**
     * 查询所有的角色数据返回给新增用户时显示
     *
     * @return Result
     */
    @RequestMapping(value = "/findAllRole", method = RequestMethod.GET)
    public Result findAllRole() {
        try {
            List<Role> roleList = userService.findAllRole();
            return HandleResult.getResult(true, MessageConstant.GET_ROLE_LIST_SUCCESS, roleList);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.GET_ROLE_LIST_FAIL);
        }
    }

    /**
     * 新增用户信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody com.itheima.pojo.User user, int[] roleIds) {
        try {
            userService.addUser(user, roleIds);
            return HandleResult.getResult(true, MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.ADD_USER_FAIL);
        }
    }

    /**
     * 分页查询,填充页面
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public Result queryPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = userService.queryPage(queryPageBean);
            return HandleResult.getResult(true, MessageConstant.QUERY_USER_SUCCESS, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.QUERY_USER_FAIL);
        }
    }

    /**
     * 编辑时回显当前用户信息和已关联的角色信息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/findUserAndSelectedRolesById", method = RequestMethod.GET)
    public Result findById(int userId) {
        try {
            Map<String, Object> map = userService.findById(userId);
            return HandleResult.getResult(true, MessageConstant.QUERY_USER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.QUERY_USER_FAIL);
        }
    }

    /**
     * 修改用户和中间表数据
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Result modify(@RequestBody com.itheima.pojo.User user, int[] roleIds) {
        try {
            userService.modify(user, roleIds);
            return HandleResult.getResult(true, MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.EDIT_USER_FAIL);
        }
    }

    /**
     * 删除用户和中间表数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Result delete(int id) {
        try {
            userService.delete(id);
            return HandleResult.getResult(true, MessageConstant.DELETE_USERE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.DELETE_USER_FAIL);
        }
    }

    /**
     * 用户登录后修改密码
     *
     * @param pass,checkPass,originalPass
     * @return
     */
    @RequestMapping(value = "/passwordModify", method = RequestMethod.POST)
    public Result passwordModify(String pass, String originalPass, String validateCode) {
        try {
            //获取redis中的验证码
            String redisValidateCode = jedisPool.getResource().get(originalPass + RedisMessageConstant.SENDTYPE_GETPWD);
            //判断用户输入的验证码和手机号是否为空
            if (validateCode == null || validateCode.equals("") || originalPass == null || originalPass.equals("")) {
                return com.itheima.utils.HandleResult.getResult(false, MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
            }

            //判断redis中的验证码是否已销毁清空
            if ((validateCode != null && !validateCode.equals("")) && (redisValidateCode == null || redisValidateCode == "")) {
                return HandleResult.getResult(false, MessageConstant.VALIDATECODE_OUTDATE);
            }
            //判断用户输入的验证码和redis中的验证码是否一致
            if (!validateCode.equals(redisValidateCode)) {
                return HandleResult.getResult(false, MessageConstant.VALIDATECODE_ERROR);
            }
            Map<String, String> map = new HashMap<String, String>();
            //获得用户名
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = user.getUsername();
            map.put("username", username);
            map.put("originalPass", originalPass);
            map.put("pass", encoder.encode(pass));
            //传用户名和Map给service,如果密码正确就直接存修改后的密码到数据库
            Boolean flag = userService.checkPassword(map);
            //判断用户输入密码是否正确
            if (flag) {
                //正确,返回true
                return HandleResult.getResult(true, MessageConstant.MODIFY_PASSWORD_SUCCESS);
            } else {
                //错误.返回false提示
                return HandleResult.getResult(false, MessageConstant.INPUT_PASSWORD_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.MODIFY_PASSWORD_FAIL);
        }
    }
}
