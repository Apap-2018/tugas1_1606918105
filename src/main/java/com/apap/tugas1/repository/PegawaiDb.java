package com.apap.tugas1.repository;

import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.PegawaiModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PegawaiDb extends JpaRepository< PegawaiModel , Long >{
	Optional<PegawaiModel> findByNip (String nip);
}

