/**
 * Title:AbstractPasswordShiroRealm.java
 * Author:riozenc
 * Datetime:2017年3月23日 下午5:51:57
**/
package com.riozenc.quicktool.shiro.realm;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;
import com.riozenc.quicktool.shiro.token.UsernamePasswordToken;

public abstract class AbstractPasswordShiroRealm extends AuthorizingRealm {

	private String hashAlgorithmName = "SHA-512";
	private int hashIterations = 1024;

	/**
	 * 获取密码
	 * 
	 * @return
	 */
	public abstract String getPassword(String loginName);

	/**
	 * 获取加密算法返回null 则使用默认值
	 * 
	 * @return
	 */
	protected abstract String getHashAlgorithmName();

	/**
	 * 获取加密算法循环次数 返回0则使用默认值
	 * 
	 * @return
	 */
	public abstract int getHashIterations();

	/**
	 * 返回认证对象
	 * 
	 * @return
	 */
	public abstract Object createPrincipal(String loginName);

	private SimpleAuthenticationInfo createAuthenticationInfo(String loginName, String password) {
		try {
			byte[] salt = Hex.decodeHex(password.substring(0, 16).toCharArray());
			return new SimpleAuthenticationInfo(createPrincipal(loginName), password.substring(16), ByteSource.Util.bytes(salt),
					getName());
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			throw new AuthenticationException("密码错误...");
		}

	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// TODO Auto-generated method stub

		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		String loginName = usernamePasswordToken.getUsername();
		if (loginName != null && !"".equals(loginName.trim())) {
			String password = getPassword(loginName);
			if (password != null) {
				return createAuthenticationInfo(loginName, password);
			}
		}
		return null;
	}

	@PostConstruct
	public void initCredentialsMatcher() {
		if (getHashAlgorithmName() != null)
			hashAlgorithmName = getHashAlgorithmName();

		if (getHashIterations() != 0)
			hashIterations = getHashIterations();

		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(hashAlgorithmName);
		matcher.setHashIterations(hashIterations);// 迭代1024次
		setCredentialsMatcher(matcher);

		LogUtil.getLogger(LOG_TYPE.MAIN).info(
				"AbstractPasswordShiroRealm  initCredentialsMatcher(" + hashAlgorithmName + "," + hashIterations + ")");
	}
}
