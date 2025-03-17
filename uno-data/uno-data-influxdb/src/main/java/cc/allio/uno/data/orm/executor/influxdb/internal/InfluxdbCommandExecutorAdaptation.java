package cc.allio.uno.data.orm.executor.influxdb.internal;

import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.StringUtils;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientOptions;
import com.influxdb.client.OrganizationsApi;
import com.influxdb.client.OrganizationsQuery;
import com.influxdb.client.domain.Organization;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * solution operate influxdb bucket api and CRUD time series data api...
 * <p>should be contains how to get organization, maybe without creation</p>
 *
 * @author j.x
 * @since 1.1.8
 */
@Slf4j
public class InfluxdbCommandExecutorAdaptation {

    @Getter
    final InfluxDBClient influxDBClient;
    @Getter
    final InfluxDBClientOptions clientOptions;

    final ReadWriteLock readWriteLock;

    Organization currentOrganization;

    public InfluxdbCommandExecutorAdaptation(InfluxDBClient influxDBClient, InfluxDBClientOptions clientOptions) {
        this.influxDBClient = influxDBClient;
        this.clientOptions = clientOptions;
        this.readWriteLock = new ReentrantReadWriteLock();
    }


    /**
     * get {@link Organization}, if empty according {@link InfluxDBClientOptions#getOrg()} of new {@link Organization}
     *
     * @return organization
     */
    public Organization getCurrentOrganization() {
        Lock readLock = readWriteLock.readLock();
        readLock.lock();
        try {
            if (currentOrganization != null) {
                return currentOrganization;
            }
        } finally {
            readLock.unlock();
        }
        Lock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            OrganizationsApi organizationsApi = influxDBClient.getOrganizationsApi();
            OrganizationsQuery organizationsQuery = new OrganizationsQuery();
            organizationsQuery.setOrg(clientOptions.getOrg());
            List<Organization> organizations = organizationsApi.findOrganizations(organizationsQuery);
            if (organizations.isEmpty()) {
                log.warn("organization [{}] not found any one through api, now creat new organization base on InfluxDBClientOptions org name", clientOptions.getOrg());
                String org = clientOptions.getOrg();
                if (StringUtils.isBlank(org)) {
                    throw Exceptions.unNull("influxdb client options org value is empty.");
                }
                currentOrganization = organizationsApi.createOrganization(org);
            } else {
                // take out first one
                currentOrganization = organizations.getFirst();
            }

            return currentOrganization;
        } finally {
            writeLock.unlock();
        }
    }
}
