package br.com.jway.igaratinga.util;

import java.util.List;

import br.com.jway.igaratinga.service.ExtractorService;

public class MainTest {

	public static void main(String args[]) {

		ExtractorService extractorService = new ExtractorService();
		
		extractorService.processaAjusteEmails();
	}


	
}
