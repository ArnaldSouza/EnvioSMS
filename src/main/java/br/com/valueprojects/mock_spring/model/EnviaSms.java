package br.com.valueprojects.mock_spring.model;

public class EnviaSms {
	private SmsDaoFalso smsDao;
	
	public EnviaSms() {
		
	}
	
	public void enviarSmsParaVencedor(Sms smsVencedor, Participante vencedor) {
		smsVencedor.setMensagem(String.format("Parabéns %s! Você ganhou o jogo", vencedor.getNome()));
		smsVencedor.setDestinatario(vencedor.getNome());
		smsDao.salva(smsVencedor);
	}
}


