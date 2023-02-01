package com.example.demo.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entidades.Carro;

@Repository
public interface CarroRepository extends JpaRepository<Carro,Integer>{

	List<Carro> findByUsuarioId(int usuarioId);
}
