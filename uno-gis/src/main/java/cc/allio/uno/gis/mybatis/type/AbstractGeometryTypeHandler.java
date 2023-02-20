package cc.allio.uno.gis.mybatis.type;

import cc.allio.uno.gis.mybatis.MybatisProperties;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.postgis.PGgeometry;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractGeometryTypeHandler<T extends Geometry> extends BaseTypeHandler<T> {

	/**
	 * WKTReader非线程安全
	 */
	private static final ThreadLocal<WKTReader> READER_POOL = ThreadLocal.withInitial(WKTReader::new);

	/**
	 * WKTWriter非线程安全
	 */
	private static final ThreadLocal<WKTWriter> WRITER_POOL = ThreadLocal.withInitial(WKTWriter::new);

	private final MybatisProperties mybatisProperties;

	protected AbstractGeometryTypeHandler(MybatisProperties mybatisProperties) {
		this.mybatisProperties = mybatisProperties;
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
		PGgeometry pGgeometry = new PGgeometry(WRITER_POOL.get().write(parameter));
		org.postgis.Geometry geometry = pGgeometry.getGeometry();
		geometry.setSrid(mybatisProperties.getDbSRID());
		ps.setObject(i, pGgeometry);
	}

	@Override
	public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String string = rs.getString(columnName);
		return getResult(string);
	}

	@Override
	public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String string = rs.getString(columnIndex);
		return getResult(string);
	}

	@Override
	public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String string = cs.getString(columnIndex);
		return getResult(string);
	}

	private T getResult(String string) throws SQLException {
		if (string == null) {
			return null;
		}
		PGgeometry pGgeometry = new PGgeometry(string);
		String s = pGgeometry.toString();
		String target = String.format("SRID=%s;", mybatisProperties.getDbSRID());
		String wkt = s.replace(target, "");
		try {
			return (T) READER_POOL.get().read(wkt);
		} catch (Exception e) {
			throw new RuntimeException("解析wkt失败：" + wkt, e);
		}
	}
}
