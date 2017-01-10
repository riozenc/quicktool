/**
 * Copyright &copy; 2012-2014  All rights reserved.
 */
package com.riozenc.quicktool.mybatis.persistence.interceptor;

import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.riozenc.quicktool.common.util.StringUtils;
import com.riozenc.quicktool.common.util.reflect.ReflectUtil;
import com.riozenc.quicktool.mybatis.persistence.Page;

/**
 * 数据库分页插件，只拦截查询语句.
 * 
 * @author poplar.yfyang / thinkgem
 * @version 2013-8-28
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
		RowBounds.class, ResultHandler.class }) })
public class PaginationInterceptor extends BaseInterceptor {

	private static final long serialVersionUID = 1L;
	private static Class[] params = new Class[] {};

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter = invocation.getArgs()[1];

		String name = mappedStatement.getId();

		String className = name.substring(0, name.lastIndexOf("."));
		String methodName = name.substring(name.lastIndexOf(".") + 1);

		Class<?> clazz = Class.forName(className);

		Method method = null;
		if (parameter == null) {

			method = clazz.getDeclaredMethod(methodName, params);
		} else {
			method = clazz.getDeclaredMethod(methodName, parameter.getClass());
		}

		if (mappedStatement.getId().lastIndexOf("ByWhere") > 0) {

			BoundSql boundSql = mappedStatement.getBoundSql(parameter);

			Object parameterObject = boundSql.getParameterObject();

			// 获取分页参数对象
			Page<Object> page = null;
			if (parameterObject != null) {
				page = convertParameter(parameterObject, page);
			}

			// 如果设置了分页对象，则进行分页
			if (page != null && page.getPageSize() != -1) {

				if (StringUtils.isBlank(boundSql.getSql())) {
					return null;
				}
				String originalSql = boundSql.getSql().trim();

				// 得到总记录数
				page.setTotalRow(
						SQLHelper.getCount(originalSql, null, mappedStatement, parameterObject, boundSql, logger));
				// 获取版面总数，与findList方法绑定（后期可通过sql的名称优化）
				// if (mappedStatement.getId().indexOf("TbMtzlDao") > 0) {
				// MappedStatement mttpCountMS =
				// mappedStatement.getConfiguration()
				// .getMappedStatement("com.zhjt.mtgl.dao.mtzl.TbMtzlDao.getMtzlMttpCount");
				// BoundSql newBoundSql = mttpCountMS.getBoundSql(parameter);
				// page.setBmCount(
				// SQLHelper.getCount(newBoundSql.getSql(), null, mttpCountMS,
				// parameterObject, newBoundSql, logger));
				// page.setBmCountFlag(true);
				// }

				// 分页查询 本地化对象 修改数据库注意修改实现
				String pageSql = SQLHelper.generatePageSql(originalSql, page, DIALECT);
				// if (log.isDebugEnabled()) {
				// log.debug("PAGE SQL:" + StringUtils.replace(pageSql, "\n",
				// ""));
				// }

				// invocation.getArgs()[2] = new
				// RowBounds(RowBounds.NO_ROW_OFFSET,
				// RowBounds.NO_ROW_LIMIT);

				BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), pageSql,
						boundSql.getParameterMappings(), boundSql.getParameterObject());
				// 解决MyBatis 分页foreach 参数失效 start
				if (ReflectUtil.getFieldValue(boundSql, "metaParameters") != null) {
					MetaObject mo = (MetaObject) ReflectUtil.getFieldValue(boundSql, "metaParameters");
					ReflectUtil.setFieldValue(newBoundSql, "metaParameters", mo);
				}
				// 解决MyBatis 分页foreach 参数失效 end
				MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));

				invocation.getArgs()[0] = newMs;
			}
		}

		// }
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		super.initProperties(properties);
	}

	private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
		MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
				ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		if (ms.getKeyProperties() != null) {
			for (String keyProperty : ms.getKeyProperties()) {
				builder.keyProperty(keyProperty);
			}
		}
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		builder.resultMaps(ms.getResultMaps());
		builder.cache(ms.getCache());
		return builder.build();
	}

	public static class BoundSqlSqlSource implements SqlSource {
		BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}

}
