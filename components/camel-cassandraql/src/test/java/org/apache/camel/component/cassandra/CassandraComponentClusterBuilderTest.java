/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.cassandra;

import java.util.HashMap;
import java.util.Map;

import com.datastax.driver.core.Cluster;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Unit test for {@link CassandraComponent}
 */
public class CassandraComponentClusterBuilderTest {
    private final CassandraComponent component = new CassandraComponent();

    @Before
    public void setUp() {
        component.setCamelContext(new DefaultCamelContext());
    }

    @Test
    public void testClusterBuilderBasic() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("clusterName", "cluster");
        Cluster.Builder clusterBuilder = component.clusterBuilder("127.0.0.1,127.0.0.2/keyspace", params);

        assertEquals(2, clusterBuilder.getContactPoints().size());
        assertThat(clusterBuilder.getContactPoints().get(0).getHostName(), isOneOf("127.0.0.1", "localhost"));
        assertThat(clusterBuilder.getContactPoints().get(1).getHostName(), isOneOf("127.0.0.2", "localhost"));
        assertEquals("cluster", clusterBuilder.getClusterName());
        assertEquals("keyspace", params.get("keyspace"));
    }

    @Test
    public void testClusterBuilderPort() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("clusterName", "cluster");
        Cluster.Builder clusterBuilder = component.clusterBuilder("127.0.0.1,127.0.0.2:1234/keyspace", params);

        assertEquals(2, clusterBuilder.getContactPoints().size());
        assertThat(clusterBuilder.getContactPoints().get(0).getHostName(), isOneOf("127.0.0.1", "localhost"));
        assertEquals(1234, clusterBuilder.getContactPoints().get(0).getPort());
        assertThat(clusterBuilder.getContactPoints().get(1).getHostName(), isOneOf("127.0.0.2", "localhost"));
        assertEquals(1234, clusterBuilder.getConfiguration().getProtocolOptions().getPort());
        assertEquals("cluster", clusterBuilder.getClusterName());
        assertEquals("keyspace", params.get("keyspace"));
    }

    @Test
    public void testClusterBuilderSimplest() {
        Map<String, Object> params = new HashMap<String, Object>();
        Cluster.Builder clusterBuilder = component.clusterBuilder("127.0.0.1", params);

        assertEquals(1, clusterBuilder.getContactPoints().size());
        assertThat(clusterBuilder.getContactPoints().get(0).getHostName(), isOneOf("127.0.0.1", "localhost"));
        assertNull(params.get("keyspace"));
    }

}
