package br.com.maycon.forum.config.swagger;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.maycon.forum.modelo.Usuario;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfigurations {

	@Bean //diz para o spring para ler esse bean
	//docket é do springfox
	public Docket forumApi() {
		return new Docket(DocumentationType.SWAGGER_2) //tipo de documentação
		        .select() //seleciona
		        .apis(RequestHandlerSelectors.basePackage("br.com.maycon.forum")) //diz apartir de qual pacote ele vai ler as classes
		        .paths(PathSelectors.ant("/**")) //passa qual paths/endpoints que o swagger pode ler, nosso caso vamo deixar tudo /**
		        .build() //compila/constroe
		        .ignoredParameterTypes(Usuario.class)//pedimos para ignorar as urls que trabalha com usuario pq é um dado sensivel
		        .globalOperationParameters(//cria um paramtro global para todos endpoints
		        		Arrays.asList( //cria uma lista
                        new ParameterBuilder() //instancia um construtor de parametros.
                                .name("Authorization") //nome do parametro
                                .description("Header para token JWT") //descrição a ser mostrada no swagger
                                .modelRef(new ModelRef("string")) //o tipo desse parametro no nosso caso String
                                .parameterType("header") //tipo de parametro a ser enviado nosso caso é um header
                                .required(false) //requirido no nosso caso é false pq tem endpoints que não precisa
                                .build())); //compila e constroi
	}
}
