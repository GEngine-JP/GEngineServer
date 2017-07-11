/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package info.xiaomo.core.client;

import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 
 * 从grizzly的 FutureImpl精简而来. </br>
 * <p/>
 * (Based on the JDK {@link java.util.concurrent.FutureTask})
 *
 * @see Future
 */
public class ClientFuture<R> implements Future<R> {

    /**
     * Construct {@link ClientFuture}.
     */
    public static <R> ClientFuture<R> create() {
        return new ClientFuture<R>();
    }

    /**
     * Creates <tt>ClientFuture</tt>
     */
    public ClientFuture() {
        sync = new Sync();
    }

    /**
     * Set the result value and notify about operation completion.
     *
     * @param result the result value
     */
    public void result(R result) {
        sync.innerSet(result);
    }

    /**
     * Notify about the failure, occurred during asynchronous operation execution.
     *
     * @param failure failure
     */
    public void failure(Throwable failure) {
        sync.innerSetException(failure);
    }


    public R getResult() {
        if (isDone()) {
            try {
                return get();
            } catch (Throwable ignored) {
            }
        }

        return null;
    }



    /**
     * The method is called when this <tt>SafeFutureImpl</tt> is marked as completed.
     * Subclasses can override this method.
     */
    protected void onComplete() {
    }


    // FROM FUTURETASK =========================================================

    /**
     * Synchronization control for FutureTask
     */
    private final Sync sync;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCancelled() {
        return sync.innerIsCancelled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return sync.ranOrCancelled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return sync.innerCancel(mayInterruptIfRunning);
    }

    /**
     * @throws CancellationException {@inheritDoc}
     */
    @Override
    public R get() throws InterruptedException, ExecutionException {
        return sync.innerGet();
    }

    /**
     * @throws CancellationException {@inheritDoc}
     */
    @Override
    public R get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return sync.innerGet(unit.toNanos(timeout));
    }

    /**
     * Protected method invoked when this task transitions to state
     * <tt>isDone</tt> (whether normally or via cancellation). The
     * default implementation does nothing.  Subclasses may override
     * this method to invoke completion callbacks or perform
     * bookkeeping. Note that you can query status inside the
     * implementation of this method to determine whether this task
     * has been cancelled.
     */
    protected void done() {
        onComplete();
    }

    /**
     * Synchronization control for FutureTask. Note that this must be
     * a non-static inner class in order to invoke the protected
     * <tt>done</tt> method. For clarity, all inner class support
     * methods are same as outer, prefixed with "inner".
     * <p/>
     * Uses AQS sync state to represent run status
     */
    private final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -7828117401763700385L;

        /**
         * State value representing that task is ready to run
         */
        private static final int READY = 0;
        /**
         * State value representing that result/exception is being set
         */
        private static final int RESULT = 1;
        /**
         * State value representing that task ran
         */
        private static final int RAN = 2;
        /**
         * State value representing that task was cancelled
         */
        private static final int CANCELLED = 3;

        /**
         * The result to return from get()
         */
        private R result;
        /**
         * The exception to throw from get()
         */
        private Throwable exception;

        private boolean ranOrCancelled() {
            return (getState() & (RAN | CANCELLED)) != 0;
        }

        /**
         * Implements AQS base acquire to succeed if ran or cancelled
         */
        @Override
        protected int tryAcquireShared(int ignore) {
            return ranOrCancelled() ? 1 : -1;
        }

        /**
         * Implements AQS base release to always signal after setting
         * final done status by nulling runner thread.
         */
        @Override
        protected boolean tryReleaseShared(int ignore) {
            return true;
        }

        boolean innerIsCancelled() {
            return getState() == CANCELLED;
        }

        R innerGet() throws InterruptedException, ExecutionException {
            acquireSharedInterruptibly(0);
            if (getState() == CANCELLED) {
                throw new CancellationException();
            }
            if (exception != null) {
                throw new ExecutionException(exception);
            }
            return result;
        }

        R innerGet(long nanosTimeout)
        throws InterruptedException, ExecutionException, TimeoutException {
            if (!tryAcquireSharedNanos(0, nanosTimeout)) {
                throw new TimeoutException();
            }
            if (getState() == CANCELLED) {
                throw new CancellationException();
            }
            if (exception != null) {
                throw new ExecutionException(exception);
            }
            return result;
        }

        void innerSet(R v) {
            if (compareAndSetState(READY, RESULT)) {
                result = v;
                setState(RAN);
                releaseShared(0);
                done();
            }
        }

        void innerSetException(Throwable t) {
            if (compareAndSetState(READY, RESULT)) {
                exception = t;
                setState(RAN);
                releaseShared(0);
                done();
            }
        }

        boolean innerCancel(boolean mayInterruptIfRunning) {
            if (compareAndSetState(READY, CANCELLED)) {
                releaseShared(0);
                done();
                return true;
            }
            
            return false;
        }
    }
}