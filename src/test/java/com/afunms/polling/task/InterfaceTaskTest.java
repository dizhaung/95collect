package com.afunms.polling.task;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.afunms.topology.model.HostNode;
import com.gatherdb.GathersqlRun;

public class InterfaceTaskTest {

	private InterfaceTask task;
	@Before
	public void setUp() throws Exception {
		task = new InterfaceTask();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRun() {
		
	}

	@Ignore
	@Test
	public void testCollect_Data() {
		HostNode host = new HostNode();
		host.setIpAddress("127.0.0.1");
		host.setCommunity("public");
		host.setSnmpversion(1);
		task.setHost(host);
		task.collect_Data();
		GathersqlRun run = new GathersqlRun();
		run.run();
	}

}
