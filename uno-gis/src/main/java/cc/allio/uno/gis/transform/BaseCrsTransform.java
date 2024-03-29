package cc.allio.uno.gis.transform;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.gis.SRID;
import lombok.Getter;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.CRS;

@Getter
public abstract class BaseCrsTransform implements CrsTransform {

    private final SRID fromSRID;
    private final SRID toSRID;
    private final CoordinateReferenceSystem fromCrs;
    private final CoordinateReferenceSystem toCrs;

    protected BaseCrsTransform(SRID fromSRID, SRID toSRID) throws FactoryException {
        this.fromSRID = fromSRID;
        this.toSRID = toSRID;
        this.fromCrs = CRS.decode(EPSG + StringPool.COLON + fromSRID.getCode());
        this.toCrs = CRS.decode(EPSG + StringPool.COLON + toSRID.getCode());
    }

}


