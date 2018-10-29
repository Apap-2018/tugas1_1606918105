package com.apap.tugas1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;

@Controller
public class JabatanController {
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private PegawaiService pegawaiService;
	
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
		return "hasilaktivitas";
	}

	@RequestMapping(value = "/jabatan/view", method = RequestMethod.GET)
	private String viewJabatan(@RequestParam("idJabatan") Long idJabatan, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanById(idJabatan);
		String nama= jabatanService.getJabatanById(idJabatan).getNama();
		String deskripsi = jabatanService.getJabatanById(idJabatan).getDeskripsi();
		double gajidouble =jabatanService.getJabatanById(idJabatan).getGajiPokok();
		String gaji = String.format("%.0f", gajidouble); 
		
		model.addAttribute("jabatan", jabatan);
		model.addAttribute("namajabatan", nama);
		model.addAttribute("deskripsijabatan",deskripsi);
		model.addAttribute("gajijabatan", gaji);
		
		
		return "view-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/viewAll")
	private String viewAllJabaran(Model model) {
		List<JabatanModel> listofAllJabatan = jabatanService.find_allJabatan();
		model.addAttribute("listofAllJabatan", listofAllJabatan);
		//String gaji = String.format("%.0f", gajidouble); 
		return "viewAllJabatan";
	}
	
	@RequestMapping(value = "/jabatan/ubah" , method=RequestMethod.GET)
	private String updateJabatan(@RequestParam("idJabatan") Long id, Model model ) {
		JabatanModel jabatan = jabatanService.getJabatanById(id);
		model.addAttribute("dataupdatJabatan",new JabatanModel());
		model.addAttribute("jabatan", jabatan);
		return "formUbahJabatan";
	}
	
	@RequestMapping(value = "/jabatan/ubah", method=RequestMethod.POST)
	private String updateJabatan( @ModelAttribute JabatanModel jabatan, Model model) {
		jabatanService.updateJabatanForm(jabatan.getId() , jabatan.getNama(), jabatan.getDeskripsi(), jabatan.getGajiPokok());
		
		return "redirect:/jabatan/view?idJabatan=" + jabatan.getId();
	}
	
	@RequestMapping(value = "/jabatan/hapus", method=RequestMethod.POST)
	private String hapusJabatan( @ModelAttribute JabatanModel jabatan, Model model) {
		for(PegawaiModel pegawai : pegawaiService.getAllPegawai()) {
			for(JabatanModel cekjabatan : pegawai.getJabatanList()) {
				//cek apakah jabatan tersebut sama degan id
				if (cekjabatan.getId() == jabatan.getId()) {
					model.addAttribute("msg", "Hapus Jabatan Tidak Berhasil");
					return "hasilaktivitas";
				}
			}
		}
		model.addAttribute("msg", "Sukses Hapus Jabatan");
		jabatanService.deleteJabatan(jabatan.getId());
		return "hasilaktivitas";
	}
}

