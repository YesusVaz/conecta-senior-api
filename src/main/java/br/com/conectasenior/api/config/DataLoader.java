package br.com.conectasenior.api.config;

import br.com.conectasenior.api.entities.Usuario;
import br.com.conectasenior.api.entities.Idoso;
import br.com.conectasenior.api.entities.ContatoEmergencia;
import br.com.conectasenior.api.repositories.UsuarioRepository;
import br.com.conectasenior.api.repositories.IdosoRepository;
import br.com.conectasenior.api.repositories.ContatoEmergenciaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Carregador de dados para desenvolvimento
 *
 * Popula o banco H2 com dados de teste para facilitar o desenvolvimento
 * e testes da API. Ativo apenas no perfil 'dev'.
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final IdosoRepository idosoRepository;
    private final ContatoEmergenciaRepository contatoEmergenciaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("🔄 Carregando dados de teste para desenvolvimento...");

        carregarUsuarios();
        carregarIdosos();
        carregarContatosEmergencia();

        log.info("✅ Dados de teste carregados com sucesso!");
        log.info("📋 Usuários de teste:");
        log.info("   - Admin: admin@conectasenior.com / senha: admin123");
        log.info("   - Cuidador: cuidador@conectasenior.com / senha: cuidador123");
        log.info("   - Médico: medico@conectasenior.com / senha: medico123");
    }

    private void carregarUsuarios() {
        if (usuarioRepository.count() == 0) {
            log.info("Criando usuários de teste...");

            // Usuário Administrador
            Usuario admin = new Usuario();
            admin.setNome("Administrador Sistema");
            admin.setEmail("admin@conectasenior.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setTipo(Usuario.TipoUsuario.ADMINISTRADOR);
            admin.setTelefone("11999999999");
            admin.setAtivo(true);
            usuarioRepository.save(admin);

            // Usuário Cuidador
            Usuario cuidador = new Usuario();
            cuidador.setNome("Maria Silva");
            cuidador.setEmail("cuidador@conectasenior.com");
            cuidador.setSenha(passwordEncoder.encode("cuidador123"));
            cuidador.setTipo(Usuario.TipoUsuario.CUIDADOR);
            cuidador.setTelefone("11888888888");
            cuidador.setAtivo(true);
            usuarioRepository.save(cuidador);

            // Usuário Médico
            Usuario medico = new Usuario();
            medico.setNome("Dr. João Santos");
            medico.setEmail("medico@conectasenior.com");
            medico.setSenha(passwordEncoder.encode("medico123"));
            medico.setTipo(Usuario.TipoUsuario.MEDICO);
            medico.setTelefone("11777777777");
            medico.setAtivo(true);
            usuarioRepository.save(medico);

            log.info("✅ {} usuários criados", usuarioRepository.count());
        }
    }

    private void carregarIdosos() {
        if (idosoRepository.count() == 0) {
            log.info("Criando idosos de teste...");

            // Idoso 1
            Idoso idoso1 = new Idoso();
            idoso1.setNome("José da Silva");
            idoso1.setDataNascimento(LocalDate.of(1940, 5, 15));
            idoso1.setCpf("12345678901");
            idoso1.setEmail("jose.silva@email.com");
            idoso1.setTelefone("11666666666");
            idoso1.setEndereco("Rua das Flores, 123 - São Paulo/SP");
            idoso1.setObservacoes("Diabético, hipertenso. Toma medicamentos pela manhã.");
            idosoRepository.save(idoso1);

            // Idoso 2
            Idoso idoso2 = new Idoso();
            idoso2.setNome("Maria Oliveira");
            idoso2.setDataNascimento(LocalDate.of(1935, 8, 22));
            idoso2.setCpf("98765432109");
            idoso2.setEmail("maria.oliveira@email.com");
            idoso2.setTelefone("11555555555");
            idoso2.setEndereco("Av. Brasil, 456 - Rio de Janeiro/RJ");
            idoso2.setObservacoes("Cardíaca. Usa marcapasso. Evitar esforços físicos.");
            idosoRepository.save(idoso2);

            // Idoso 3
            Idoso idoso3 = new Idoso();
            idoso3.setNome("Antônio Santos");
            idoso3.setDataNascimento(LocalDate.of(1948, 12, 3));
            idoso3.setCpf("45678912345");
            idoso3.setTelefone("11444444444");
            idoso3.setEndereco("Rua da Paz, 789 - Belo Horizonte/MG");
            idoso3.setObservacoes("Mobilidade reduzida. Usa andador.");
            idosoRepository.save(idoso3);

            log.info("✅ {} idosos criados", idosoRepository.count());
        }
    }

    private void carregarContatosEmergencia() {
        if (contatoEmergenciaRepository.count() == 0) {
            log.info("Criando contatos de emergência...");

            Idoso idoso1 = idosoRepository.findByCpf("12345678901").orElse(null);
            Idoso idoso2 = idosoRepository.findByCpf("98765432109").orElse(null);

            if (idoso1 != null) {
                // Contatos do José
                ContatoEmergencia contato1 = new ContatoEmergencia();
                contato1.setNome("Ana Silva");
                contato1.setTelefone("11111111111");
                contato1.setRelacionamento("Filha");
                contato1.setEmail("ana.silva@email.com");
                contato1.setPrioridade(1);
                contato1.setObservacoes("Contato principal - mora próximo");
                contato1.setIdoso(idoso1);
                contatoEmergenciaRepository.save(contato1);

                ContatoEmergencia contato2 = new ContatoEmergencia();
                contato2.setNome("Carlos Silva");
                contato2.setTelefone("11222222222");
                contato2.setRelacionamento("Filho");
                contato2.setPrioridade(2);
                contato2.setObservacoes("Contato secundário");
                contato2.setIdoso(idoso1);
                contatoEmergenciaRepository.save(contato2);
            }

            if (idoso2 != null) {
                // Contatos da Maria
                ContatoEmergencia contato3 = new ContatoEmergencia();
                contato3.setNome("Pedro Oliveira");
                contato3.setTelefone("11333333333");
                contato3.setRelacionamento("Neto");
                contato3.setEmail("pedro.oliveira@email.com");
                contato3.setPrioridade(1);
                contato3.setObservacoes("Mora com a avó");
                contato3.setIdoso(idoso2);
                contatoEmergenciaRepository.save(contato3);
            }

            log.info("✅ {} contatos de emergência criados", contatoEmergenciaRepository.count());
        }
    }
}
