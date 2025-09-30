-- Script de inicialização do banco PostgreSQL para produção
-- Este arquivo será executado automaticamente na primeira inicialização

-- Criar extensões necessárias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Configurações otimizadas para produção
ALTER SYSTEM SET shared_buffers = '256MB';
ALTER SYSTEM SET effective_cache_size = '1GB';
ALTER SYSTEM SET maintenance_work_mem = '64MB';
ALTER SYSTEM SET checkpoint_completion_target = 0.7;
ALTER SYSTEM SET wal_buffers = '16MB';
ALTER SYSTEM SET default_statistics_target = 100;

-- Recarregar configurações
SELECT pg_reload_conf();

-- Criar usuário da aplicação com privilégios limitados (mais seguro)
DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'conecta_app') THEN
      CREATE ROLE conecta_app LOGIN PASSWORD 'senha_segura_app';
   END IF;
END
$$;

-- Conceder privilégios específicos
GRANT CONNECT ON DATABASE conectasenior TO conecta_app;
GRANT CREATE ON SCHEMA public TO conecta_app;
GRANT USAGE ON SCHEMA public TO conecta_app;
