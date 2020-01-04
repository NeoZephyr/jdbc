package com.pain.flame.datasource.filter;

import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ConnectionLogFilter extends FilterEventAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionLogFilter.class);

    @Override
    public void connection_connectBefore(FilterChain chain, Properties info) {
        logger.info("=== before connection");
        super.connection_connectBefore(chain, info);
    }

    @Override
    public void connection_connectAfter(ConnectionProxy connection) {
        logger.info("=== after connection");
    }
}
