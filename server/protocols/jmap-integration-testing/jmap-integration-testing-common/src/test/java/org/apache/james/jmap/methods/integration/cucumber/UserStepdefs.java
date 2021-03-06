/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.jmap.methods.integration.cucumber;

import javax.inject.Inject;

import org.apache.james.jmap.JmapAuthentication;
import org.apache.james.jmap.api.access.AccessToken;

import cucumber.api.java.en.Given;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class UserStepdefs {

    private final MainStepdefs mainStepdefs;
    protected String username;
    protected AccessToken accessToken;

    @Inject
    private UserStepdefs(MainStepdefs mainStepdefs) {
        this.mainStepdefs = mainStepdefs;
    }

    @Given("^a domain named \"([^\"]*)\"$")
    public void createDomain(String domain) throws Throwable {
        mainStepdefs.jmapServer.serverProbe().addDomain(domain);
    }

    @Given("^a current user with username \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void createUserWithPasswordAndAuthenticate(String username, String password) throws Throwable {
        this.username = username;
        mainStepdefs.jmapServer.serverProbe().addUser(username, password);
        accessToken = JmapAuthentication.authenticateJamesUser(username, password);
    }

}
