package com.apap.tugas1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.repository.JabatanDb;


@Service
@Transactional
public class JabatanServiceImpl implements JabatanService {
	
	@Autowired
	private JabatanDb jabatanDb;

	@Override
	public void addJabatan(JabatanModel jabatan) {
		jabatanDb.save(jabatan);
		
	}

	@Override
	public JabatanModel getJabatanById(Long idJabatan) {
		return jabatanDb.findById(idJabatan).get();
	}

	@Override
	public List<JabatanModel> find_allJabatan() {
		// TODO Auto-generated method stub
		return jabatanDb.findAll();
	}

	@Override
	public void updateJabatanForm(long id, String nama, String deskripsi, double gajiPokok) {
		jabatanDb.findById(id).get().setNama(nama);
		jabatanDb.findById(id).get().setDeskripsi(deskripsi);
		jabatanDb.findById(id).get().setGajiPokok(gajiPokok);
	}

	@Override
	public void deleteJabatan(long id) {
		jabatanDb.deleteById(id);
		
	}


	

	
	
	

}
