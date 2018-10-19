package com.apap.tugas1.controller;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.apap.tugas1.service.*;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.model.*;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	@Autowired
	private JabatanService jabatanService;
	
	@RequestMapping("/")
	private String home(Model model) {
		List<JabatanModel> get_allJabatan = jabatanService.find_allJabatan();	
		model.addAttribute("listJabatan",get_allJabatan);
		return"home";
	}
	
	/*
	 * Respon view berdasarkan nip
	 */
	 
	@RequestMapping(value = "/viewpegawai", method = RequestMethod.GET)
	private String  showPegawai(@RequestParam("nipPegawai") String nipPegawai, Model model) {
		PegawaiModel getPegawaiModel = pegawaiService.getPegawaiDetailByNip(nipPegawai).get();
		String nip = nipPegawai;
		model.addAttribute("pegawai",getPegawaiModel);
		model.addAttribute("nipPegawai",nip); 
		double gaji = 0;
		double gajiTerbesar = 0;
		        for (JabatanModel jabatanPegawai: getPegawaiModel.getJabatanList() ) {
		            if (jabatanPegawai.getGajiPokok() > gajiTerbesar) {
		                gajiTerbesar = jabatanPegawai.getGajiPokok();
		            }
		        }
		        gaji = gajiTerbesar;
		        double tunjangan = getPegawaiModel.getInstansi().getProvinsi().getPresentaseTunjangan();
		        gaji += (gaji * tunjangan/100);
		        Integer gajiInt = (int) gaji;
		model.addAttribute("gaji",gajiInt);
		
		return "view-pegawai";
	}
	

	
}



