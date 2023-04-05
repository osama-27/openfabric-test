package ai.openfabric.api.config;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {
    @Value("${node.api.path}")
    private String REST_API_PREFIX;

    @Bean
    public Docket restApi() {

        List<SecurityScheme> securitySchemes = Lists.newArrayList(
                new ApiKey("Authorization", "Authorization", "header"));
        List<SecurityContext> securityContexts = Lists.newArrayList(
                xAuthTokenSecurityContext()
        );

        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ai.openfabric"))
                .paths(PathSelectors.ant(REST_API_PREFIX + "/**"))
                .build()
                .groupName("REST-API")
                .apiInfo(getApiInfo())
                .securitySchemes(securitySchemes)
                .securityContexts(securityContexts)
                .globalResponses(HttpMethod.GET, getGlobalResponses())
                .globalResponses(HttpMethod.POST, getGlobalResponses())
                .globalResponses(HttpMethod.PUT, getGlobalResponses())
                .globalResponses(HttpMethod.PATCH, getGlobalResponses())
                .globalResponses(HttpMethod.DELETE, getGlobalResponses());
    }
    private List<Response> getGlobalResponses() {
        return Arrays.asList(
                new ResponseBuilder().code("200").description("OK").build(),
                new ResponseBuilder().code("400").description("Bad Request").build(),
                new ResponseBuilder().code("401").description("Unauthorized").build(),
                new ResponseBuilder().code("403").description("Forbidden").build(),
                new ResponseBuilder().code("404").description("Not Found").build(),
                new ResponseBuilder().code("500").description("Internal Server Error").build()
        );
    }

    private static ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Openfabric")
                .version("v1")
                .build();
    }

    private SecurityContext xAuthTokenSecurityContext() {
        return SecurityContext.builder()
                .securityReferences(Lists.newArrayList(
                        new SecurityReference("Authorization", new AuthorizationScope[0])))
                .build();
    }


}
