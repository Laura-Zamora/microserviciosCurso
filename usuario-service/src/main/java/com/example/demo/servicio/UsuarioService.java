package com.example.demo.servicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entidades.Usuario;
import com.example.demo.feignclients.CarrofeignClient;
import com.example.demo.feignclients.MotoFeignClient;
import com.example.demo.modelos.Carro;
import com.example.demo.modelos.Moto;
import com.example.demo.repositorio.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CarrofeignClient carrofeignClient;

	@Autowired
	private MotoFeignClient motoFeignClient;

	public List<Usuario> getAll() {

		return usuarioRepository.findAll();
	}

	public Usuario getUsuarioById(int id) {

		return usuarioRepository.findById(id).orElse(null);
	}

	public Usuario save(Usuario usuario) {
		Usuario nuevoUsuario = usuarioRepository.save(usuario);
		return nuevoUsuario;
	}

	public List<Carro> getCarros(int usuarioId) {
		List<Carro> carros = restTemplate.getForObject("http://localhost:8002/carro/usuario/" + usuarioId, List.class);
		return carros;
	}

	public List<Moto> getMotos(int usuarioId) {
		List<Moto> motos = restTemplate.getForObject("http://localhost:8003/moto/usuario/" + usuarioId, List.class);
		return motos;
	}

	public Carro saveCarro(int usuarioId, Carro carro) {
		carro.setUsuarioId(usuarioId);
		Carro nuevoCarro = carrofeignClient.save(carro);
		return nuevoCarro;
	}

	public Moto saveMoto(int usuarioId, Moto moto) {
		moto.setUsuarioId(usuarioId);
		Moto nuevaMoto = motoFeignClient.save(moto);
		return nuevaMoto;
	}

	public Map<String, Object> getUsuarioAndVehiculos(int usuarioId) {
		Map<String, Object> resultado = new HashMap<>();
		Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
		
		if (usuario == null) {
			resultado.put("Mensaje", "El usuaro no existe");
			return resultado;
		}
		
		resultado.put("Usuario", usuario);
		List<Carro> carros = carrofeignClient.getCarros(usuarioId);
		
		if (carros.isEmpty()) {
			resultado.put("Carros", "El usuario no tiene carros");
		} else {
			resultado.put("Carros", carros);
		}
		
		List<Moto> motos = motoFeignClient.getMotos(usuarioId);
		if (motos.isEmpty()) {
			resultado.put("Motos", "El usuario no tiene motos");
		} else {
			resultado.put("Motos", carros);
		}
		
		return resultado;
	}
}
