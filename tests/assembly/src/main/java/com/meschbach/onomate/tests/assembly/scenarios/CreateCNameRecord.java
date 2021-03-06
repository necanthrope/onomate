/*
 * Copyright 2014 Mark Eschbach.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.meschbach.onomate.tests.assembly.scenarios;

import java.util.UUID;
import org.openqa.selenium.WebDriver;
import static org.testng.Assert.assertEquals;

/**
 *
 * @author Mark Eschbach <meschbach@gmail.com>
 * @version 0.0.4
 * @since 0.0.5
 */
public class CreateCNameRecord implements AcceptanceScenario{

    public void run(WebDriver driver, String deployedURL) throws Exception {
        final UUID randomUUID = UUID.randomUUID();
        final String id = randomUUID.toString();
        final String zoneName = "soa-"+ id + ".assembly-tests.onomate.test";
        final String nameServer = "ns-"+id +".assembly-tests.onomate.test";
        final String contactEmail = "mail-" +id +"assembly-test.soa.test";
        final String recordHost = "record."+zoneName;
        final String recordRealName = "wait.test";

        OnomateAssembly assembly = new OnomateAssembly(driver, deployedURL);
        OnomateAssembly.Dashboard dashboard = assembly.gotoLanding().authenticate();
        dashboard.newAuthority(zoneName, recordRealName, contactEmail);
        OnomateAssembly.Authority authority = dashboard.authorityByZone(zoneName);
        OnomateAssembly.Zone details = authority.waitOnPersisted().details();
        OnomateAssembly.ResourceRow row = details.createRecord(recordHost, OnomateAssembly.RecordType.CNAME, recordRealName).getResource(recordHost);
        assertEquals(row.host(), recordHost);
        assertEquals(row.type(), "CNAME");
        assertEquals(row.value(), recordRealName);
    }
    
    public static void main( String arguments[] ){
        AcceptanceTestRunner runner = new AcceptanceTestRunner(arguments);
        runner.run(new CreateNSRecord());
    }
}
