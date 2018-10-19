package com.apap.tugas1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.JabatanService;

@Controller
public class JabatanController {
	
	@Autowired
	private JabatanService jabatanService;
	
	@RequestMapping(value="/jabatan/tambah")
	private String tamabah(Model model) {
		JabatanModel newJabatan = new JabatanModel();
		model.addAttribute("jabatan", newJabatan);
		return "tambahJabatan";
	}
	
	
	@RequestMapping(value = "/jabatan/tambah/create", method = RequestMethod.POST)
	private String tambahJabatan(@ModelAttribute JabatanModel jabatan, Model model ) {
		jabatanService.addJabatan(jabatan);
		model.addAttribute("msg","Jabatan berhasil ditambah");
		return "addsukses";
	}

	@RequestMapping(value = "/jabatan/view", method = RequestMethod.GET)
	private String viewJabatan(@RequestParam("idJabatan") Long idJabatan, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanById(idJabatan);
		String nama= jabatanService.getJabatanById(idJabatan).getNama();
		String deskripsi = jabatanService.getJabatanById(idJabatan).getDeskripsi();
		Integer gaji = (int)jabatanService.getJabatanById(idJabatan).getGajiPokok();
		
		model.addAttribute("namajabatan", nama);
		model.addAttribute("deskripsijabatan",deskripsi);
		model.addAttribute("gajijabatan", gaji);
		
		
		return "view-jabatan";
	
	
	}

}

