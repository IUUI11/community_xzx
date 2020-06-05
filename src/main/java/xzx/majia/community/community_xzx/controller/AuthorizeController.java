package xzx.majia.community.community_xzx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xzx.majia.community.community_xzx.dto.AccessTokenDto;
import xzx.majia.community.community_xzx.dto.GithubUser;
import xzx.majia.community.community_xzx.mapper.UserMapper;
import xzx.majia.community.community_xzx.model.User;
import xzx.majia.community.community_xzx.model.UserExample;
import xzx.majia.community.community_xzx.provider.GithubProvider;
import xzx.majia.community.community_xzx.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(redirectUri);
        accessTokenDto.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser !=null && String.valueOf(githubUser.getId()) != null){
            String token = UUID.randomUUID().toString();
            User user =new User();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token",token));//正常来说 这边加到cookie里面去 然后再从cookie里面拿出来 就行
            request.getSession().setAttribute("user",githubUser);//但是我的不行，所以还是暂时在session里面
            System.out.println(user.getName());
            return "redirect:/";
        }else {
            System.out.println("没有拿到值");
            return "redirect:/";
        }


/*
        if (githubUser != null ) {
            if (userMapper.findByAccountId(String.valueOf(githubUser.getId()))==null){
                User user = new User();
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                user.setName(githubUser.getName());
                user.setAccountId(String.valueOf(githubUser.getId()));
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());
                user.setAvatarUrl(githubUser.getAvatar_url());
                userMapper.insert(user);
                response.addCookie(new Cookie("token",token));//正常来说 这边加到cookie里面去 然后再从cookie里面拿出来 就行
                request.getSession().setAttribute("user",githubUser);//但是我的不行，所以还是暂时在session里面
                //登陆成功 写session和cookie
                //登陆成功 写session和cookie
                System.out.println(user.getName());
                return "redirect:/";
            }else {
                User user =userMapper.findByAccountId(String.valueOf(githubUser.getId()));
                request.getSession().setAttribute("user",user);
                System.out.println(githubUser.getName());
                return "redirect:/";
            }
            */
/*User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));//正常来说 这边加到cookie里面去 然后再从cookie里面拿出来 就行
            request.getSession().setAttribute("user",githubUser);//但是我的不行，所以还是暂时在session里面
            //登陆成功 写session和cookie
            //登陆成功 写session和cookie
            System.out.println(user.getName());
            return "redirect:/";*//*

        } else {
            //登陆失败 重新登陆
            System.out.println("没有拿到值");
            return "redirect:/";
        }
*/


    }

    @GetMapping("/login")
    public String login(@RequestParam("name") String name,
                         HttpServletRequest request)
                        {
                            UserExample userExample = new UserExample();
                            userExample.createCriteria()
                                    .andNameEqualTo(name);
                            List<User> users = userMapper.selectByExample(userExample);
                            request.getSession().setAttribute("user",users.get(0));
        return "redirect:/";
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response
                         ){
        request.getSession().removeAttribute("user");
        Cookie cookie =new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
