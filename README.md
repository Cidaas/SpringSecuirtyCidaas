Setup your rest service project

Add the following dependency in your pom.xml

<dependency>
			<groupId>de.cidaas</groupId>
			<artifactId>cidaas-interceptor-spring-security-xml</artifactId>
			<version>0.0.1</version>
</dependency>

Usage : 

Create a ``applicationContext-security.xml`` file in your project configuration folder.
Add the following code into the ``applicationContext-security.xml`` file.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans:beans xmlns:beans="http://www.springframework.org/schema/beans"
	xmlns="http://www.springframework.org/schema/security" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.2.xsd
          http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.2.xsd
          http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security.xsd">

	<context:component-scan base-package="de.cidaas" />
	<context:annotation-config />

	<http pattern="/**" auto-config='false' create-session="stateless"
		entry-point-ref="entryPoint" use-expressions="true">
			
		<intercept-url pattern="/v1/**" access="isAuthenticated()" />
		<intercept-url pattern="/public/calendar"  access="permitAll" />
		<intercept-url pattern="/leavetype" method="OPTIONS" access="denyAll()" />
		<intercept-url pattern="/myprofile" access="isAuthenticated()" />
		<intercept-url pattern="/api/employeelist" access="hasRole('HR')" />
		<intercept-url pattern="/api/holidaylist" access="hasAuthority('holidaylist:read')" />
		

		<custom-filter ref="optionsFilter" before="BASIC_AUTH_FILTER" />
		<custom-filter ref="tokenFilter" after="BASIC_AUTH_FILTER" />
		<csrf disabled="true" />
	</http>

	<beans:bean id="optionsFilter" class="de.cidaas.config.OptionsFilter" />

	<beans:bean id="entryPoint"
		class="org.springframework.security.web.authentication.HttpStatusEntryPoint">
		<beans:constructor-arg name="httpStatus">
			<beans:value type="org.springframework.http.HttpStatus">UNAUTHORIZED</beans:value>
		</beans:constructor-arg>
	</beans:bean>

	<!-- Authentication manager -->
	<authentication-manager alias="authenticationManager">
		<!-- Access token -->
		<authentication-provider ref="tokenAuthenticationProvider" />
	</authentication-manager>

	<!-- Token authentication provider -->
	<beans:bean id="tokenAuthenticationProvider"
		class="de.cidaas.config.TokenAuthenticationProvider" />

	<!-- OAuth2 Token authentication filter -->
	<beans:bean id="tokenFilter" class="de.cidaas.config.TokenFilterBean" />

</beans:beans>

```

Create a ``applicationContext.xml`` file in your project configuration folder.
Add the following code into the ``applicationContext.xml`` file.
applicationContext-security.xml file is initiated in this file only.
```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans:beans
	xmlns="http://www.springframework.org/schema/mvc"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:beans="http://www.springframework.org/schema/beans"
	xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/mvc
                                 http://www.springframework.org/schema/mvc/spring-mvc.xsd
                                 http://www.springframework.org/schema/beans
                                 http://www.springframework.org/schema/beans/spring-beans.xsd
                                 http://www.springframework.org/schema/context
                                 http://www.springframework.org/schema/context/spring-context.xsd">


	<annotation-driven />

	<resources mapping="/resources/**" location="/resources/" />

	<context:component-scan
		base-package="de.cidaas" />
		
	<beans:import
		resource="classpath:/configuration/applicationContext-security.xml" /> 
		
	<beans:bean
		class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
		<beans:property name="messageConverters">
			<beans:list>
				<beans:ref bean="jsonMessageConverter" />
			</beans:list>
		</beans:property>
	</beans:bean>

	<!-- Configure bean to convert JSON to POJO and vice versa -->
	<beans:bean id="jsonMessageConverter"
		class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter" />

	<beans:bean id="requestMappingHandlerMapping"
		class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping">
	</beans:bean>

</beans:beans>

```

Create a ``services-context.xml`` file in your project configuration folder.
Add the following code into the ``services-context.xml`` file.
```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:security="http://www.springframework.org/schema/security"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/security
                           http://www.springframework.org/schema/security/spring-security.xsd">

    

    <bean id="authenticationTokenProcessingFilter"
          class="de.cidaas.filter.AuthenticationTokenProcessingFilter">
        <constructor-arg ref="authenticationManager"/>
    </bean>

    <!-- Configures in-memory implementation of the UserDetailsService implementation -->
    <security:authentication-manager alias="authenticationManager">
       <!--  <security:authentication-provider ref="apiServerAuthenticationProvider"/> -->
        
    </security:authentication-manager>
    <bean id="accessDecisionManager" class="org.springframework.security.access.vote.AffirmativeBased">
        <constructor-arg name="decisionVoters">
            <list>
                <bean class="org.springframework.security.access.vote.RoleVoter">
                    <property name="rolePrefix" value=""/>
                </bean>
            </list>
        </constructor-arg>
    </bean>

     <bean id="exceptionTranslationFilter" class="org.springframework.security.web.access.ExceptionTranslationFilter">
        <constructor-arg name="authenticationEntryPoint" ref="apiServerAuthenticationEntryPoint"/>
    </bean> 

</beans>
```

Property configuration: 

Create a `cidaas_config.properties` file inside `resources/properties/conf` directory 

base_url=https://<cidaas-base-url>.cidaas.de
client_id=<non-interactive-app-client-id>
client_secret=<non-interactive-app-client-secret>