package br.com.jway.igaratinga.util;
import java.io.File;

import javax.swing.JOptionPane;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;


public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();

	@SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
		try {

			File file = new File("hibernate.cfg.xml");
			if (!file.exists()) {
				JOptionPane
						.showMessageDialog(null,
								"arquivo de configuração do Banco de Dados não encontrado!");
				throw new ExceptionInInitializerError();
			} else {
				AnnotationConfiguration configuration = new AnnotationConfiguration();
				configuration.addAnnotatedClass(br.com.jway.igaratinga.model.Competencias.class);
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.Guias.class);
		
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.GuiasNotasFiscais.class);
		
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.NotasFiscais.class);
		
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.NotasFiscaisCanceladas.class);
				
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.NotasFiscaisCondPagamentos.class);
		
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.NotasFiscaisEmails.class);
		
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.NotasFiscaisObras.class);
		
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.NotasFiscaisPrestadores.class);
		
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.NotasFiscaisServicos.class);
				
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.NotasFiscaisSubst.class);
		
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.NotasFiscaisTomadores.class);
		
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.NotasFiscaisXml.class);
		
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.Pagamentos.class);
		
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.Prestadores.class);
		
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.PrestadoresAtividades.class);
		
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.PrestadoresOptanteSimples.class);
				
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.Tomadores.class);
		
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.Pessoa.class);
				
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.MunicipiosIbge.class);
				
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.Bairros.class);
				
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.Logradouros.class);
				
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.entidadesOrigem.DadosLivroPrestador.class);
				
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.entidadesOrigem.Servicos.class);
				
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.model.CnaeAtualizado.class);
				
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.entidadesOrigem.ServicosAliquota.class);
				
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.entidadesOrigem.ServicosContribuinte.class);
				
				configuration
				.addAnnotatedClass(br.com.jway.igaratinga.entidadesOrigem.AtividadeEconomicaContribuinte.class);
				
				
				SessionFactory sessionFactory = configuration.configure(file)
						.buildSessionFactory();
				return sessionFactory;
			}
		} catch (Throwable ex) {
			JOptionPane.showMessageDialog(null,
					"Erro em configuração do banco! Detalhes no log da aplicação");
			ex.printStackTrace();
			return null;
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		getSessionFactory().close();
	}

}
