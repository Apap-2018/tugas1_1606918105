package com.apap.tugas1.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.repository.PegawaiDb;
import com.apap.tugas1.service.PegawaiService;

@Service
@Transactional
public class PergawaiServiceImpl implements PegawaiService  {
	@Autowired
	private PegawaiDb pegawaiDb;

	@Override
	public Optional<PegawaiModel> getPegawaiDetailByNip(String nip) {
		// TODO Auto-generated method stub
		return pegawaiDb.findByNip(nip);
	}

	@Override
	public List<PegawaiModel> getAllPegawai() {
		
		return pegawaiDb.findAll();
	}

	@Override
	public PegawaiModel findPegawaiTertua(long idInstansi) {
		List<PegawaiModel> pegawaiInstansi = new ArrayList<PegawaiModel>();
		for (PegawaiModel pegawai :this.getAllPegawai()) {
			if (pegawai.getInstansi().getId() == idInstansi) { //jika pegawai memiliki id instansi sama dengan idInstansi yang dicari (ex: aceh)
				pegawaiInstansi.add(pegawai);
			}
		}
		PegawaiModel pegawaiTertua = pegawaiInstansi.get(0);
		for(int i=1; i<pegawaiInstansi.size() ; i++) {
			if(pegawaiInstansi.get(i).getTanggalLahir().before(pegawaiTertua.getTanggalLahir())){
				pegawaiTertua = pegawaiInstansi.get(i);
			}
		}
		
	return pegawaiTertua;
	}

	@Override
	public PegawaiModel findPegawaiTermuda(long idInstansi) {
		List<PegawaiModel> pegawaidiInstansi = new ArrayList<PegawaiModel>(); //mengumpulkan seluruh pegawai dalam instansi tersebut
		for (PegawaiModel pegawai :this.getAllPegawai()) {
			if (pegawai.getInstansi().getId() == idInstansi) { //jika pegawai memiliki id instansi sama dengan idInstansi yang dicari (ex: aceh)
				pegawaidiInstansi.add(pegawai);
			}
		}
		//melakukan pengecekan untuk setiap id pegawai(Instansi) dimulai dr idx 0
		PegawaiModel pegawaiDituju = pegawaidiInstansi.get(0); 
		for (int i=1; i<pegawaidiInstansi.size(); i++) { 
			if(pegawaidiInstansi.get(i).getTanggalLahir().after(pegawaiDituju.getTanggalLahir())) {
				pegawaiDituju = pegawaidiInstansi.get(i);
			}
		}
		return pegawaiDituju;

	}

	@Override
	public PegawaiModel getPegawaiById(Long idPegawai) {
		return pegawaiDb.findById(idPegawai).get();
		
	}

	@Override
	public void addPegawai(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
		
	}

	@Override
	public void updatePegawai(PegawaiModel pegawai) {
		PegawaiModel updatePegawai = pegawaiDb.getOne(pegawai.getId());
		updatePegawai.setInstansi(pegawai.getInstansi());
		updatePegawai.setNama(pegawai.getNama());
		updatePegawai.setTahunMasuk(pegawai.getTahunMasuk());
		updatePegawai.setTanggalLahir(pegawai.getTanggalLahir());
		updatePegawai.setTempatLahir(pegawai.getTempatLahir());
		//pegawai.setNip(this.nipGenerator(pegawai));
		//System.out.println("NIP BARU: "+this.nipGenerator(pegawai));
	    pegawaiDb.save(pegawai);
		
	}


}
	

	


