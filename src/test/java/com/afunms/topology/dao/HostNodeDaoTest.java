package com.afunms.topology.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HostNodeDaoTest {

	private HostNodeDao dao;
	@Before
	public void setUp() throws Exception {
		dao = new HostNodeDao();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadIsMonitoredNode() {
		System.out.println(dao.loadIsMonitoredNode());
	}

}
