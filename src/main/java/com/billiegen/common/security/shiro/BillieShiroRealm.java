package com.billiegen.common.security.shiro;

import com.billiegen.system.action.CaptchaAction;
import com.billiegen.system.dao.AdminDao;
import com.billiegen.system.entity.Admin;
import com.billiegen.system.entity.Role;
import com.billiegen.utils.security.EncodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author CodePorter
 * @date 2017-10-19
 */
public class BillieShiroRealm extends AuthorizingRealm {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private AdminDao adminDao;

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        logger.info("{} is trying to authentication", token.getUsername());

        // 验证码校验
//        String captchaRight = (String) SecurityUtils.getSubject().getSession(true)
//                .getAttribute(CaptchaAction.SESSION_ATTR_CAPTCHA);
//        String captchaInput = token.getCaptcha();
//        if (StringUtils.isEmpty(captchaInput) || !StringUtils.equalsIgnoreCase(captchaInput, captchaRight)) {
//            throw new AuthenticationException("验证码错误.");
//        }
        // 用户名密码校验
        Admin user = adminDao.findAdminByUsernameEquals(token.getUsername());
        if (user != null) {
            if (!user.getEnabled()) {
                throw new AuthenticationException("账户未启用.");
            }
            if (user.getLocked()) {
                throw new AuthenticationException("账号被锁定.");
            }
            byte[] salt = EncodeUtil.decodeHex(user.getPassword().substring(0, 16));
            return new SimpleAuthenticationInfo(
                    new Principal(user),
                    user.getPassword().substring(16),
                    ByteSource.Util.bytes(salt), getName());
        }
        return null;
    }

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Principal principal = (Principal) getAvailablePrincipal(principalCollection);
        Admin user = adminDao.findAdminByUsernameEquals(principal.getUsername());
        if (user != null) {
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            user.getRoleSet().forEach(role -> addRbacAuthorization(authorizationInfo, role));
            // update last login date
            user.setLoginDate(new Date());
            adminDao.save(user);
            // TODO: 2017/10/20 0020 record login log
            return authorizationInfo;
        }
        return null;
    }

    private void addRbacAuthorization(SimpleAuthorizationInfo authorization, Role role) {
        authorization.addRole(role.getId());
        role.getRightSet().forEach(right -> authorization.addStringPermission(right.getRightLink()));
    }
}
