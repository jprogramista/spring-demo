package com.example.demo.configuration

import com.example.demo.configuration.oauth.OAuthSuccessHandler
import com.example.demo.service.OAuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.OAuth2ClientContext
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.CompositeFilter
import javax.servlet.Filter
import javax.sql.DataSource


/**
If only method access is what we need we can put configuration on:
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class MethodSecurityConfig : GlobalMethodSecurityConfiguration()
 */

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class SecurityConfiguration(private val dataSource: DataSource,
                            private val oAuthService: OAuthService,
                            private val passwordEncoder: PasswordEncoder,
                            private val oauth2ClientContext: OAuth2ClientContext) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    public override fun configure(auth: AuthenticationManagerBuilder?) {

        // order 1
        auth!!.inMemoryAuthentication()
            .withUser("user").password("\$2a\$10\$LGnaUUfQAnQyzxqqLY5AnOkBsoms3eBKgYyYDXAx.WjF6UwWidC3e").roles("USER")
            .and()
            .withUser("admin").password("\$2a\$10\$LGnaUUfQAnQyzxqqLY5AnOkBsoms3eBKgYyYDXAx.WjF6UwWidC3e").roles("USER", "ADMIN")

        // order 2
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select email, password, enabled from users where email = ? and oauth_service_type is null")
                .authoritiesByUsernameQuery("select email, role from authorities where email = ?")
                .passwordEncoder(passwordEncoder)


    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {

            http.csrf().disable()

            http
                .httpBasic().and()
                .formLogin().loginPage("/form-login").defaultSuccessUrl("/").permitAll()
                .and()
                .antMatcher("/**").authorizeRequests()
                .antMatchers(HttpMethod.GET, "/welcome").authenticated()
                .antMatchers(HttpMethod.GET, "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/**").hasRole("ADMIN")
                .antMatchers("/", "/favicon.ico", "/login**", "/error**", "/sign-up")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().logout().logoutSuccessUrl("/").permitAll()
                .and().rememberMe()
                .rememberMeParameter("rememberme").tokenRepository(tokenRepository())
                //.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter::class.java)

    }

    @Bean
    fun oauth2ClientFilterRegistration(filter : OAuth2ClientContextFilter) : FilterRegistrationBean<OAuth2ClientContextFilter> {
        val registration = FilterRegistrationBean<OAuth2ClientContextFilter>()
        registration.filter = filter
        registration.order = -100
        return registration
    }

    @Bean
    fun tokenRepository() : PersistentTokenRepository {
        val jdbcTokenRepositoryImpl= JdbcTokenRepositoryImpl()
        jdbcTokenRepositoryImpl.setDataSource(dataSource)
        return jdbcTokenRepositoryImpl
    }

    @Bean
    @ConfigurationProperties("facebook.client")
    fun facebook(): AuthorizationCodeResourceDetails {
        return AuthorizationCodeResourceDetails()
    }

    @Bean
    @ConfigurationProperties("facebook.resource")
    fun facebookResource(): ResourceServerProperties {
        return ResourceServerProperties()
    }

    @Bean
    @ConfigurationProperties("github.client")
    fun github(): AuthorizationCodeResourceDetails {
        return AuthorizationCodeResourceDetails()
    }

    @Bean
    @ConfigurationProperties("github.resource")
    fun githubResource(): ResourceServerProperties {
        return ResourceServerProperties()
    }

    @Bean
    @ConfigurationProperties("google.client")
    fun google(): AuthorizationCodeResourceDetails {
        return AuthorizationCodeResourceDetails()
    }

    @Bean
    @ConfigurationProperties("google.resource")
    fun googleResource(): ResourceServerProperties {
        return ResourceServerProperties()
    }

    @Bean
    @ConfigurationProperties("linked-in.client")
    fun linkedIn(): AuthorizationCodeResourceDetails {
        return AuthorizationCodeResourceDetails()
    }

    @Bean
    @ConfigurationProperties("linked-in.resource")
    fun linkedInResource(): ResourceServerProperties {
        return ResourceServerProperties()
    }

    @Bean
    @ConfigurationProperties("twitter.client")
    fun twitter(): AuthorizationCodeResourceDetails {
        return AuthorizationCodeResourceDetails()
    }

    @Bean
    @ConfigurationProperties("twitter.resource")
    fun twitterResource(): ResourceServerProperties {
        return ResourceServerProperties()
    }


    @Bean
    fun oAuthSuccessHandler() : OAuthSuccessHandler {
        return OAuthSuccessHandler("/", oAuthService)
    }

    private fun ssoFilter(): Filter {
        val filter = CompositeFilter()
        val filters = ArrayList<Filter>()

        val facebookFilter = OAuth2ClientAuthenticationProcessingFilter("/login/facebook")
        val facebookTemplate = OAuth2RestTemplate(facebook(), oauth2ClientContext)
        facebookFilter.setRestTemplate(facebookTemplate)
        var tokenServices = UserInfoTokenServices(facebookResource().userInfoUri, facebook().clientId)
        tokenServices.setRestTemplate(facebookTemplate)
        facebookFilter.setTokenServices(tokenServices)
        facebookFilter.setAuthenticationSuccessHandler(oAuthSuccessHandler())
        filters.add(facebookFilter)

        val githubFilter = OAuth2ClientAuthenticationProcessingFilter("/login/github")
        val githubTemplate = OAuth2RestTemplate(github(), oauth2ClientContext)
        githubFilter.setRestTemplate(githubTemplate)
        tokenServices = UserInfoTokenServices(githubResource().userInfoUri, github().clientId)
        tokenServices.setRestTemplate(githubTemplate)
        githubFilter.setTokenServices(tokenServices)
        githubFilter.setAuthenticationSuccessHandler(oAuthSuccessHandler())
        filters.add(githubFilter)

        val googleFilter = OAuth2ClientAuthenticationProcessingFilter("/login/google")
        val googleTemplate = OAuth2RestTemplate(google(), oauth2ClientContext)
        googleFilter.setRestTemplate(googleTemplate)
        tokenServices = UserInfoTokenServices(googleResource().userInfoUri, google().clientId)
        tokenServices.setRestTemplate(googleTemplate)
        googleFilter.setTokenServices(tokenServices)
        googleFilter.setAuthenticationSuccessHandler(oAuthSuccessHandler())
        filters.add(googleFilter)

        val linkedInFilter = OAuth2ClientAuthenticationProcessingFilter("/login/linkedIn")
        val linkedInTemplate = OAuth2RestTemplate(linkedIn(), oauth2ClientContext)
        linkedInFilter.setRestTemplate(linkedInTemplate)
        tokenServices = UserInfoTokenServices(linkedInResource().userInfoUri, linkedIn().clientId)
        tokenServices.setRestTemplate(linkedInTemplate)
        linkedInFilter.setTokenServices(tokenServices)
        linkedInFilter.setAuthenticationSuccessHandler(oAuthSuccessHandler())
        filters.add(linkedInFilter)

        val twitterFilter = OAuth2ClientAuthenticationProcessingFilter("/login/twitter")
        val twitterTemplate = OAuth2RestTemplate(twitter(), oauth2ClientContext)
        twitterFilter.setRestTemplate(twitterTemplate)
        tokenServices = UserInfoTokenServices(twitterResource().userInfoUri, twitter().clientId)
        tokenServices.setRestTemplate(twitterTemplate)
        twitterFilter.setTokenServices(tokenServices)
        twitterFilter.setAuthenticationSuccessHandler(oAuthSuccessHandler())
        filters.add(twitterFilter)

        filter.setFilters(filters)
        return filter
    }
}