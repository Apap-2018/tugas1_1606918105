package com.apap.tugas1.service;
import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.PegawaiModel;


public interface PegawaiService {
	Optional<PegawaiModel> getPegawaiDetailByNip(String nip);
	List<PegawaiModel> getAllPegawai();
	PegawaiModel findPegawaiTertua(long idInstansi);
	PegawaiModel findPegawaiTermuda(long idInstansi);
	PegawaiModel getPegawaiById(Long idPegawai);
	void addPegawai(PegawaiModel pegawai);
	void updatePegawai(PegawaiModel pegawai);
	
}
