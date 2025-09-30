!/bin/bash

# Script de deploy para produção - Conecta Senior API
# Execute este script no servidor de produção

set -e  # Para em caso de erro

echo "🚀 Iniciando deploy da Conecta Senior API..."

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para log colorido
log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

warn() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] WARNING: $1${NC}"
}

error() {
    echo -e "${RED}[$(date +'%Y-%m-%d %H:%M:%S')] ERROR: $1${NC}"
    exit 1
}

# Verificações iniciais
log "Verificando dependências..."

# Verificar se Docker está instalado
if ! command -v docker &> /dev/null; then
    error "Docker não está instalado. Instale o Docker primeiro."
fi

# Verificar se Docker Compose está instalado
if ! command -v docker-compose &> /dev/null; then
    error "Docker Compose não está instalado."
fi

# Verificar se arquivo .env existe
if [ ! -f .env ]; then
    warn "Arquivo .env não encontrado. Copiando .env.example..."
    cp .env.example .env
    warn "IMPORTANTE: Edite o arquivo .env com suas configurações antes de continuar!"
    read -p "Pressione Enter após editar o .env..."
fi

# Fazer backup se já existir deploy anterior
if [ -d "backup" ]; then
    log "Fazendo backup da versão anterior..."
    BACKUP_DIR="backup/$(date +'%Y%m%d_%H%M%S')"
    mkdir -p "$BACKUP_DIR"
    docker-compose down || true
    cp -r . "$BACKUP_DIR/" || true
fi

# Build da aplicação
log "Fazendo build da aplicação..."
./mvnw clean package -DskipTests

# Verificar se o JAR foi gerado
if [ ! -f target/api-*.jar ]; then
    error "Falha no build da aplicação. JAR não foi gerado."
fi

# Build das imagens Docker
log "Fazendo build das imagens Docker..."
docker-compose build

# Parar containers anteriores
log "Parando containers anteriores..."
docker-compose down || true

# Iniciar serviços
log "Iniciando serviços..."
docker-compose up -d

# Aguardar inicialização
log "Aguardando inicialização dos serviços..."
sleep 30

# Verificar health
log "Verificando saúde da aplicação..."
for i in {1..10}; do
    if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
        log "✅ Aplicação está saudável!"
        break
    else
        warn "Tentativa $i/10 - Aguardando aplicação ficar disponível..."
        sleep 10
    fi

    if [ $i -eq 10 ]; then
        error "Aplicação não ficou disponível após 100 segundos"
    fi
done

# Mostrar status dos containers
log "Status dos containers:"
docker-compose ps

# Mostrar logs recentes
log "Logs recentes da aplicação:"
docker-compose logs --tail=20 api

echo ""
log "🎉 Deploy concluído com sucesso!"
log "📊 Swagger UI: https://localhost/swagger-ui.html"
log "🔍 Health Check: https://localhost/actuator/health"
log "📋 Para ver logs: docker-compose logs -f api"
log "🛑 Para parar: docker-compose down"

echo ""
warn "PRÓXIMOS PASSOS:"
warn "1. Configure seu domínio no nginx.conf"
warn "2. Adicione certificados SSL válidos"
warn "3. Configure backup automático do banco"
warn "4. Configure monitoramento (Prometheus/Grafana)"
