# Dockerfile para produção
FROM openjdk:24-jdk-slim

# Metadados
LABEL maintainer="conecta-senior-team"
LABEL version="1.0.0"
LABEL description="API Conecta Senior - Sistema de cuidado para idosos"

# Variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=prod
ENV TZ=America/Sao_Paulo

# Criar usuário não-root para segurança
RUN addgroup --system spring && adduser --system --ingroup spring spring

# Diretório de trabalho
WORKDIR /app

# Copiar arquivos de build
COPY target/api-*.jar app.jar

# Alterar ownership dos arquivos
RUN chown -R spring:spring /app

# Usar usuário não-root
USER spring

# Porta da aplicação
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
