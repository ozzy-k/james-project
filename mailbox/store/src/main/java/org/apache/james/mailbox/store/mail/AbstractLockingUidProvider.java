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
package org.apache.james.mailbox.store.mail;

import org.apache.james.mailbox.MailboxPathLocker;
import org.apache.james.mailbox.MailboxPathLocker.LockAwareExecution;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.store.StoreMailboxPath;
import org.apache.james.mailbox.store.mail.model.Mailbox;


/**
 * Abstract base implementation of {@link UidProvider} which used the given {@link MailboxPathLocker} to 
 * lock the {@link Mailbox} while the next uid is generated
 * 
 *
 */
public abstract class AbstractLockingUidProvider implements UidProvider{

    private final MailboxPathLocker locker;

    public AbstractLockingUidProvider(MailboxPathLocker locker) {
        this.locker = locker;
    }
    
    @Override
    public long nextUid(final MailboxSession session, final Mailbox mailbox) throws MailboxException {
        return locker.executeWithLock(session, new StoreMailboxPath(mailbox), new LockAwareExecution<Long>() {

            @Override
            public Long execute() throws MailboxException {
                return lockedNextUid(session, mailbox);
            }
        }, true);
    }
    
    /**
     * Generate the next uid to use while the {@link Mailbox} is locked
     * 
     * @param session
     * @param mailbox
     * @return nextUid
     * @throws MailboxException
     */
    protected abstract long lockedNextUid(MailboxSession session, Mailbox mailbox) throws MailboxException;

}
