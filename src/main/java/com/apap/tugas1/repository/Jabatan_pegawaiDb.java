package com.apap.tugas1.repository;
import java.util.List;
import com.apap.tugas1.model.JabatanPegawaiModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Jabatan_pegawaiDb extends JpaRepository< JabatanPegawaiModel , Long >{
	//Optional<List<jabatanPegawaiModel>> findAllByPegawai_Nip(String nip);
    //List<jabatanPegawaiModel> findAllByJabatan_Id(Long id);
}
