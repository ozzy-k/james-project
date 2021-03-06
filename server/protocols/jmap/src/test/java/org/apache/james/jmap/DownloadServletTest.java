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

package org.apache.james.jmap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletResponse;

import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.junit.Test;

public class DownloadServletTest {

    @Test
    public void blobIdFromShouldSkipTheFirstCharacter() {
        String blobId = new DownloadServlet(null).blobIdFrom("1234");
        assertThat(blobId).isEqualTo("234");
    }

    @Test
    public void downloadMayFailWhenUnableToCreateAttachmentMapper() throws Exception {
        MailboxSession mailboxSession = mock(MailboxSession.class);
        MailboxSessionMapperFactory mailboxSessionMapperFactory = mock(MailboxSessionMapperFactory.class);
        when(mailboxSessionMapperFactory.createAttachmentMapper(mailboxSession))
            .thenThrow(new MailboxException());

        DownloadServlet testee = new DownloadServlet(mailboxSessionMapperFactory);

        String blobId = null;
        HttpServletResponse resp = mock(HttpServletResponse.class);
        testee.download(mailboxSession, blobId, resp);

        verify(resp).setStatus(500);
    }
}
