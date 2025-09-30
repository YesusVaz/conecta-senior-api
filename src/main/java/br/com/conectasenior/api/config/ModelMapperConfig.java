package br.com.conectasenior.api.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do ModelMapper para conversões automáticas entre DTOs e Entidades
 *
 * O ModelMapper facilita a conversão entre objetos de domínio (entidades) e
 * objetos de transferência de dados (DTOs), reduzindo código boilerplate.
 *
 * Configuração otimizada para:
 * - Estratégia STRICT: evita mapeamentos ambíguos
 * - Skip null values: não sobrescreve campos com valores nulos
 * - Field matching: permite mapear campos privados
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Configuração da estratégia de matching - STRICT evita mapeamentos incorretos
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        return mapper;
    }
}
