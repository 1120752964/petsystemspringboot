package com.ldl.petsystemspringboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ldl.petsystemspringboot.entity.*;
import com.ldl.petsystemspringboot.mapper.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/invitation")
public class InvitationHandler {

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

    @PostMapping("/add")
    public List<Invitation> register(@RequestBody Invitation invitation) {
        if (invitation == null) {
            return null;
        }
        int flag = invitationMapper.insert(invitation);
        List<Invitation> invitations = invitationMapper.selectList(null);
        if (flag == 1) {
            return invitations;
        } else {
            return null;
        }
    }

    @PostMapping("/addpetmovie")
    public Invitation addpetmovie(@RequestParam(value = "invitation", required = false) String invitation,
                                  @RequestParam(value = "invitationimage", required = false) MultipartFile file) {
        String savedpath = "";
        String suffix = "";
        String name = "";
        String Path = saveimgpath;
        UUID uuid = UUID.randomUUID();
        Invitation invitation1 = (Invitation) JSONObject.toBean(JSONObject.fromObject(invitation), Invitation.class);
        //存帖子
        if (invitation != null) {
            //这是新建帖子  此时没有id
            if (invitation1.getInvitationid() == 0) {
                //获取保存的id  获取不来。。。只能通过invitation1.getInvitationid()获取
                int id = invitationMapper.insert(invitation1);
                // 然后去存image 如果有的话
                if (file != null) {
                    suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
                    name = uuid.toString() + "." + suffix;
                    savedpath = Path + name;//生成随机名字 suffix后缀
                    try {
                        //将文件保存指定目录
                        file.transferTo(new File(savedpath));
                        Image image = new Image();
                        image.setInvititionid(invitation1.getInvitationid());
                        image.setUrl(name);
                        imageMapper.insert(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    invitation1.setContext(name);
                    invitationMapper.updateById(invitation1);
                }
            } else {
                //这是帖子有id  所以是修改
                invitationMapper.updateById(invitation1);
                // 然后去存image 如果有的话
                if (file != null) {
                    suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
                    name = uuid.toString() + "." + suffix;
                    savedpath = Path + name;//生成随机名字 suffix后缀
                    try {
                        //先删掉之前的image
                        imageMapper.delete(new QueryWrapper<Image>().lambda().eq(Image::getInvititionid, invitation1.getInvitationid()));
                        //将文件保存指定目录
                        file.transferTo(new File(savedpath));
                        Image image = new Image();
                        image.setInvititionid(Long.parseLong(invitation1.getInvitationid() + ""));
                        image.setUrl(name);
                        imageMapper.insert(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    invitation1.setContext(name);
                    invitationMapper.updateById(invitation1);
                }
            }
        }
        return invitation1;
    }

    //判断是否点赞或者收藏了  返回所有该用户以及该帖子id的集合
    @GetMapping("/judgeiflikeorcollect/{invitationid}/{userid}")
    public List<Likeorcollect> judgeiflikeorcollect(@PathVariable("invitationid") Long invitationid, @PathVariable("userid") Long userid) {
        QueryWrapper<Likeorcollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", userid).eq("invitationid", invitationid);
        List<Likeorcollect> likeorcollects = likeorcollectMapper.selectList(queryWrapper);
        return likeorcollects;
    }

    @GetMapping("/addcomment/{invitationid}/{userid}")
    public Integer addcomment(@PathVariable("invitationid") Long invitationid, @PathVariable("userid") Long userid, @RequestParam("content") String content) {
        Comment comment = new Comment();
        comment.setUserid(userid);
        comment.setContext(content);
        comment.setUsername(userMapper.selectById(userid).getUsername());
        comment.setInvitationid(invitationid);
        int a = commentMapper.insert(comment);
        return a;
    }

    @GetMapping("/getcomments/{invitationid}")
    public List<Comment> getcomments(@PathVariable("invitationid") Long invitationid) {
        return commentMapper.selectList(new QueryWrapper<Comment>().eq("invitationid", invitationid));
    }

    //修改petmovie帖子   包含了点赞和收藏内容
    @PostMapping("/changepetmovie/{like}/{collect}/{userid}")
    public List<Invitation> changepetmovie(@PathVariable("like") String like,
                                   @PathVariable("collect") String collect, @PathVariable("userid") Long userid,
                                   @RequestParam(value = "invitation", required = false) String invitation1,
                                   @RequestParam(value = "invitationimage", required = false) MultipartFile file) {
        if (invitation1 == null) {
            return null;
        }
        Invitation invitation = (Invitation) JSONObject.toBean(JSONObject.fromObject(invitation1), Invitation.class);
        //先判断该用户有没有给这个帖子点过赞
        List<Likeorcollect> likeorcollects1 = likeorcollectMapper.selectList(new QueryWrapper<Likeorcollect>().eq("invitationid", invitation.getInvitationid()).eq("userid", userid).eq("likeorcollect", "0"));
        Boolean likeforthis = likeorcollects1.size() != 0;
        //先判断该用户有没有给这个帖子点过赞
        List<Likeorcollect> likeorcollects2 = likeorcollectMapper.selectList(new QueryWrapper<Likeorcollect>().eq("invitationid", invitation.getInvitationid()).eq("userid", userid).eq("likeorcollect", "1"));
        Boolean collectforthis = likeorcollects2.size() != 0;
        //用户想点
        if (like.equals("1")) {
            //而且之前点过  这种情况下 不用动
            if (likeforthis) {
            } else {//之前没点过  新增记录 并且点赞数+1
                Likeorcollect likeorcollect = new Likeorcollect();
                likeorcollect.setUserid(userid);
                likeorcollect.setInvitationid(invitation.getInvitationid());
                likeorcollect.setLikeorcollect("0");
                likeorcollectMapper.insert(likeorcollect);
                invitation.setLiketnumber(invitation.getLiketnumber() + 1);
            }
        } else {//用户不想点
            if (likeforthis) {//之前点过，现在不想点了 删除记录 点赞数-1
                for (Likeorcollect likeorcollect : likeorcollects1) {
                    likeorcollectMapper.deleteById(likeorcollect.getLikeorcollectid());
                }
                invitation.setLiketnumber(invitation.getLiketnumber() - 1);
            } else { //之前没点过 现在也不想点  无事发生

            }
        }
        if (collect.equals("1")) {
            //而且之前点过  这种情况下 不用动
            if (collectforthis) {
            } else {//之前没点过  新增记录 并且点赞数+1
                Likeorcollect likeorcollect = new Likeorcollect();
                likeorcollect.setUserid(userid);
                likeorcollect.setInvitationid(invitation.getInvitationid());
                likeorcollect.setLikeorcollect("1");
                likeorcollectMapper.insert(likeorcollect);
                invitation.setCollectnumber(invitation.getCollectnumber() + 1);
            }
        } else {//用户不想点
            if (collectforthis) {//之前点过，现在不想点了 删除记录 点赞数-1
                for (Likeorcollect likeorcollect : likeorcollects2) {
                    likeorcollectMapper.deleteById(likeorcollect.getLikeorcollectid());
                }
                invitation.setCollectnumber(invitation.getCollectnumber() - 1);
            } else { //之前没点过 现在也不想点  无事发生

            }
        }
        System.out.println(invitation);
        //控制图片和短片的更新逻辑
        //这种需要先存图片，然后更新invitation里的路径 最后保存帖子
        //查看的时候  默认应该是上传为空的  所以不能无脑删除  得有文件的时候才删除
        String savedpath = "";
        String suffix = "";
        String name = "";
        String Path = saveimgpath;
        UUID uuid = UUID.randomUUID();
        if (file == null) {
        } else {
            //有文件的时候去更新
            imageMapper.delete(new QueryWrapper<Image>().lambda().eq(Image::getInvititionid, invitation.getInvitationid()));
            suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
            name = uuid.toString() + "." + suffix;
            savedpath = Path + name;//生成随机名字 suffix后缀
            try {
                //将文件保存指定目录
                file.transferTo(new File(savedpath));
                Image image = new Image();
                image.setInvititionid(invitation.getInvitationid());
                image.setUrl(name);
                imageMapper.insert(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
            invitation.setContext(name);
        }
        int flag = invitationMapper.updateById(invitation);
        List<Invitation> invitations = invitationMapper.selectList(null);
        if (flag == 1) {
            return invitations;
        } else {
            return null;
        }
    }
    //修改帖子   包含了点赞和收藏内容
    @PostMapping("/change/{like}/{collect}/{userid}")
    public List<Invitation> change(@PathVariable("like") String like,
                                           @PathVariable("collect") String collect, @PathVariable("userid") Long userid,
                                           @RequestBody Invitation invitation) {
        if (invitation == null) {
            return null;
        }
        //先判断该用户有没有给这个帖子点过赞
        List<Likeorcollect> likeorcollects1 = likeorcollectMapper.selectList(new QueryWrapper<Likeorcollect>().eq("invitationid", invitation.getInvitationid()).eq("userid", userid).eq("likeorcollect", "0"));
        Boolean likeforthis = likeorcollects1.size() != 0;
        //先判断该用户有没有给这个帖子收藏
        List<Likeorcollect> likeorcollects2 = likeorcollectMapper.selectList(new QueryWrapper<Likeorcollect>().eq("invitationid", invitation.getInvitationid()).eq("userid", userid).eq("likeorcollect", "1"));
        Boolean collectforthis = likeorcollects2.size() != 0;
        //用户想点
        if (like.equals("1")) {
            //而且之前点过  这种情况下 不用动
            if (likeforthis) {
            } else {//之前没点过  新增记录 并且点赞数+1
                Likeorcollect likeorcollect = new Likeorcollect();
                likeorcollect.setUserid(userid);
                likeorcollect.setInvitationid(invitation.getInvitationid());
                likeorcollect.setLikeorcollect("0");
                likeorcollectMapper.insert(likeorcollect);
                invitation.setLiketnumber(invitation.getLiketnumber() + 1);
            }
        } else {//用户不想点
            if (likeforthis) {//之前点过，现在不想点了 删除记录 点赞数-1
                for (Likeorcollect likeorcollect : likeorcollects1) {
                    likeorcollectMapper.deleteById(likeorcollect.getLikeorcollectid());
                }
                invitation.setLiketnumber(invitation.getLiketnumber() - 1);
            } else { //之前没点过 现在也不想点  无事发生
            }
        }
        //用户想点
        if (collect.equals("1")) {
            //而且之前点过  这种情况下 不用动
            if (collectforthis) {
            } else {//之前没点过  新增记录 并且收藏数+1
                Likeorcollect likeorcollect = new Likeorcollect();
                likeorcollect.setUserid(userid);
                likeorcollect.setInvitationid(invitation.getInvitationid());
                likeorcollect.setLikeorcollect("1");
                likeorcollectMapper.insert(likeorcollect);
                invitation.setCollectnumber(invitation.getCollectnumber() + 1);
            }
        } else {//用户不想点
            if (collectforthis) {//之前点过，现在不想点了 删除记录 并且收藏数-1
                for (Likeorcollect likeorcollect : likeorcollects2) {
                    likeorcollectMapper.deleteById(likeorcollect.getLikeorcollectid());
                }
                invitation.setCollectnumber(invitation.getCollectnumber() - 1);
            } else { //之前没点过 现在也不想点  无事发生
            }
        }
        int flag = invitationMapper.updateById(invitation);
        List<Invitation> invitations = invitationMapper.selectList(null);
        if (flag == 1) {
            return invitations;
        } else {
            return null;
        }
    }

    //分页查询
    @GetMapping("/findAll")
    public IPage<?> findAll(@RequestParam(value = "currentPage") Integer currentPage,
                                     @RequestParam(value = "pageSize") Integer pageSize,
                                     @RequestParam(value = "search", required = false) String search,
                                     @RequestParam(value = "pettype", required = false) String pettype,
                                     @RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "userid", required = false) Long userid,
                                     @RequestParam(value = "likeorcollect", required = false) String likeorcollect,
                                        @RequestParam(value = "isformyinvitation", required = false) String isformyinvitation
    ) {
        QueryWrapper<Invitation> queryWrapper = new QueryWrapper<>();
        if (!pettype.equals("")) {
            if (!pettype.equals("全部类型")) {
                queryWrapper.lambda().eq(Invitation::getPettype, pettype);
            }
        }
        if (!search.equals("")) {
            queryWrapper.lambda().like(Invitation::getTitle, search);
        }
        if (!isformyinvitation.equals("")) {
            queryWrapper.lambda().like(Invitation::getUserid, userid);
        }
        if (!type.equals("")) {
            queryWrapper.lambda().eq(Invitation::getType, type);
        }
        if (!likeorcollect.equals("")) {
            QueryWrapper<Likeorcollect> likeorcollectQueryWrapper = new QueryWrapper<>();
            if(!(userid==0)){
                likeorcollectQueryWrapper.lambda().eq(Likeorcollect::getUserid, userid);
            }
            return likeorcollectMapper.selectPage(new Page<>(currentPage, pageSize), likeorcollectQueryWrapper);
        }
        return invitationMapper.selectPage(new Page<>(currentPage, pageSize), queryWrapper);
    }

    //根据主键查询
    @GetMapping("/findbyid/{id}")
    public Invitation findbyid(@PathVariable(value = "id") Long id) {
        return invitationMapper.selectById(id);
    }

    //根据主键删除
    @GetMapping("/deletebyid/{id}")
    public Integer deletebyid(@PathVariable(value = "id") Long id) {
        //这里需要删除帖子相关的评论和点赞以及收藏信息
        likeorcollectMapper.delete(new QueryWrapper<Likeorcollect>().lambda().eq(Likeorcollect::getInvitationid, id));
        commentMapper.delete(new QueryWrapper<Comment>().lambda().eq(Comment::getInvitationid, id));
        return invitationMapper.deleteById(id);
    }

    //根据主键删除comment
    @GetMapping("/deletecommentbyid/{id}")
    public Integer deletecommentbyid(@PathVariable(value = "id") Long id) {
        return commentMapper.deleteById(id);
    }

    @PostMapping("/save")
    public User save(@RequestParam(value = "user", required = false) String user,
                     @RequestParam(value = "userimage", required = false) MultipartFile file) {
        String savedpath = "";
        String suffix = "";
        String Path = saveimgpath;
        UUID uuid = UUID.randomUUID();
        //存图片
        if (file != null) {
            suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
            savedpath = Path + uuid.toString() + "." + suffix;//生成随机名字 suffix后缀
            try {
                //将文件保存指定目录
                file.transferTo(new File(savedpath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //存用户
        if (user != null) {
            User user1 = (User) JSONObject.toBean(JSONObject.fromObject(user), User.class);
            if (file != null) {
                if (user1.getUserid() != null) {
                    User temp = userMapper.selectById(user1.getUserid());
                    File file1 = new File(Path + temp.getUserimage());
                    if (file1.exists() && file1.isFile()) {
                        //如果用户提交了新的 那么删掉之前的
                    }
                }
            }
            //如果路径不为空  设置用户头像文件名
            if (!savedpath.equals("")) {
                user1.setUserimage(uuid.toString() + "." + suffix);
            }
            if (user1.getUserimage().contains("/")) {
                user1.setUserimage(user1.getUserimage().substring(user1.getUserimage().lastIndexOf("/"), user1.getUserimage().length()));
            }
            userMapper.updateById(user1);
            return user1;
        }
        return null;
    }

}
