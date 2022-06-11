package com.ldl.petsystemspringboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ldl.petsystemspringboot.entity.Comment;
import com.ldl.petsystemspringboot.entity.Invitation;
import com.ldl.petsystemspringboot.entity.Likeorcollect;
import com.ldl.petsystemspringboot.entity.User;
import com.ldl.petsystemspringboot.mapper.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserHandler {

    public String saveimgpath = "D:/images/";
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private InvitationMapper invitationMapper;
    @Autowired
    private LikeorcollectMapper likeorcollectMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ImageMapper imageMapper;

    @PostMapping("/login")
    public User login(@RequestBody User user){
        System.out.println(user);
        List<User> userList = userMapper.selectList(null);
        for (int i = 0; i < userList.size(); i++) {
            if(userList.get(i).getUseremail().equals(user.getUseremail())&&userList.get(i).getUserpassword().equals(user.getUserpassword())){
                return userList.get(i);
            }
        }
        return null;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        if(user==null){
            return null;
        }
        System.out.println(user);
        int flag = userMapper.insert(user);
        if(flag==1){
            return user;
        }else {
            return null;
        }
    }

    //分页查询
    @GetMapping("/findAll")
    public IPage<?> findAll(@RequestParam(value = "currentPage") Integer currentPage,
                            @RequestParam(value = "pageSize") Integer pageSize,
                            @RequestParam(value = "search", required = false) String search,
                            @RequestParam(value = "userid", required = false) Long userid,
                            @RequestParam(value = "userrole", required = false) String userrole
    ) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!search.equals("")) {
            queryWrapper.lambda().like(User::getUsername, search);
        }
        if (!userrole.equals("")) {
            if (!userrole.equals("全部类型")) {
                queryWrapper.lambda().eq(User::getUserrole, userrole);
            }
        }
        return userMapper.selectPage(new Page<>(currentPage, pageSize), queryWrapper);
    }



    @GetMapping("/findByUserid/{userid}")
    public User findByUserid(@PathVariable("userid")Long userid){
        return userMapper.selectById(userid);
    }

    @GetMapping("/deletebyid/{userid}")
    public User deletebyid(@PathVariable("userid")Long userid){
        User user = userMapper.selectById(userid);
        userMapper.deleteById(userid);
        invitationMapper.delete(new QueryWrapper<Invitation>().lambda().eq(Invitation::getUserid,user.getUserid()));
        likeorcollectMapper.delete(new QueryWrapper<Likeorcollect>().lambda().eq(Likeorcollect::getUserid,user.getUserid()));
        commentMapper.delete(new QueryWrapper<Comment>().lambda().eq(Comment::getUserid,user.getUserid()));
        return user;
    }

    @PostMapping("/save")
    public User save(@RequestParam(value = "user",required = false) String user,
                     @RequestParam(value = "userimage",required = false) MultipartFile file){
        String savedpath = "";
        String suffix = "";
        String Path = saveimgpath;
        UUID uuid = UUID.randomUUID();
        //存图片
        if(file!=null){
            suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
            savedpath = Path+uuid.toString()+"."+suffix;//生成随机名字 suffix后缀
            try {
                //将文件保存指定目录
                file.transferTo(new File(savedpath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //存用户
        if(user!=null){
            User user1 = (User) JSONObject.toBean(JSONObject.fromObject(user),User.class);
            if(file!=null){
                if(user1.getUserid()!=null){
                    User temp = userMapper.selectById(user1.getUserid());
                    File file1 = new File(Path+temp.getUserimage());
                    if(file1.exists()&&file1.isFile()){
                        //如果用户提交了新的 那么删掉之前的
                        System.out.println(file1.delete());;
                    }
                }
            }
            //如果路径不为空  设置用户头像文件名
            if(!savedpath.equals("")){
                user1.setUserimage(uuid.toString()+"."+suffix);
            }
             userMapper.updateById(user1);
            return user1;
        }
        return null;
    }

}
