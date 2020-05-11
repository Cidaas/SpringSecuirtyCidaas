# About this project
This is an example project for the cidaas-interceptor-spring-security-xml (Interceptor with xml configuration).  
If you don't want to xml for the interceptor configuration, you can use the https://github.com/Cidaas/cidaas-interceptor-spring-security instead.
## Getting Started!
To get started you can simply clone the **_cidaas-interceptor-spring-security-xml-example_** repository and install the dependencies.
## Prerequisites
You need git to clone the **_cidaas-interceptor-spring-security-xml-example_** repository. You can get git from [_here_](https://git-scm.com/)

You must have JAVA and its package manager \(maven\) installed. You can get them from [_JAVA here_](https://java.com/en/download/) and [_MAVEN here_](https://maven.apache.org/install.html).
## Clone SpringSecuirtyCidaas
Clone the **_cidaas-interceptor-spring-security-xml-example_** repository using git:
```
git clone https://github.com/Cidaas/cidaas-interceptor-spring-security-xml-example.git

```

## Add the cidaas spring security xml interceptor 
```
 <!-- add the following dependency in pom.xml --> 
 
<dependency>
	<groupId>de.cidaas</groupId>
	<artifactId>cidaas-interceptor-spring-security-xml</artifactId>
	<version>0.0.1</version>
</dependency>

```
## Usage

>Create a ``applicationContext-security.xml`` file in your project configuration folder.
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
		<intercept-url pattern="/myprofile" access="isAuthenticated()" />
		<intercept-url pattern="/api/employeelist" access="hasRole('HR')" />
		<intercept-url pattern="/api/holidaylist" access="hasAuthority('holidaylist:read')" />
		
                <!-- Use @PermitAll & @DenyAll annotations in service level to perform the PermitAll & DenyAll operations for corresponding service-->
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

>Create a ``applicationContext.xml`` file in your project configuration folder.
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

## Property configuration: 

>Create a `cidaas_config.properties` file inside `resources/properties/conf` directory 

```
base_url=https://<cidaas-base-url>
client_id=<non-interactive-app-client-id>
client_secret=<non-interactive-app-client-secret>
```
