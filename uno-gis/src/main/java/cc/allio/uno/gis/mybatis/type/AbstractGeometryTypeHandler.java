package cc.allio.uno.gis.mybatis.type;

import cc.allio.uno.gis.SRID;
import cc.allio.uno.gis.config.UnoGisProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.postgis.PGgeometry;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@Slf4j
public abstract class AbstractGeometryTypeHandler<T extends Geometry> extends BaseTypeHandler<T> {

    /**
     * WKTReader非线程安全
     */
    private static final ThreadLocal<WKTReader> READER_POOL = ThreadLocal.withInitial(WKTReader::new);

    /**
     * WKTWriter非线程安全
     */
    private static final ThreadLocal<WKTWriter> WRITER_POOL = ThreadLocal.withInitial(WKTWriter::new);

    private final UnoGisProperties gisProperties;

    protected AbstractGeometryTypeHandler(UnoGisProperties gisProperties) {
        this.gisProperties = gisProperties;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        PGgeometry pGgeometry = new PGgeometry(WRITER_POOL.get().write(parameter));
        org.postgis.Geometry geometry = pGgeometry.getGeometry();
        geometry.setSrid(gisProperties.getDefaultSrid());
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
        String target = String.format("SRID=%s;", gisProperties.getDefaultSrid());
        String wkt = s.replace(target, "");
        try {
            return (T) READER_POOL.get().read(wkt);
        } catch (Exception e) {
            log.error("解析wkt失败：" + wkt, e);
            return getRealResult(s);
        }
    }

    private T getRealResult(String s){
        boolean right = false;
        T result = null;
        for (SRID srid : SRID.values()) {
            if (right) break;
            String wkt = s.replace(String.format("SRID=%s;", srid.getCode()), "");
            try {
                result = (T) READER_POOL.get().read(wkt);
                right = true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
