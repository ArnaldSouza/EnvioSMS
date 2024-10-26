package br.com.valueprojects.mock_spring.model;

import java.util.ArrayList;
import java.util.List;

public class VencedorDaoFalso {
	private static List<Participante> Vencedores = new ArrayList<Participante>();
	
	public void salva(Participante vencedor) {
		Vencedores.add(vencedor);
	}
	
	public List<Participante> getVencedores(){
		return VencedorDaoFalso.Vencedores;
	}
}

