package br.com.valueprojects.mock_spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.valueprojects.mock_spring.builder.CriadorDeJogo;
import br.com.valueprojects.mock_spring.model.*;
import infra.JogoDao;

public class FinalizaJogoTest {

	@Test
	public void deveFinalizarJogosDaSemanaAnterior() {

		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 1, 20);

		Jogo jogo1 = new CriadorDeJogo().para("Ca�a moedas").naData(antiga).constroi();
		Jogo jogo2 = new CriadorDeJogo().para("Derruba barreiras").naData(antiga).constroi();

		// mock no lugar de dao falso

		List<Jogo> jogosAnteriores = Arrays.asList(jogo1, jogo2);

		JogoDao daoFalso = mock(JogoDao.class);

		when(daoFalso.emAndamento()).thenReturn(jogosAnteriores);

		FinalizaJogo finalizador = new FinalizaJogo(daoFalso);
		finalizador.finaliza();

		assertTrue(jogo1.isFinalizado());
		assertTrue(jogo2.isFinalizado());
		assertEquals(2, finalizador.getTotalFinalizados());
	}

	@Test
	public void deveVerificarSeMetodoAtualizaFoiInvocado() {

		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 1, 20);

		Jogo jogo1 = new CriadorDeJogo().para("Cata moedas").naData(antiga).constroi();
		Jogo jogo2 = new CriadorDeJogo().para("Derruba barreiras").naData(antiga).constroi();

		// mock no lugar de dao falso

		List<Jogo> jogosAnteriores = Arrays.asList(jogo1, jogo2);

		JogoDao daoFalso = mock(JogoDao.class);

		when(daoFalso.emAndamento()).thenReturn(jogosAnteriores);

		FinalizaJogo finalizador = new FinalizaJogo(daoFalso);
		finalizador.finaliza();

		verify(daoFalso, times(1)).atualiza(jogo1);
		// Mockito.verifyNoInteractions(daoFalso);

	}
	
	
	@Test
	public void deveEnviarSmsAoVencedorApósFinalizarESalvarJogoDaSemanaAnteriorESalvarVencedor() {
	   Calendar antiga = Calendar.getInstance();
	   antiga.set(2024, 10, 26);
	   
	   Jogo jogo = new CriadorDeJogo().para("Recompensa - One Piece").naData(antiga).constroi();
	   
	   jogo.anota(new Resultado(new Participante("Vinsmoke Sanji"), 1032000));
	   jogo.anota(new Resultado(new Participante("Roronoa Zoro"), 1111000));
	   jogo.anota(new Resultado(new Participante("Mokey D. Luffy"), 3000000));
	   
	   JogoDao jogoDaoFalso = mock(JogoDao.class);
	   
	   FinalizaJogo finalizador = new FinalizaJogo(jogoDaoFalso);
	   finalizador.finaliza();
	   
	   jogoDaoFalso.salva(jogo);
	   verify(jogoDaoFalso, times(1)).salva(jogo);
	   
	   Juiz juiz = new Juiz();
	   juiz.julga(jogo);
	   
	   Participante vencedor = juiz.getTresMaiores(jogo).get(0).getParticipante();
	   
	   VencedorDaoFalso vencedorDaoFalso = mock(VencedorDaoFalso.class);
	   
	   vencedorDaoFalso.salva(vencedor);
	   verify(vencedorDaoFalso, times(1)).salva(vencedor);
	   
	   
	   EnviaSms enviaSms = mock(EnviaSms.class);
	   
	   Sms smsVencedor = new Sms();
	   enviaSms.enviarSmsParaVencedor(smsVencedor, vencedor);
	   verify(enviaSms, times(1)).enviarSmsParaVencedor(smsVencedor, vencedor); 
	}


}
