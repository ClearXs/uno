package cc.allio.uno.gis.jackson;

import cc.allio.uno.gis.jackson.parsers.*;
import cc.allio.uno.gis.jackson.serialization.GeometryDeserializer;
import cc.allio.uno.gis.jackson.serialization.GeometrySerializer;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.locationtech.jts.geom.*;

import static cc.allio.uno.gis.GeometryTypes.*;

public class JtsModule extends SimpleModule {

    public JtsModule() {
        this(new GeometryFactory());
    }

    public JtsModule(GeometryFactory geometryFactory) {
        super("JtsModule", new Version(1, 0, 0, null, "cc.allio.uno.gis", "uno-gis"));

        addSerializer(GEOMETRY, new GeometrySerializer(null));
        GenericGeometryParser genericGeometryParser = new GenericGeometryParser(geometryFactory);
        addDeserializer(GEOMETRY, new GeometryDeserializer<>(genericGeometryParser));
        addDeserializer(POINT, new GeometryDeserializer<>(new PointParser(geometryFactory)));
        addDeserializer(MULTI_POINT, new GeometryDeserializer<>(new MultiPointParser(geometryFactory)));
        addDeserializer(LINE_STRING, new GeometryDeserializer<>(new LineStringParser(geometryFactory)));
        addDeserializer(MULTI_LINE_STRING, new GeometryDeserializer<>(new MultiLineStringParser(geometryFactory)));
        addDeserializer(POLYGON, new GeometryDeserializer<>(new PolygonParser(geometryFactory)));
        addDeserializer(MULTI_POLYGON, new GeometryDeserializer<>(new MultiPolygonParser(geometryFactory)));
        addDeserializer(GEOMETRY_COLLECTION, new GeometryDeserializer<>(new GeometryCollectionParser(geometryFactory, genericGeometryParser)));
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
    }
}
