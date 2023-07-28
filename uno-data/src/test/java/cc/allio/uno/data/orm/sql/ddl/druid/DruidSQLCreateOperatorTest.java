package cc.allio.uno.data.orm.sql.ddl.druid;

import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.data.orm.type.DataType;
import cc.allio.uno.data.orm.type.GenericSQLType;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.SQLColumnDef;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DruidSQLCreateOperatorTest extends BaseTestCase {

    @Test
    void testCreateTable() {
        DruidSQLCreateOperator createOperator = new DruidSQLCreateOperator(DBType.POSTGRESQL);
        String sql = createOperator.from("sxxx")
                .column(
                        SQLColumnDef.builder()
                                .sqlName(SQLName.of("name"))
                                .dataType(DataType.createCharType(GenericSQLType.CHAR, 9))
                                .isPk(true)
                                .isNonNull(true)
                                .isUnique(true)
                                .build())
                .getSQL();
        System.out.println(sql);
    }

    @Test
    void pgTable() {
        String cr_pg = "CREATE TABLE \"am_device_instance\"\n" +
                "(\n" +
                "    \"id\"                        int8 NOT NULL,\n" +
                "    \"code\"                      varchar(64),\n" +
                "    \"name\"                      varchar(64),\n" +
                "    \"type\"                      varchar(64),\n" +
                "    \"region_code\"               varchar(64),\n" +
                "    \"manufacture_date\"          date,\n" +
                "    \"manufacturer\"              varchar(64),\n" +
                "    \"address\"                   varchar(255),\n" +
                "    \"facility_code\"             varchar(64),\n" +
                "    \"installation_date\"         date,\n" +
                "    \"geometry\"                  varchar(512),\n" +
                "    \"data_access_configuration\" varchar(900),\n" +
                "    \"create_user\"               int8,\n" +
                "    \"create_time\"               date,\n" +
                "    \"create_dept\"               int8,\n" +
                "    \"update_user\"               int8,\n" +
                "    \"update_time\"               date,\n" +
                "    \"update_dept\"               int8,\n" +
                "    \"status\"                    int4,\n" +
                "    \"is_deleted\"                int4,\n" +
                "    \"tenant_id\"                 int8,\n" +
                "    \"images\"                    text,\n" +
                "    \"parent_code\"               varchar(64),\n" +
                "    CONSTRAINT \"am_device_instance_pkey\" PRIMARY KEY (\"id\")\n" +
                ")\n" +
                ";\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"id\" IS '主键';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"code\" IS '设备编码';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"name\" IS '设备名称';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"type\" IS '设备类型';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"region_code\" IS '行政区划code';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"manufacture_date\" IS '生产日期';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"manufacturer\" IS '生产商';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"address\" IS '站址';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"facility_code\" IS '设施;设施id';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"installation_date\" IS '安装日期';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"geometry\" IS '空间信息';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"data_access_configuration\" IS '数据接入配置;{   \"source\": [     \"KAFKA\",     \"HTTP\",     \"RABIITMQ\"   ],   \"converters\": [     {       \"source\": \"xxx\",       \"targer\": \"xxx\",       \"converterType\": \"xxx转换器\",       \"defaultValue\": \"\"     }   ],   \"clean\": [     {       \"key\": \"监测指标\",       \"value\": \"清洗值\",       \"action\": \">/=/is null/not null\",       \"logic\": \"and/or\"     }   ],   \"trasform\": [     {       \"index\": \"监测指标\",       \"value\": \"变换后的目标值\"     }   ],   \"storage\": {     \"type\": \"database/key-value/document/search-engine/time-series/graph\",     \"policy\": \"database/redis/mangodb/elasticsearch/influxdb/neo4j\",     \"index\": \"表名或者索引名称\"   },   \"push\": [     \"WEBSOCKET\",     \"MQTT\"   ] }';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"create_user\" IS '创建人';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"create_time\" IS '创建时间';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"create_dept\" IS '创建部门';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"update_user\" IS '更新人';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"update_time\" IS '更新时间';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"update_dept\" IS '更新部门';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"status\" IS '状态';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"is_deleted\" IS '逻辑删除';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"tenant_id\" IS '租户';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"images\" IS '图片信息';\n" +
                "\n" +
                "COMMENT\n" +
                "ON COLUMN \"am_device_instance\".\"parent_code\" IS '父设备code';\n" +
                "\n" +
                "COMMENT\n" +
                "ON TABLE \"am_device_instance\" IS '设备实例';\n";
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(cr_pg, DbType.postgresql);

    }
}
