package com.ibm.cai.employee.config;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration class.
* @auhor rajasood@in.ibm.com
 */

@Configuration
@EnableSwagger2
@Import({BeanValidatorPluginsConfiguration.class})
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    private final Logger myLogger = LoggerFactory.getLogger(this.getClass());
        
    @Value("${build.version}")
    private String buildVersion;
    
    @Value("${build.date}")
    private String buildDate;
    
/*    
   @Bean
    public Docket greetingApi() {
    return new Docket(DocumentationType.SWAGGER_2)
    .select()
    .apis(RequestHandlerSelectors.basePackage("com.ibm.cai.akka.controller"))
    .build()
    .apiInfo(metaData());

    }*/
        
    /**
     * Adds a custom view controller to redirect the application root to
     * the swagger documentation page.
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // root url will 404 (if logged in), so instead redirect to documentation page
        registry.addRedirectViewController("/","/swagger-ui.html");
    }
    
    /**
     * Returns a newly configured Docket
     * @return a newly configured Docket
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                    //.apis((RequestHandlerSelectors.basePackage("com.ibm.cai.employee.controller")))
                    .paths(paths())
                    .build()
                    .apiInfo(metaData());
    }
    
    
    private Predicate<String> paths() {
        return Predicates.not(PathSelectors.regex("/basic-error-controller.*"));
    }
    /**
     * Returns the build date as a ZonedDateTime
     *      */
    private ZonedDateTime getBuildDateTime() {
        ZonedDateTime zdt = null;
        
        try {
            if( buildDate != null && !buildDate.endsWith( "@timestamp@" ) ) {
                // parse buildDate, eg. "2021-02-15T10:40:39.615-0500"
                zdt = ZonedDateTime.parse( buildDate, DateTimeFormatter.ofPattern( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" ) );
            } else {
                // use now as a default if the build date is not found
                zdt = ZonedDateTime.now();
            }
        } catch( Exception e ) {
            myLogger.error( "Unexpected error parsing isoDateTimeString", e );
        }
        
        return zdt;
    }
    
    /**
     * Returns the version string to display on Swagger UI
     * @return the version string to display on Swagger UI
     */
    private String getVersion() {
        StringBuilder version = new StringBuilder();
        
        // check if version property set (this will equal template value
        // if the maven filter doesn't work for any reason)
        if( buildVersion != null && !buildVersion.equals( "@pom.version@" ) ) {
            // append version String
            version.append( 'v' ).append( buildVersion );
        }
        
        // append build date (in unix epoch time)
        ZonedDateTime buildDateTime = getBuildDateTime();
        if( buildDateTime != null ) {
            version.append( " build " ).append( buildDateTime.toEpochSecond() );
        }
        
        return version.toString();
    }
    
    /**
     * Returns a newly configured ApiInfo
     * @return a newly configured ApiInfo
     */
    private ApiInfo metaData() {

        // note: swagger 2.8 appears to expect fully qualified urls for the links below
        
        // return API info
        return new ApiInfoBuilder()
                .title( "Reference Services API" )
                .description( "REST API Documentation for Reference Services" )
                .version( getVersion() )
                .build();
    }

}
