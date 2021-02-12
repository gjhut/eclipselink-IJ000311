# Eclipselink test application

This is a test application meant to investigate in which scenarios a 
JBoss EAP 7.2 / 7.3 application generates an IJ000311 info message 
with a stacktrace (see below)

The application also provides the logging in the latest Wildfly 22.0 version, and uses only the standard H2 ExampleDS datasource, so no 
additional database is needed. 

## Analysis

The application shows that the message is logged in the following
situations: 

- with native SQL queries (direct or named)
- within an EJB bean
- in a second transaction in a request

Changes making the stacktrace logging go away:
- Making the TestSessionBean a @Stateless session bean.
- Removing the @Stateless annotation from the TestDao class.
- Using standard JPA queries instead of native SQL queries.
- Encapsulating all requests in one transaction.
- Using the JBoss Hibernate distribution (by removing the <provider.. /> in the persistence.xml )

We tried to run the application in JBoss EAP 6.4, and that also removed the stacktrace logging.

So it looks like  some change in JBoss EAP 7.x made the stacktrace logging appear for native queries that are executed in a multi transaction request where a combination of CDI / EJB beans are used.  

Unfortunately, this is a combination that happens a lot with any larger JSF application, as the application where this error originally occurred.  



## The stacktrace logging

````
09:26:10,382 INFO  [org.jboss.jca.core.connectionmanager.listener.TxConnectionListener] (default task-1) IJ000311: Throwable from unregister connection: java.lang.IllegalStateException: IJ000152: Trying to return an unknown connection: org.jboss.jca.adapters.jdbc.jdk8.WrappedConnectionJDK8@a34cc68
	at org.jboss.jca.core.connectionmanager.ccm.CachedConnectionManagerImpl.unregisterConnection(CachedConnectionManagerImpl.java:408)
	at org.jboss.jca.core.connectionmanager.listener.TxConnectionListener.connectionClosed(TxConnectionListener.java:645)
	at org.jboss.jca.adapters.jdbc.BaseWrapperManagedConnection.returnHandle(BaseWrapperManagedConnection.java:600)
	at org.jboss.jca.adapters.jdbc.BaseWrapperManagedConnection.closeHandle(BaseWrapperManagedConnection.java:545)
	at org.jboss.jca.adapters.jdbc.WrappedConnection.returnConnection(WrappedConnection.java:310)
	at org.jboss.jca.adapters.jdbc.WrappedConnection.close(WrappedConnection.java:268)
	at org.eclipse.persistence.internal.databaseaccess.DatabaseAccessor.closeDatasourceConnection(DatabaseAccessor.java:502)
	at org.eclipse.persistence.internal.databaseaccess.DatasourceAccessor.closeConnection(DatasourceAccessor.java:524)
	at org.eclipse.persistence.internal.databaseaccess.DatabaseAccessor.closeConnection(DatabaseAccessor.java:527)
	at org.eclipse.persistence.internal.databaseaccess.DatasourceAccessor.closeJTSConnection(DatasourceAccessor.java:190)
	at org.eclipse.persistence.sessions.server.ClientSession.releaseJTSConnection(ClientSession.java:175)
	at org.eclipse.persistence.transaction.AbstractSynchronizationListener.beforeCompletion(AbstractSynchronizationListener.java:177)
	at org.eclipse.persistence.transaction.JTASynchronizationListener.beforeCompletion(JTASynchronizationListener.java:70)
	at org.wildfly.transaction.client.AbstractTransaction.performConsumer(AbstractTransaction.java:236)
	at org.wildfly.transaction.client.AbstractTransaction.performConsumer(AbstractTransaction.java:247)
	at org.wildfly.transaction.client.AbstractTransaction$AssociatingSynchronization.beforeCompletion(AbstractTransaction.java:292)
	at com.arjuna.ats.internal.jta.resources.arjunacore.SynchronizationImple.beforeCompletion(SynchronizationImple.java:76)
	at com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator.beforeCompletion(TwoPhaseCoordinator.java:360)
	at com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator.end(TwoPhaseCoordinator.java:91)
	at com.arjuna.ats.arjuna.AtomicAction.commit(AtomicAction.java:162)
	at com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple.commitAndDisassociate(TransactionImple.java:1295)
	at com.arjuna.ats.internal.jta.transaction.arjunacore.BaseTransaction.commit(BaseTransaction.java:126)
	at com.arjuna.ats.jbossatx.BaseTransactionManagerDelegate.commit(BaseTransactionManagerDelegate.java:94)
	at org.wildfly.transaction.client.LocalTransaction.commitAndDissociate(LocalTransaction.java:78)
	at org.wildfly.transaction.client.ContextTransactionManager.commit(ContextTransactionManager.java:71)
	at com.arjuna.ats.jta.cdi.DelegatingTransactionManager.commit(DelegatingTransactionManager.java:128)
	at com.arjuna.ats.jta.cdi.NarayanaTransactionManager.commit(NarayanaTransactionManager.java:323)
	at sun.reflect.GeneratedMethodAccessor31.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.jboss.weld.bean.proxy.AbstractBeanInstance.invoke(AbstractBeanInstance.java:38)
	at org.jboss.weld.bean.proxy.ProxyMethodHandler.invoke(ProxyMethodHandler.java:106)
	at org.jboss.weld.io.Serializable$TransactionManager$305494972$Proxy$_$$_WeldClientProxy.commit(Unknown Source)
	at com.arjuna.ats.jta.cdi.TransactionHandler.endTransaction(TransactionHandler.java:81)
	at com.arjuna.ats.jta.cdi.transactional.TransactionalInterceptorBase.invokeInOurTx(TransactionalInterceptorBase.java:191)
	at com.arjuna.ats.jta.cdi.transactional.TransactionalInterceptorBase.invokeInOurTx(TransactionalInterceptorBase.java:168)
	at com.arjuna.ats.jta.cdi.transactional.TransactionalInterceptorRequired.doIntercept(TransactionalInterceptorRequired.java:53)
	at com.arjuna.ats.jta.cdi.transactional.TransactionalInterceptorBase.intercept(TransactionalInterceptorBase.java:86)
	at com.arjuna.ats.jta.cdi.transactional.TransactionalInterceptorRequired.intercept(TransactionalInterceptorRequired.java:47)
	at sun.reflect.GeneratedMethodAccessor29.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.jboss.weld.interceptor.reader.SimpleInterceptorInvocation$SimpleMethodInvocation.invoke(SimpleInterceptorInvocation.java:73)
	at org.jboss.weld.interceptor.proxy.InterceptorMethodHandler.executeAroundInvoke(InterceptorMethodHandler.java:84)
	at org.jboss.weld.interceptor.proxy.InterceptorMethodHandler.executeInterception(InterceptorMethodHandler.java:72)
	at org.jboss.weld.interceptor.proxy.InterceptorMethodHandler.invoke(InterceptorMethodHandler.java:56)
	at org.jboss.weld.bean.proxy.CombinedInterceptorAndDecoratorStackMethodHandler.invoke(CombinedInterceptorAndDecoratorStackMethodHandler.java:79)
	at org.jboss.weld.bean.proxy.CombinedInterceptorAndDecoratorStackMethodHandler.invoke(CombinedInterceptorAndDecoratorStackMethodHandler.java:68)
	at com.eclipselink.test.session.TestSessionBean$Proxy$_$$_WeldSubclass.requestNativeNamedQuery(Unknown Source)
	at com.eclipselink.test.session.TestSessionBean$Proxy$_$$_WeldClientProxy.requestNativeNamedQuery(Unknown Source)
	at com.eclipselink.test.test_eclipselink.HelloServlet.doGet(HelloServlet.java:30)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:503)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:590)
	at io.undertow.servlet.handlers.ServletHandler.handleRequest(ServletHandler.java:74)
	at io.undertow.servlet.handlers.FilterHandler$FilterChainImpl.doFilter(FilterHandler.java:129)
	at io.opentracing.contrib.jaxrs2.server.SpanFinishingFilter.doFilter(SpanFinishingFilter.java:52)
	at io.undertow.servlet.core.ManagedFilter.doFilter(ManagedFilter.java:61)
	at io.undertow.servlet.handlers.FilterHandler$FilterChainImpl.doFilter(FilterHandler.java:131)
	at io.undertow.servlet.handlers.FilterHandler.handleRequest(FilterHandler.java:84)
	at io.undertow.servlet.handlers.security.ServletSecurityRoleHandler.handleRequest(ServletSecurityRoleHandler.java:62)
	at io.undertow.servlet.handlers.ServletChain$1.handleRequest(ServletChain.java:68)
	at io.undertow.servlet.handlers.ServletDispatchingHandler.handleRequest(ServletDispatchingHandler.java:36)
	at org.wildfly.extension.undertow.security.SecurityContextAssociationHandler.handleRequest(SecurityContextAssociationHandler.java:78)
	at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:43)
	at io.undertow.servlet.handlers.RedirectDirHandler.handleRequest(RedirectDirHandler.java:68)
	at io.undertow.servlet.handlers.security.SSLInformationAssociationHandler.handleRequest(SSLInformationAssociationHandler.java:117)
	at io.undertow.servlet.handlers.security.ServletAuthenticationCallHandler.handleRequest(ServletAuthenticationCallHandler.java:57)
	at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:43)
	at io.undertow.security.handlers.AbstractConfidentialityHandler.handleRequest(AbstractConfidentialityHandler.java:46)
	at io.undertow.servlet.handlers.security.ServletConfidentialityConstraintHandler.handleRequest(ServletConfidentialityConstraintHandler.java:64)
	at io.undertow.security.handlers.AuthenticationMechanismsHandler.handleRequest(AuthenticationMechanismsHandler.java:60)
	at io.undertow.servlet.handlers.security.CachedAuthenticatedSessionHandler.handleRequest(CachedAuthenticatedSessionHandler.java:77)
	at io.undertow.security.handlers.NotificationReceiverHandler.handleRequest(NotificationReceiverHandler.java:50)
	at io.undertow.security.handlers.AbstractSecurityContextAssociationHandler.handleRequest(AbstractSecurityContextAssociationHandler.java:43)
	at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:43)
	at org.wildfly.extension.undertow.security.jacc.JACCContextIdHandler.handleRequest(JACCContextIdHandler.java:61)
	at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:43)
	at org.wildfly.extension.undertow.deployment.GlobalRequestControllerHandler.handleRequest(GlobalRequestControllerHandler.java:68)
	at io.undertow.servlet.handlers.SendErrorPageHandler.handleRequest(SendErrorPageHandler.java:52)
	at io.undertow.server.handlers.PredicateHandler.handleRequest(PredicateHandler.java:43)
	at io.undertow.servlet.handlers.ServletInitialHandler.handleFirstRequest(ServletInitialHandler.java:269)
	at io.undertow.servlet.handlers.ServletInitialHandler.access$100(ServletInitialHandler.java:78)
	at io.undertow.servlet.handlers.ServletInitialHandler$2.call(ServletInitialHandler.java:133)
	at io.undertow.servlet.handlers.ServletInitialHandler$2.call(ServletInitialHandler.java:130)
	at io.undertow.servlet.core.ServletRequestContextThreadSetupAction$1.call(ServletRequestContextThreadSetupAction.java:48)
	at io.undertow.servlet.core.ContextClassLoaderSetupAction$1.call(ContextClassLoaderSetupAction.java:43)
	at org.wildfly.extension.undertow.security.SecurityContextThreadSetupAction.lambda$create$0(SecurityContextThreadSetupAction.java:105)
	at org.wildfly.extension.undertow.deployment.UndertowDeploymentInfoService$UndertowThreadSetupAction.lambda$create$0(UndertowDeploymentInfoService.java:1530)
	at org.wildfly.extension.undertow.deployment.UndertowDeploymentInfoService$UndertowThreadSetupAction.lambda$create$0(UndertowDeploymentInfoService.java:1530)
	at org.wildfly.extension.undertow.deployment.UndertowDeploymentInfoService$UndertowThreadSetupAction.lambda$create$0(UndertowDeploymentInfoService.java:1530)
	at org.wildfly.extension.undertow.deployment.UndertowDeploymentInfoService$UndertowThreadSetupAction.lambda$create$0(UndertowDeploymentInfoService.java:1530)
	at org.wildfly.extension.undertow.deployment.UndertowDeploymentInfoService$UndertowThreadSetupAction.lambda$create$0(UndertowDeploymentInfoService.java:1530)
	at io.undertow.servlet.handlers.ServletInitialHandler.dispatchRequest(ServletInitialHandler.java:249)
	at io.undertow.servlet.handlers.ServletInitialHandler.access$000(ServletInitialHandler.java:78)
	at io.undertow.servlet.handlers.ServletInitialHandler$1.handleRequest(ServletInitialHandler.java:99)
	at io.undertow.server.Connectors.executeRootHandler(Connectors.java:387)
	at io.undertow.server.HttpServerExchange$1.run(HttpServerExchange.java:841)
	at org.jboss.threads.ContextClassLoaderSavingRunnable.run(ContextClassLoaderSavingRunnable.java:35)
	at org.jboss.threads.EnhancedQueueExecutor.safeRun(EnhancedQueueExecutor.java:1990)
	at org.jboss.threads.EnhancedQueueExecutor$ThreadBody.doRunTask(EnhancedQueueExecutor.java:1486)
	at org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run(EnhancedQueueExecutor.java:1377)
	at org.xnio.XnioWorker$WorkerThreadFactory$1$1.run(XnioWorker.java:1280)
	at java.lang.Thread.run(Thread.java:748)
````