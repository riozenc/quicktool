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

import com.riozenc.quicktool.shiro.Principal;
import com.riozenc.quicktool.shiro.token.UsernamePasswordToken;

public abstract class AbstractPasswordShiroRealm extends AuthorizingRealm {

	private String hashAlgorithmName = "SHA-512";
	private int hashIterations = 1024;

	public abstract String getPassword();

	/**
	 * 返回null则使用默认值
	 * 
	 * @return
	 */
	protected abstract String getHashAlgorithmName();

	/**
	 * 返回0则使用默认值
	 * 
	 * @return
	 */
	public abstract int getHashIterations();

	private SimpleAuthenticationInfo createAuthenticationInfo(String password) {
		try {
			byte[] salt = Hex.decodeHex(password.substring(0, 16).toCharArray());
			return new SimpleAuthenticationInfo(new Principal(), password.substring(16), ByteSource.Util.bytes(salt),
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
		String username = usernamePasswordToken.getUsername();
		if (username != null && !"".equals(username.trim())) {
			String password = getPassword();
			if (password != null) {
				return createAuthenticationInfo(password);
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
	}
}
