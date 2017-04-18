package com.afunms.polling.task;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.afunms.topology.model.HostNode;
import com.gatherdb.GathersqlRun;

public class InterfaceTaskTest {

	private InterfaceTaskHC task;
	@Before
	public void setUp() throws Exception {
		task = new InterfaceTaskHC();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRun() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testCollect_Data() {
		HostNode host = new HostNode();
		host.setIpAddress("127.0.0.1");
		host.setCommunity("public");
		host.setSnmpversion(2);
		task.collect_Data(host,  Calendar.getInstance());
		GathersqlRun run = new GathersqlRun();
		run.run();
	}

}
