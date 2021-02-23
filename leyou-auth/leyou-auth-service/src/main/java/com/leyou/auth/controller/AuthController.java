package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

//    @Autowired
//    private AuthService authService;
//
//    @Autowired
//    private JwtProperties prop;
//
//    /**
//     * 登录授权
//     *
//     * @param username
//     * @param password
//     * @return
//     */
//    @PostMapping("accredit")
//    public ResponseEntity<Void> authentication(
//            @RequestParam("username") String username,
//            @RequestParam("password") String password,
//            HttpServletRequest request,
//            HttpServletResponse response) {
//        // 登录校验
//        String token = this.authService.authentication(username, password);
//        if (StringUtils.isBlank(token)) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//        // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
//        CookieUtils.setCookie(request, response, prop.getCookieName(),
//                token, prop.getCookieMaxAge(), null, true);
//        return ResponseEntity.ok().build();
//    }


    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(@RequestParam("username")String username, @RequestParam("password")String password,
                                         HttpServletRequest request, HttpServletResponse response){
        String token=this.authService.accredit(username,password);

        if(StringUtils.isBlank(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CookieUtils.setCookie(request,response,this.jwtProperties.getCookieName(),token,this.jwtProperties.getExpire());

        return ResponseEntity.ok(null);
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN")String token,HttpServletRequest request, HttpServletResponse response){
        try {
        //通过jwt工具类使用公钥解析jwt
        UserInfo user = JwtUtils.getInfoFromToken(token,this.jwtProperties.getPublicKey());

        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //刷新jwt中有效时间
        token = JwtUtils.generateToken(user, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());


         //刷新cookie中的有效时间
        CookieUtils.setCookie(request,response,this.jwtProperties.getCookieName(),token,this.jwtProperties.getExpire() * 60);

        return ResponseEntity.ok(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
