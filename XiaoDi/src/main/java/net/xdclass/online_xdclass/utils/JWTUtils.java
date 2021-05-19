package net.xdclass.online_xdclass.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.xdclass.online_xdclass.model.entity.User;

import java.util.Date;

/**
 * JWT工具类
 * 注意点：
 * 1、生成的token，是可以通过base64进行解密出明文信息的
 * 2、base64进行解密出明文信息，修改再进行编码，则会解密失败
 * 3、无法作废已颁布的token，除非改密钥
 */
public class JWTUtils {

    /**
     * 过期时间：一周
     */
    private static final long EXPIRE = 60000 * 60 * 24 * 7;

    /**
     * 加密密钥
     */
    private static final String SECRET = "xdclass.net168";

    /**
     * 令牌前缀
     */
    private static final String TOKEN_PREFIX = "xdclass";

    /**
     * subject
     */
    private static final String SUBJECT = "xdclass";

    /**
     * 封装生成token方法 【根据用户信息，生成令牌】
     * @param user
     * @return
     */
    public static String geneJsonWebToken(User user){
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("head_img",user.getHeadImg())
                .claim("id",user.getId())
                .claim("name",user.getName())
                .setIssuedAt(new Date())    //令牌下发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))   //令牌过期时间
                .signWith(SignatureAlgorithm.HS256,SECRET).compact();   //设置签名加密方式
        token = TOKEN_PREFIX + token;
        return token;
    }

    /**
     * 校验token的方法
     * @param token
     * @return
     */
    public static Claims checkJWT(String token){
        try {
            final Claims claims = Jwts.parser().setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX,"")).getBody();
            return claims;
        } catch (Exception e){
            return null;
        }
    }
}
