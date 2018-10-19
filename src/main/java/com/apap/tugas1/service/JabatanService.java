package com.apap.tugas1.service;

import java.util.ArrayList;
import java.util.List;

import com.apap.tugas1.model.JabatanModel;

public interface JabatanService {
	void addJabatan (JabatanModel jabatan);
	JabatanModel getJabatanById(Long idJabatan);
	List<JabatanModel> find_allJabatan();
	void updateJabatanForm(long id,String nama, String deskripsi, double gajiPokok);
	void deleteJabatan(long id);
}


