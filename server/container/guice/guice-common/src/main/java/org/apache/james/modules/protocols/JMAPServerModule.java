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

package org.apache.james.modules.protocols;

import java.security.Security;
import java.util.List;

import org.apache.james.jmap.JMAPModule;
import org.apache.james.jmap.JMAPServer;
import org.apache.james.jmap.crypto.JamesSignatureHandler;
import org.apache.james.lifecycle.api.Configurable;
import org.apache.james.utils.ConfigurationPerformer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

public class JMAPServerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JMAPModule());
        Multibinder.newSetBinder(binder(), ConfigurationPerformer.class).addBinding().to(JMAPModuleConfigurationPerformer.class);
    }

    @Singleton
    public static class JMAPModuleConfigurationPerformer implements ConfigurationPerformer {

        private final JMAPServer server;
        private final JamesSignatureHandler signatureHandler;

        @Inject
        public JMAPModuleConfigurationPerformer(JMAPServer server, JamesSignatureHandler signatureHandler) {
            this.server = server;
            this.signatureHandler = signatureHandler;
        }

        @Override
        public void initModule() {
            try {
                signatureHandler.init();
                server.configure(null);
                registerPEMWithSecurityProvider();
            } catch (Exception e) {
                Throwables.propagate(e);
            }
        }

        private void registerPEMWithSecurityProvider() {
            Security.addProvider(new BouncyCastleProvider());
        }

        @Override
        public List<Class<? extends Configurable>> forClasses() {
            return ImmutableList.of(JMAPServer.class);
        }
    }

}
